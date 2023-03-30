package org.example.Bot.TG;

import org.example.Bot.User;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SafeUpdateParser
{
	public static Update update;
	private static Map<String, User> users = new HashMap<>();
	
	public static String getName()
	{
		String fromName = "";
		
		if(update.hasMessage()) fromName = update.getMessage().getFrom().getUserName();
		else if(update.hasCallbackQuery()) fromName = update.getCallbackQuery().getFrom().getUserName();
		
		return fromName;
	}
	
	public static Long getChatID()  // Получаем ID пользователя разными способами
	{
		Long chatId = -1L;
		
		if(update.hasMessage()) chatId = update.getMessage().getChatId();
		else if(update.hasCallbackQuery()) chatId = update.getCallbackQuery().getMessage().getChatId();
		
		return chatId;
	}
	
	public static User getActiveUser()
	{
		String name = getName();
		
		if(!users.containsKey(name)) users.put(name, new User(name));
		
		return users.get(name);
	}
}
