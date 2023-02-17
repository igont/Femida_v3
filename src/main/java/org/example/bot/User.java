package main.java.org.example.bot;

import main.java.org.example.Main;
import org.telegram.telegrambots.meta.api.objects.Update;

public class User
{
	public final String name;
	public String phoneNumber;
	private int lastMessageId;
	public Update lastUpdate;

	public User(String name)
	{
		this.name = name;
	}

	public Update getLastUpdate()
	{
		return lastUpdate;
	}
}
