package main.java.org.example.bot.Files;

public enum ResourcesFiles
{

	TEMPLATE_COMPETITION("Template_Competition.xlsx"),
	TEMPLATE_REFEREE("Template_Referee.xlsx");

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
