package main.java.org.example.bot.Dialogue;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.InputStream;

public class Validateable
{
	public String message;
	public InputStream inputStream;
	public Update update;

	@Override
	public String toString()
	{
		return "Validateable{" + "message='" + message + '\'' + ", inputStream=" + inputStream + '}';
	}
}
