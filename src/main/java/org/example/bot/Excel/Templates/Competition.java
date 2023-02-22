package main.java.org.example.bot.Excel.Templates;

import java.util.Date;
import java.util.List;

public record Competition(String title, String place, Date date, List<CompetitionMember> members)
{
	@Override
	public String toString()
	{
		return "Competition{" + "title='" + title + '\'' + ", place='" + place + '\'' + ", date=" + date + ", members=" + members + '}';
	}

}
