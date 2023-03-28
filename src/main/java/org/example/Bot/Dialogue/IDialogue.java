package main.java.org.example.Bot.Dialogue;

import main.java.org.example.Bot.Dialogue.Interfaces.ValidationResult;
import main.java.org.example.Bot.TG.TGSender;
import main.java.org.example.Main;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Objects;

import static main.java.org.example.Bot.Dialogue.Interfaces.ValidationResult.*;

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
		if(validateStage(currentStage, answer) == NOT_FOUND)
		{
			// Если не нашли, то проверяем глобальную стадию со старотвыми командами (стадия 0)
			if(validateStage(stages.get("global stage"), answer) == NOT_FOUND)
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
		String currentStageValidatorName = stageFrom.preValidation(answer);
		System.out.printf("from: [%s]\nstage: [%s]\nquery: [%s]\n-------------------------\n", Main.updateHandler.getActiveUser().name, stageFrom.stageName, currentStageValidatorName);
		
		if(currentStageValidatorName == null)
		{
			System.out.println(Main.updateHandler.getActiveUser().name + ": " + stageFrom.stageName + " --> ??? --> global stage");
			System.out.println("-------------------------");
			
			return NOT_FOUND;
		}
		
		if(stageFrom.validators.get(currentStageValidatorName).validate(answer)) // Валидируемся на следующую
		{
			System.out.println(Main.updateHandler.getActiveUser().name + ": " + stageFrom.stageName + " --> " + currentStageValidatorName);
			System.out.println("-------------------------");
			changeStage(currentStageValidatorName);
		}
		else
		{
			System.out.println(Main.updateHandler.getActiveUser().name + ": " + stageFrom.stageName + " --> " + currentStageValidatorName + " [false] --> global stage");
			System.out.println("-------------------------");
		}
		return NEXT_STAGE;
	}
	
	public String getCurrentStage()
	{
		return currentStage.getStageName();
	}
}