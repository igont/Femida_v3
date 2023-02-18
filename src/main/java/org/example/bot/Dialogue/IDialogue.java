package main.java.org.example.bot.Dialogue;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public abstract class IDialogue
{
	public List<IStage> stages;
	private IStage currentStage;
	public int num;

	public void start()
	{

	}

	public void changeStage(int nextStage)
	{
		currentStage = stages.get(nextStage);
		currentStage.action();
	}

	public void receiveUpdate(Update update)
	{
		Answer answer = new Answer(update);

		// Сначала проверяем основную стадию
		if(!validateStage(answer, currentStage))
		{
			// Потом проверяем нулевую стадию с глобальными командами
			if(currentStage.stageNum != 0) validateStage(answer, stages.get(0));
		}
		System.out.println(answer);
	}

	private boolean validateStage(Answer answer, IStage checkedStage)
	{
		answer.setNextStage(checkedStage.preValidation(answer));

		if(checkedStage.getStageNum() == answer.getNextStage()) return false;

		if(checkedStage.validators.get(answer.getNextStage()).validate(answer))
		{
			changeStage(answer.getNextStage());
			answer.setNextStage(answer.getNextStage());
			return true;
		}
		return false;
	}

	public int getCurrentStage()
	{
		return currentStage.getStageNum();
	}
}

