package main.java.org.example.Bot.Dialogue;

import main.java.org.example.Bot.Dialogue.Interfaces.IValidator;

import java.util.HashMap;
import java.util.Map;

public abstract class IStage

{
	protected String stageName;
	
	public IStage(String stageName)
	{
		this.stageName = stageName;
		addValidators();
	}
	
	public abstract void action();
	public abstract String preValidation(Answer answer);

	public Map<String, IValidator> validators = new HashMap<>();
	public abstract void addValidators();

	public String getStageName()
	{
		return stageName;
	}
}