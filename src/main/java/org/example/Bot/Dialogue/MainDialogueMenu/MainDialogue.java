package org.example.Bot.Dialogue.MainDialogueMenu;

import org.example.Bot.Dialogue.IDialogue;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;

public class MainDialogue extends IDialogue
{
	@Override
	public void start()
	{
		stages = new HashMap<>();
		
		stages.put("global stage", new GlobalStage("global stage"));
		stages.put("new referee",new NewRefereeStage("new referee"));
		stages.put("new competition",new NewCompetitionStage("new competition"));
		stages.put("register",new RegisterStage("register"));
		stages.put("admin panel",new AdminPanelStage("admin panel"));


		changeStage("global stage");
	}

	@Override
	public void changeStage(String nextStage)
	{
		super.changeStage(nextStage);
	}

	@Override
	public void receiveUpdate(Update update)
	{
		super.receiveUpdate(update);
	}
}
