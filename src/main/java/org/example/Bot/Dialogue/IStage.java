package main.java.org.example.Bot.Dialogue;

import main.java.org.example.Bot.Dialogue.Interfaces.IValidator;
import main.java.org.example.Bot.Dialogue.Interfaces.PreValidationResponse;

import java.util.HashMap;
import java.util.Map;

public abstract class IStage
{
	protected String stageName;
	public abstract void action();
	public abstract PreValidationResponse preValidation(Answer answer);

	public Map<String, IValidator> validators = new HashMap<>();
	public abstract void addValidators();

	public String getStageName()
	{
		return stageName;
	}

	protected void init(String stageNum)
	{
		this.stageName = stageNum;
		addValidators();
	}
}