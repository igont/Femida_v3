package main.java.org.example.Bot.Dialogue;

import main.java.org.example.Bot.Dialogue.Interfaces.PreValidationResponse;
import main.java.org.example.Bot.Dialogue.Interfaces.ValidationResult;
import main.java.org.example.Bot.TG.TGSender;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Stack;

import static main.java.org.example.Bot.Dialogue.Interfaces.ValidationResult.*;

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
		PreValidationResponse preValidationResponse = currentStage.preValidation(answer);


		if(validateStage(preValidationResponse, currentStage, answer) == NOT_FOUND) // Проверяем данную стадию диалога на наличие обработчика команды
		{
			preValidationResponse = stages.get(0).preValidation(answer);

			// Если не нашли, то проверяем глобальную стадию со старотвыми командами (стадия 0)
			if(validateStage(preValidationResponse, stages.get(0), answer) == NOT_FOUND)
			{
				TGSender.send("❗️️️️Такой команды я не знаю");
			}
		}
	}

	private ValidationResult validateStage(PreValidationResponse preValidationResponse, IStage checkedStage, Answer answer)
	{
		switch(preValidationResponse.getValidationResult())
		{
			case FORCE_REPEAT ->
			{
				if(checkedStage.validators.get(preValidationResponse.getNextStage()).validate(answer))
				{
					changeStage(preValidationResponse.getNextStage());
				}
			}
			case NEXT_STAGE ->
			{
				if(checkedStage.stageNum != preValidationResponse.getNextStage())
				{
					if(checkedStage.validators.get(preValidationResponse.getNextStage()).validate(answer))
					{
						changeStage(preValidationResponse.getNextStage());
					}
				}
			}
			case NOT_FOUND ->
			{
				return NOT_FOUND;
			}
			case REPEAT ->
			{
				checkedStage.validators.get(preValidationResponse.getNextStage()).validate(null);
			}
		}
		return NEXT_STAGE;
	}

	public int getCurrentStage()
	{
		return currentStage.getStageNum();
	}
}

