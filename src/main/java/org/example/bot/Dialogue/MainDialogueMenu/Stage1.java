package main.java.org.example.bot.Dialogue.MainDialogueMenu;

import main.java.org.example.bot.Dialogue.IStage;
import main.java.org.example.bot.TG.TGSender;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class Stage1 extends IStage
{
	public Stage1(List<IStage> list)
	{
		init(list.size());
	}

	@Override
	public void action()
	{
		TGSender.send("[[1 стадия меню]]");
		TGSender.send("Для выхода, нажмите: 0");
	}

	@Override
	public int preValidation(Update update)
	{
		if(update.getMessage().getText().equals("0")) return 0;
		return 1;
	}

	@Override
	public void addValidators()
	{
		validators.put(0, (Validateable) -> true);
	}
}
