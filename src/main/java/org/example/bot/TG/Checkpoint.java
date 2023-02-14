package main.java.org.example.bot.TG;

import main.java.org.example.Main;
import main.java.org.example.bot.TG.Log.TGLogger;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Checkpoint
{
	public static void in(Update update) // Получение сообщения ботом
	{
		TGLogger.send(Main.updateHandler.authoriseUser(update)); // Регистрация пользователя
		if(Main.dialogue.stages == null) Main.dialogue.start();
		Main.dialogue.receiveUpdate(update);
	}
}
