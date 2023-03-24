package main.java.org.example.Bot.Excel;

import java.util.Random;

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

	public static RefereePosition convertPositionTitle(String title)
	{
		return switch(title)
		{
			case "Главный судья соревнований" -> CHIEF_COMPETITION_REFEREE;
			case "Рефери" -> REFEREE;
			case "Боковой судья" -> SIDE_REFEREE;
			case "Руководитель ковра" -> CARPET_MANAGER;
			case "Главный секретарь" -> CHIEF_SECRETARY;
			case "Секретарь" -> SECRETARY;
			default -> NONE;
		};
	}
	
	public static RefereePosition getRandomRefereePosition()
	{
		Random random = new Random();
		int num = random.nextInt(1, RefereePosition.values().length);
		return RefereePosition.values()[num];
	}
}
