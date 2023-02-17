package main.java.org.example.bot.Excel.Templates;

import main.java.org.example.bot.Excel.RefereePosition;

public class Competition
{
	private int refereeID;
	private RefereePosition refereePosition;
	private int points;

	public Competition(int refereeID, RefereePosition refereePosition, int points)
	{
		this.refereeID = refereeID;
		this.refereePosition = refereePosition;
		this.points = points;
	}

	public Competition()
	{
		refereeID = -1;
		refereePosition = RefereePosition.NONE;
		points = 0;
	}

	@Override
	public String toString()
	{
		return "Competition{" + "refereeID=" + refereeID + ", refereePosition=" + refereePosition + ", points=" + points + '}';
	}

	public int getRefereeID()
	{
		return refereeID;
	}

	public void setRefereeID(int refereeID)
	{
		this.refereeID = refereeID;
	}

	public RefereePosition getRefereePosition()
	{
		return refereePosition;
	}

	public void setRefereePosition(RefereePosition refereePosition)
	{
		this.refereePosition = refereePosition;
	}

	public int getPoints()
	{
		return points;
	}

	public void setPoints(int points)
	{
		this.points = points;
	}
}
