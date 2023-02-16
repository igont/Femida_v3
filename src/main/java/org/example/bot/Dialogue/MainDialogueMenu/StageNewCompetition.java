package main.java.org.example.bot.Dialogue.MainDialogueMenu;

import main.java.org.example.bot.Dialogue.IStage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class StageNewCompetition extends IStage
{
	public StageNewCompetition(List<IStage> list)
	{
		init(list.size());
	}
	@Override
	public void action()
	{

	}

	@Override
	public int preValidation(Update update)
	{
		return 0;
	}

	@Override
	public void addValidators()
	{

	}
}
