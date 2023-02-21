package main.java.org.example.bot.Dialogue.MainDialogueMenu;

import main.java.org.example.bot.Dialogue.Answer;
import main.java.org.example.bot.Dialogue.IStage;

import java.util.List;

public class CompetitionStage extends IStage // потом переименовать класс
{
	public CompetitionStage(List<IStage> list)
	{
		init(list.size());
	}

	@Override
	public void action()
	{

	}

	@Override
	public int preValidation(Answer answer)
	{
		return 0;
	}

	@Override
	public void addValidators()
	{

	}
}
