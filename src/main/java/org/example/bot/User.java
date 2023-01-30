package main.java.org.example.bot;

import main.java.org.example.Main;
import org.telegram.telegrambots.meta.api.objects.Update;

public class User
{
	public final String name;
	private int lastMessageId;
	public Update lastUpdate;

	public User(String name)
	{
		this.name = name;
	}

	public static String getName() // Получаем Имя пользователя разными способами
	{
		String fromName = "";
		Update update = Main.updateHandler.activeUser.getLastUpdate();

		if(update.hasMessage()) fromName = update.getMessage().getFrom().getUserName();
		else if(update.hasCallbackQuery()) fromName = update.getCallbackQuery().getFrom().getUserName();

		return fromName;
	}
	public Update getLastUpdate()
	{
		return lastUpdate;
	}
}
