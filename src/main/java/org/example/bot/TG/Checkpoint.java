package main.java.org.example.bot.TG;

import main.java.org.example.Main;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Checkpoint
{
	public static void in(Update update) // Получение сообщения ботом
	{
		Main.safeUpdateParser.setUpdate(update);

		Main.updateHandler.authoriseUser(update); // Регистрация пользователя

		if(Main.updateHandler.activeUser.dialogue.stages == null) Main.updateHandler.activeUser.dialogue.start();

		Main.updateHandler.activeUser.dialogue.receiveUpdate(update);
	}
}
