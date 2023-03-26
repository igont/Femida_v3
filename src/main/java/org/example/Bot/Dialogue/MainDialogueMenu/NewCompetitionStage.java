package main.java.org.example.Bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Bot.Dialogue.Answer;
import main.java.org.example.Bot.Dialogue.IStage;
import main.java.org.example.Bot.Dialogue.Interfaces.PreValidationResponse;
import main.java.org.example.Bot.Excel.ExcelParser;
import main.java.org.example.Bot.Excel.RefereePosition;
import main.java.org.example.Bot.Excel.Templates.Competition;
import main.java.org.example.Bot.Excel.Templates.Referee;
import main.java.org.example.Bot.Files.MyFiles;
import main.java.org.example.Bot.Files.ResourcesFiles;
import main.java.org.example.Bot.TG.TGSender;
import main.java.org.example.Main;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static main.java.org.example.Bot.Dialogue.Interfaces.ValidationResult.*;

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
		File fileToSend = Main.excelStorage.getCompetitionBookFile();
		sender.setSendFile(fileToSend);
		sender.sendPreparedMessage();
	}
	
	@Override
	public PreValidationResponse preValidation(Answer answer)
	{
		if(Objects.equals(answer.getMessage(), "/NewCompetition")) return new PreValidationResponse(REPEAT, stageNum);
		if(answer.hasDocument()) return new PreValidationResponse(NEXT_STAGE, 6);
		return new PreValidationResponse(NOT_FOUND, -1);
	}
	
	@Override
	public void addValidators()
	{
		validators.put(stageNum, (Answer) ->
		{
			action();
			return false;
		});
		
		validators.put(6, (Answer answer) ->
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
