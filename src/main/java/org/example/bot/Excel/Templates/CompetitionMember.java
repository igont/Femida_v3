package main.java.org.example.bot.Excel.Templates;

import main.java.org.example.bot.Excel.RefereePosition;

public record CompetitionMember(int refereeID, RefereePosition refereePosition, int points)
{
	@Override
	public String toString()
	{
		return "CompetitionMember{" + "refereeID=" + refereeID + ", refereePosition=" + refereePosition + ", points=" + points + '}';
	}
}
