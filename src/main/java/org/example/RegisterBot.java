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
		
		// Если с пользоавтелем не ведется ниакакого диалога, то создается новый диалог
		if(SafeUpdateParser.getActiveUser().getDialogue().stages == null) SafeUpdateParser.getActiveUser().getDialogue().start();
		
		// Обработка запроса с помощью диалога
		SafeUpdateParser.getActiveUser().getDialogue().receiveUpdate(update);
	}
}
