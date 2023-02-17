package main.java.org.example.bot.Excel;

public enum RefereePosition
{
	CHIEF_COMPETITION_REFEREE("Главный судья соревнований"),
	REFEREE("Рефери"),
	SIDE_REFEREE("Боковой судья"),
	CARPET_MANAGER("Руководитель ковра"),
	CHIEF_SECRETARY("Главный секретарь"),
	SECRETARY("Секретарь"),
	NONE("Неизвестно");

	private final String positionTitle;

	RefereePosition(String title)
	{
		positionTitle = title;
	}

	public String getPositionTitle()
	{
		return positionTitle;
	}
}
