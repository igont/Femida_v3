package main.java.org.example.bot.Dialogue.MainDialogueMenu;

import main.java.org.example.bot.Dialogue.IStage;
import main.java.org.example.bot.TG.TGSender;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class StageNewReferee extends IStage
{
	public StageNewReferee(List<IStage> list)
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
		return stageNum;
	}

	@Override
	public void addValidators()
	{
		validators.put(0, (Validateable) -> true);
	}
}
