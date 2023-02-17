package main.java.org.example;

import main.java.org.example.bot.BotStarter;
import main.java.org.example.bot.Dialogue.IDialogue;
import main.java.org.example.bot.Dialogue.MainDialogueMenu.MainDialogue;
import main.java.org.example.bot.SafeUpdateParser;
import main.java.org.example.bot.UpdateHandler;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main
{
	public static final BotStarter myBot = new BotStarter();
	public static final UpdateHandler updateHandler = new UpdateHandler();
	public static IDialogue dialogue = new MainDialogue();
	public static SafeUpdateParser safeUpdateParser = new SafeUpdateParser();

	public static void main(String[] args)
	{
		startBot();
	}

	private static String startBot()
	{
		try
		{
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(myBot);
			return "Connected";
		}
		catch(TelegramApiException e)
		{
			e.printStackTrace();
			return "Error";
		}
	}
}