package org.example.Bot.Excel.Templates;

import org.example.Bot.Excel.RefereePosition;

public record CompetitionMember(int refereeID, RefereePosition refereePosition, int points)
{
	@Override
	public String toString()
	{
		return "CompetitionMember{" + "refereeID=" + refereeID + ", refereePosition=" + refereePosition + ", points=" + points + '}';
	}
}
