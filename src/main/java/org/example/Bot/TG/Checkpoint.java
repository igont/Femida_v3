package main.java.org.example.Bot.TG;

import main.java.org.example.Main;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Checkpoint
{
	public static void in(Update update) // Получение сообщения ботом
	{
		Main.safeUpdateParser.setUpdate(update); // Просто сохранение апдейта

		Main.updateHandler.authoriseUser(update); // Регистрация пользователя

        // Если с пользоавтелем не ведется ниакакого диалога, то создается новый диалог
		if(Main.updateHandler.activeUser.dialogue.stages == null) Main.updateHandler.activeUser.dialogue.start();

        // Обработка запроса с помощью диалога
		Main.updateHandler.activeUser.dialogue.receiveUpdate(update);
	}
}
