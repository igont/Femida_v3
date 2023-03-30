package org.example.Bot.Files;

public enum ResourcesFiles
{

	TEMPLATE_COMPETITION("Template_Competition.xlsx"),
	TEMPLATE_REFEREE("Template_Referee.xlsx"),
	TEMPLATE_REFEREE_ORIGINAL("Sources\\Template_Referee.xlsx"),
	TEMPLATE_COMPETITION_ORIGINAL("Sources\\Template_Competition.xlsx");

	private String fileName;
	ResourcesFiles(String templateCompetition)
	{
		fileName = templateCompetition;
	}
	public String getFileName()
	{
		return fileName;
	}
}
