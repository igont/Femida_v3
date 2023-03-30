package org.example.Bot.Dialogue;

import org.example.Bot.Dialogue.Interfaces.ValidationResult;
import org.example.Bot.TG.SafeUpdateParser;
import org.example.Bot.TG.TGSender;
import org.example.Main;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

public abstract class IDialogue
{
	public Map<String, IStage> stages;
	private IStage currentStage;
	
	public void start()
	{
	
	}
	
	public void changeStage(String nextStage)
	{
		currentStage = stages.get(nextStage);
		currentStage.action();
	}
	
	public void receiveUpdate(Update update)
	{
		Answer answer = new Answer(update);
		
		// Проверяем данную стадию диалога на наличие обработчика команды
		if(validateStage(currentStage, answer) == ValidationResult.NOT_FOUND)
		{
			// Если не нашли, то проверяем глобальную стадию со старотвыми командами (стадия 0)
			if(validateStage(stages.get("global stage"), answer) == ValidationResult.NOT_FOUND)
			{
				TGSender.send("❗️️️️Такой команды я не знаю");
			}
			else
			{
				// Если нашли нужную команду в глобальной стадии, по пусть она за нами и значится
				changeStage("global stage");
			}
		}
	}
	
	private ValidationResult validateStage(IStage stageFrom, Answer answer)
	{
		String name = SafeUpdateParser.getActiveUser().getName();
		
		String currentStageValidatorName = stageFrom.preValidation(answer);
		System.out.printf("from:\t\t[%s]\nmessage:\t[%s]\nstage:\t\t[%s]\nquery: \t\t[%s]\n-------------------------\n", name,answer.getMessage(), stageFrom.stageName, currentStageValidatorName);
		
		if(currentStageValidatorName == null)
		{
			System.out.println(name + ": " + stageFrom.stageName + " --> ??? --> global stage");
			System.out.println("-------------------------");
			
			return ValidationResult.NOT_FOUND;
		}
		
		if(stageFrom.validators.get(currentStageValidatorName).validate(answer)) // Валидируемся на следующую
		{
			System.out.println(name + ": " + stageFrom.stageName + " --> " + currentStageValidatorName);
			System.out.println("-------------------------");
			changeStage(currentStageValidatorName);
		}
		else
		{
			System.out.println(name + ": " + stageFrom.stageName + " --> " + currentStageValidatorName + " [false] --> global stage");
			System.out.println("-------------------------");
		}
		return ValidationResult.NEXT_STAGE;
	}
	
	public String getCurrentStage()
	{
		return currentStage.getStageName();
	}
}