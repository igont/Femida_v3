package org.example.Bot.Dialogue.MainDialogueMenu;

import org.example.Bot.Dialogue.Answer;
import org.example.Bot.Dialogue.IStage;
import org.example.Bot.Excel.ExcelParser;
import org.example.Bot.Excel.RefereePosition;
import org.example.Bot.Excel.Templates.Competition;
import org.example.Bot.Excel.Templates.Referee;
import org.example.Bot.Files.MyFiles;
import org.example.Bot.TG.TGSender;
import org.example.Main;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class NewCompetitionStage extends IStage
{
	public NewCompetitionStage(String name)
	{
		super(name);
	}
	
	@Override
	public void action()
	{
		TGSender.send("Скачайте шаблон, внесите необходимые данные и перешлите обратно:");
		
		TGSender sender = new TGSender();
		File fileToSend = Main.excelStorage.getCompetitionBookFile();
		sender.setSendFile(fileToSend);
		sender.sendPreparedMessage();
	}
	
	@Override
	public String preValidation(Answer answer)
	{
		if(Objects.equals(answer.getMessage(), "/NewCompetition")) return stageName;
		if(answer.hasDocument()) return "process document";
		return null;
	}
	
	@Override
	public void addValidators()
	{
		validators.put(stageName, (answer) ->
		{
			action();
			return false;
		});
		
		validators.put("process document", (answer) ->
		{
			ExcelParser parser;
			try
			{
				File excelBookFile = MyFiles.getFile(MyFiles.TEMP_ROOT + answer.getFileName());
				parser = new ExcelParser(excelBookFile);
				
				//				if(!isBookOriginal(parser))
				//				{
				//					TGSender.send("❗️️️️Структура файла была изменена. Вы можете лишь добавлять текст в зеленые ячейки");
				//					return false;
				//				}
				
				String title = parser.getCell(6, 3).getStringCellValue();
				String place = parser.getCell(7, 3).getStringCellValue();
				Date date = new java.sql.Date(parser.getCell(8, 3).getDateCellValue().getTime());
				
				if(!isDataFilled(title, place, date)) return false;
				
				List<Integer> members = new ArrayList<>();
				List<String> carpets = new ArrayList<>();
				List<Float> grades = new ArrayList<>();
				List<RefereePosition> positions = new ArrayList<>();
				
				int[] membersArr = null;
				String[] carpetsArr = null;
				float[] gradesArr = null;
				RefereePosition[] positionsArr = null;
				
				
				int row = 12;
				
				while(true)
				{
					String fio = parser.getCell(row, 2).getStringCellValue();
					String[] fioArr = fio.split(" ");
					
					if(fioArr.length < 3) break;
					
					String surname = fioArr[0];
					String name = fioArr[1];
					String patronymic = fioArr[2];
					
					int refereeID = Referee.findRefereeID(surname, name, patronymic);
					
					RefereePosition refereePosition = RefereePosition.convertPositionTitle(parser.getCell(row, 3).getStringCellValue());
					
					int points = (int) parser.getCell(row, 5).getNumericCellValue();
					
					String carpet;
					try
					{
						carpet = parser.getCell(row, 4).getStringCellValue();
					}
					catch(IllegalStateException e)
					{
						carpet = Math.round(parser.getCell(row, 4).getNumericCellValue()) + "";
					}
					
					members.add(refereeID);
					carpets.add(carpet);
					grades.add((float) points);
					positions.add(refereePosition);
					
					membersArr = new int[members.size()];
					carpetsArr = new String[carpets.size()];
					gradesArr = new float[grades.size()];
					positionsArr = new RefereePosition[positions.size()];
					
					for(int i = 0; i < members.size(); i++)
					{
						membersArr[i] = members.get(i);
						carpetsArr[i] = carpets.get(i);
						gradesArr[i] = grades.get(i);
						positionsArr[i] = positions.get(i);
					}
					
					row++;
				}
				if(members.size() == 0)
				{
					TGSender.send("*❗️В соревновании нет ни одного участника.*\n - Необходим минимум один человек");
					return false;
				}
				
				Competition competition = new Competition(title, place, date, membersArr, carpetsArr, gradesArr, positionsArr);
				Main.sql.addCompetition(competition);
				TGSender.send("✅ Соревнование успешно добавлено");
			}
			catch(IOException | InvalidFormatException e)
			{
				TGSender.send("Это не похоже на книгу Excel, Вам нужно прислать тот же файл, который вы скачали из бота и внесли в него данные");
				return false;
			}
			return false;
		});
	}
	
	private static boolean isDataFilled(String title, String place, Date date)
	{
		String report = "*❗️Необходимо добавить:*\n";
		boolean needAReport = false;
		
		if(Objects.equals(title, ""))
		{
			report += " - Название\n";
			needAReport = true;
		}
		
		if(Objects.equals(place, ""))
		{
			report += " - Город\n";
			needAReport = true;
		}
		
		if(Objects.equals(date, null))
		{
			report += " - Дату\n";
			needAReport = true;
		}
		
		if(needAReport)
		{
			TGSender.send(report);
			return false;
		}
		return true;
	}
	
	private static boolean isBookOriginal(ExcelParser parser)
	{
		if(!Objects.equals(parser.getCell(0, 1).getStringCellValue(), "Шаблон проведенного соревнования\n" + "Для использования в учетной " + "базе FEMIDA")) return false;
		if(!Objects.equals(parser.getCell(11, 2).getStringCellValue(), "Фамилия")) return false;
		if(!Objects.equals(parser.getCell(11, 7).getStringCellValue(), "Рейтинг")) return false;
		if(!Objects.equals(parser.getCell(6, 2).getStringCellValue(), "Название соревнования:")) return false;
		
		return true;
	}
}
