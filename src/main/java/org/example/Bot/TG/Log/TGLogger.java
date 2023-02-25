package main.java.org.example.Bot.TG.Log;

public class TGLogger
{
	public static void logMethod(String method, Object... variables)
	{
		String s = method + "() <--";
		for(Object var : variables)
		{
			s += String.format(" [%s],", var.toString().replaceAll("\\n", "\t"));
		}
		send(s.substring(0, s.length() - 1));
	}

	public static void logMethodReturn(String method, Object... variables)
	{
		String s = method + "() -->";
		for(Object var : variables)
		{
			s += String.format(" [%s],", var.toString());
		}
		send(s.substring(0, s.length() - 1));

	}

	public static void send(String message)
	{
		if(message == null) return;
		System.out.println(message);
	}
}
