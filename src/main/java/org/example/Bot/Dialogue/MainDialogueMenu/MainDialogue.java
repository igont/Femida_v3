package main.java.org.example.Bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Bot.Dialogue.IDialogue;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

public class MainDialogue extends IDialogue
{
	@Override
	public void start()
	{
		stages = new ArrayList<>();
		stages.add(new GlobalStage(stages));            	//0
		stages.add(new NewRefereeStage(stages));        	//1  NewReferee
		stages.add(null);                                   //2  GlobalRating
		stages.add(new NewCompetitionStage(stages));    	//3  NewCompetition
		stages.add(null);                                   //4  Register
		stages.add(null);                                   //5  GetPhone


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
