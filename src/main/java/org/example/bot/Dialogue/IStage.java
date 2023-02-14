package main.java.org.example.bot.Dialogue;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

public abstract class IStage
{
	protected int stageNum;
	public abstract void action();
	public abstract int preValidation(Update update);

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