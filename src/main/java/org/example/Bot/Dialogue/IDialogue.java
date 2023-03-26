package main.java.org.example.Bot.Dialogue;

import main.java.org.example.Bot.Dialogue.Interfaces.PreValidationResponse;
import main.java.org.example.Bot.Dialogue.Interfaces.ValidationResult;
import main.java.org.example.Bot.TG.TGSender;
import main.java.org.example.Main;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Stack;

import static main.java.org.example.Bot.Dialogue.Interfaces.ValidationResult.*;

public abstract class IDialogue
{
	public List<IStage> stages;
	private IStage currentStage;
	
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
			case NEXT_STAGE ->
			{
				try
				{
					if(checkedStage.stageNum != preValidationResponse.getNextStage()) // Если это не та же самая стадия
					{
						if(checkedStage.validators.get(preValidationResponse.getNextStage()).validate(answer)) // Валидируемся на следующую
						{
							System.out.println(Main.updateHandler.getActiveUser().name + ": " + checkedStage.stageNum + " --> " + preValidationResponse.getNextStage());
							changeStage(preValidationResponse.getNextStage());
						}
						else
						{
							System.out.println(Main.updateHandler.getActiveUser().name + ": " + checkedStage.stageNum + " --> " + preValidationResponse.getNextStage() + " [false] --> " + Main.updateHandler.getActiveUser().dialogue.currentStage.stageNum);
						}
					}
				}
				catch(NullPointerException e)
				{
					System.out.printf("❗Next stage: %s is null in stage: %s\n", preValidationResponse.getNextStage(), checkedStage.stageNum);
				}
			}
			
			case REPEAT ->
			{
				if(checkedStage.validators.get(preValidationResponse.getNextStage()).validate(answer)) // Валидируемся заново
				{
					System.out.println(Main.updateHandler.getActiveUser().name + ": " + checkedStage.stageNum + " --> " + checkedStage.stageNum);
					changeStage(preValidationResponse.getNextStage());
				}
				else
				{
					System.out.println(Main.updateHandler.getActiveUser().name + ": " + checkedStage.stageNum + " --> " + checkedStage.stageNum + " [false] --> " + Main.updateHandler.getActiveUser().dialogue.currentStage.stageNum);
				}
			}
			
			case FORCE_REPEAT ->
			{
				System.out.println(Main.updateHandler.getActiveUser().name + ": " + checkedStage.stageNum + " --> " + checkedStage.stageNum);
				checkedStage.validators.get(preValidationResponse.getNextStage()).validate(null); // Валидируемся без параметров
			}
			
			case NOT_FOUND ->
			{
				System.out.println(Main.updateHandler.getActiveUser().name + ": " + checkedStage.stageNum + " --> ???");
				return NOT_FOUND;
			}
		}
		return NEXT_STAGE;
	}
	
	public int getCurrentStage()
	{
		return currentStage.getStageNum();
	}
}

