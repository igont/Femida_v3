package main.java.org.example.bot.Dialogue.MainDialogueMenu;

import main.java.org.example.bot.Dialogue.Answer;
import main.java.org.example.bot.Dialogue.IStage;
import main.java.org.example.bot.Excel.ExcelParser;
import main.java.org.example.bot.Excel.RefereePosition;
import main.java.org.example.bot.Excel.Templates.Competition;
import main.java.org.example.bot.Excel.Templates.CompetitionMember;
import main.java.org.example.bot.Files.MyFiles;
import main.java.org.example.bot.Files.ResourcesFiles;
import main.java.org.example.bot.TG.TGSender;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCompetitionStage extends IStage
{
	public NewCompetitionStage(List<IStage> list)
	{
		init(list.size());
	}

	@Override
	public void action()
	{
		TGSender.send("Скачайте шаблон, внесите необходимые данные и перешлите обратно:");

		TGSender sender = new TGSender();
		File fileToSend = MyFiles.getFile(ResourcesFiles.TEMPLATE_COMPETITION);
		sender.setSendFile(fileToSend);
		sender.sendPreparedMessage();
	}

	@Override
	public int preValidation(Answer answer)
	{
		if(answer.hasDocument()) return 6;
		return stageNum;
	}

	@Override
	public void addValidators()
	{
		validators.put(6, (Answer answer) ->
		{
			ExcelParser parser;
			try
			{
				File excelBookFile = MyFiles.getFile(MyFiles.TEMP_ROOT + answer.getFileName());
				parser = new ExcelParser(excelBookFile);

				String title = parser.readCell(6, 3).getStringCellValue();
				String place = parser.readCell(7, 3).getStringCellValue();
				Date date = parser.readCell(8, 3).getDateCellValue();

				List<CompetitionMember> members = new ArrayList<>();
				int row = 12;

				while(parser.readCell(row, 2) != null)
				{
					int refereeID = -1; // TODO Получить данные из БД

					String surname = parser.readCell(row,2).getStringCellValue();
					String name = parser.readCell(row,3).getStringCellValue();
					String patronymic = parser.readCell(row,4).getStringCellValue();

					if((surname + name + patronymic).equals("")) break;

					RefereePosition refereePosition = RefereePosition.convertPositionTitle(parser.readCell(row,5).getStringCellValue());
					int points = (int)parser.readCell(row,6).getNumericCellValue();

					members.add(new CompetitionMember(refereeID,refereePosition,points));
					row ++;
				}

				Competition competition = new Competition(title, place, date, members);
				System.out.println(competition);
			}
			catch(IOException | InvalidFormatException e)
			{
				TGSender.send("Это не похоже на книгу Excel, Вам нужно прислать тот же файл, который вы скачали из бота и внесли в него данные");
				return false;
			}
			return true;
		});
	}
}
