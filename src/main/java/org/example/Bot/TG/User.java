package main.java.org.example.Bot.TG;

import main.java.org.example.Bot.Dialogue.IDialogue;
import main.java.org.example.Bot.Dialogue.MainDialogueMenu.MainDialogue;
import org.telegram.telegrambots.meta.api.objects.Update;

public class User
{
	public final String name;
	public String phoneNumber;
	public Update lastUpdate;

	public IDialogue dialogue = new MainDialogue();

	public User(String name)
	{
		this.name = name;
	}

	public Update getLastUpdate()
	{
		return lastUpdate;
	}
}
