package main.java.org.example.bot.Dialogue;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

public class Dialogue
{
	public List<Stage> stages;
	public Stage currentStage;

	public void start()
	{
		changeStage(0);
	}

	public void changeStage(int nextStage)
	{
		currentStage = stages.get(nextStage);
		currentStage.action.send();
	}

	public void ReceiveAnswer(Answer answer)
	{
		if(currentStage.validations.get(answer.nextStage).validate(answer.validateable))
		{
			changeStage(answer.nextStage);
		}
	}
}

class Stage
{
	public Action action;
	public Map<Integer, Validation> validations;
}

class Answer
{
	public int nextStage;
	public Validateable validateable;
}

class Validateable
{
	public String message;
	public FileInputStream fileInputStream;
}