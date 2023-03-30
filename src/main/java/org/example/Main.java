package org.example;

import org.example.Bot.Excel.ExcelStorage;
import org.example.DataBase.SQL;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Scanner;


public class Main
{
	public static RegisterBot myBot;
	public static ExcelStorage excelStorage;
	public static SQL sql;
	
	public static void main(String[] args)
	{
		Scanner console = new Scanner(System.in);
		try
		{
			String token = System.getenv("BOT_TOKEN");
			
			System.out.println("-----------------------------------------------------------");
			System.out.println("                          FEMIDA");
			System.out.println("-----------------------------------------------------------");
			
			if(token != null)
			{
				System.out.println("✅Token successfully loaded");
			}
			else
			{
				System.out.print("ENTER TOKEN: ");
				token = console.nextLine();
			}
			
			myBot = new RegisterBot(token);
			excelStorage = new ExcelStorage();
			sql = new SQL();
			
			startBot();
			System.out.println("-----------------------------------------------------------");
			System.out.println("                          FEMIDA");
			System.out.println("-----------------------------------------------------------");
			//sql.addRandomlyData();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			console.nextLine();
			console.nextLine();
		}
	}
	
	private static void startBot()
	{
		try
		{
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(myBot);
			System.out.println();
			System.out.println("✅Bot registered");
		}
		catch(TelegramApiException e)
		{
			System.out.println("❗️Bot registration fail");
			e.printStackTrace();
		}
	}
}