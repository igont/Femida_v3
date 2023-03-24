package main.java.org.example;

import main.java.org.example.Bot.Excel.ExcelStorage;
import main.java.org.example.Bot.TG.SafeUpdateParser;
import main.java.org.example.Bot.TG.UpdateHandler;
import main.java.org.example.DataBase.SQL;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;



public class Main
{
	public static final BotStarter myBot = new BotStarter();
	public static final UpdateHandler updateHandler = new UpdateHandler();
	public static final ExcelStorage excelStorage = new ExcelStorage();
	public static final SQL sql = new SQL();

	public static void main(String[] args)
	{
		startBot();
		sql.addRandomlyData();
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