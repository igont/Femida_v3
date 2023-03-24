package main.java.org.example.Bot;

import main.java.org.example.Bot.Dialogue.IDialogue;
import main.java.org.example.Bot.Dialogue.MainDialogueMenu.MainDialogue;
import main.java.org.example.Bot.Dialogue.Role;
import main.java.org.example.Bot.Dialogue.Possibility;

public class User
{
	public final String name;
	public int femidaID;

	public IDialogue dialogue = new MainDialogue();
	public String phoneNumber;
	
	public User(String name)
	{
		this.name = name;
		femidaID = -1;
		phoneNumber = "";
	}
}
