package main.java.org.example.Bot.TG;

import main.java.org.example.Main;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SafeUpdateParser
{
	private Update update;

	public static String getName()
	{
		String fromName = "";
		Update update = Main.updateHandler.activeUser.getLastUpdate();

		if(update.hasMessage()) fromName = update.getMessage().getFrom().getUserName();
		else if(update.hasCallbackQuery()) fromName = update.getCallbackQuery().getFrom().getUserName();

		return fromName;
	}

	public static Long getChatID()  // Получаем ID пользователя разными способами
	{
		Update update = Main.updateHandler.activeUser.getLastUpdate();
		Long chatId = -1L;

		if(update.hasMessage()) chatId = update.getMessage().getChatId();
		else if(update.hasCallbackQuery()) chatId = update.getCallbackQuery().getMessage().getChatId();

		return chatId;
	}

	public Update getUpdate()
	{
		return update;
	}

	public void setUpdate(Update update)
	{
		this.update = update;
	}
}
