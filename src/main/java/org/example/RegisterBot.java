package org.example;

import org.example.Bot.TG.SafeUpdateParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class RegisterBot extends TelegramLongPollingBot
{
	private String token;
	public RegisterBot(String token)
	{
		this.token = token;
	}
	
	@Override
	public String getBotUsername()
	{
		return "FEMIDA";
	}

	@Override
	public String getBotToken()
	{
		return token;
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
