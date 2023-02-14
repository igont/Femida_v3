package main.java.org.example.bot.Dialogue;

import java.io.InputStream;

public class Validateable
{
	public String message;
	public InputStream inputStream;

	@Override
	public String toString()
	{
		return "Validateable{" + "message='" + message + '\'' + ", inputStream=" + inputStream + '}';
	}
}
