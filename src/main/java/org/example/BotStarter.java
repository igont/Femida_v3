package main.java.org.example;

import main.java.org.example.Bot.TG.SafeUpdateParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BotStarter extends TelegramLongPollingBot
{
	@Override
	public String getBotUsername()
	{
		return System.getenv("BOT_NAME");
	}

	@Override
	public String getBotToken()
	{
		return System.getenv("BOT_TOKEN");
	}

	@Override
	public void onUpdateReceived(Update update)
	{
		SafeUpdateParser.update = update; // Просто сохранение апдейта
		
		Main.updateHandler.authoriseUser(update); // Регистрация пользователя
		
		// Если с пользоавтелем не ведется ниакакого диалога, то создается новый диалог
		if(Main.updateHandler.getActiveUser().dialogue.stages == null) Main.updateHandler.getActiveUser().dialogue.start();
		
		// Обработка запроса с помощью диалога
		Main.updateHandler.getActiveUser().dialogue.receiveUpdate(update);
	}
}
