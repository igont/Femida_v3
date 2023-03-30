package org.example.Bot;

import org.example.Bot.Dialogue.MainDialogueMenu.MainDialogue;
import org.example.Bot.Dialogue.IDialogue;

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
