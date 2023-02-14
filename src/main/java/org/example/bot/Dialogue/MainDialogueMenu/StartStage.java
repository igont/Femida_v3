package main.java.org.example.bot.Dialogue.MainDialogueMenu;

import main.java.org.example.bot.Dialogue.IStage;
import main.java.org.example.bot.TG.TGSender;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class StartStage extends IStage // Стадия приветствия
{
	public StartStage(List<IStage> list)
	{
		init(list.size());
	}
	@Override
	public void action()
	{
		TGSender.send("""
				Добро пожаловать в систему учета рейтинга спортивных судей "FEMIDA".

				С помощью бота вы сможете выполнять следующие действия:
				
				-Зарегистрировать нового судью:
				/NewReferee
				
				-Вывести рейтинг всех судей:
				/GlobalRating
				
				-Создать новое соревнование:
				/NewCompetition""");
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
