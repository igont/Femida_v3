package main.java.org.example.Bot.Dialogue;

import main.java.org.example.Bot.Dialogue.Interfaces.IValidator;

import java.util.HashMap;
import java.util.Map;

public abstract class IStage
{
	protected int stageNum;
	public abstract void action();
	public abstract int preValidation(Answer answer);

	public Map<Integer, IValidator> validators = new HashMap<>();
	public abstract void addValidators();

	public int getStageNum()
	{
		return stageNum;
	}

	protected void init(int stageNum)
	{
		this.stageNum = stageNum;
		addValidators();
	}
}