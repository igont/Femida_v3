package main.java.org.example.bot.Dialogue.MainDialogueMenu;

import main.java.org.example.bot.Dialogue.IDialogue;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

public class MainDialogue extends IDialogue
{
	@Override
	public void start()
	{
		stages = new ArrayList<>();
		stages.add(new GlobalStage(stages));
		stages.add(new StageNewCompetition(stages));

		changeStage(0);
	}

	@Override
	public void changeStage(int nextStage)
	{
		super.changeStage(nextStage);
	}

	@Override
	public void receiveUpdate(Update update)
	{
		super.receiveUpdate(update);
	}
}
