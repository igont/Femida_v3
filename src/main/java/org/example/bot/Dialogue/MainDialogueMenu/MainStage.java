package main.java.org.example.bot.Dialogue.MainDialogueMenu;

import main.java.org.example.bot.Dialogue.IStage;
import main.java.org.example.bot.TG.TGSender;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class MainStage extends IStage
{
	public MainStage(List<IStage> list)
	{
		init(list.size());
	}
	@Override
	public void action()
	{
		TGSender.send("[[Основная стадия меню]]");
		TGSender.send("Для перехода на 1 стадию, напишите: 1");
		TGSender.send("Для перехода на 2 стадию, напишите: ты лох");
	}

	@Override
	public int preValidation(Update update)
	{
		if(update.getMessage().getText().equals("1")) return 1;
		if(update.getMessage().getText().equals("ты лох")) return 2;
		return 0;
	}

	@Override
	public void addValidators()
	{
		validators.put(1,(Validateable) -> true);
		validators.put(2,(Validateable) -> true);
	}
}
