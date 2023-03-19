package main.java.org.example.Bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Bot.Dialogue.Answer;
import main.java.org.example.Bot.Dialogue.IStage;
import main.java.org.example.Bot.Dialogue.Interfaces.PreValidationResponse;
import main.java.org.example.Bot.Excel.ExcelParser;
import main.java.org.example.Bot.Excel.Templates.Referee;
import main.java.org.example.Bot.Files.MyFiles;
import main.java.org.example.Bot.Files.ResourcesFiles;
import main.java.org.example.Bot.TG.TGSender;
import main.java.org.example.Main;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.*;

import static main.java.org.example.Bot.Dialogue.Interfaces.ValidationResult.*;

public class NewRefereeStage extends IStage
{
	public NewRefereeStage(List<IStage> list)
	{
		init(list.size());
	}
	
	@Override
	public void action()
	{
		TGSender.send("Скачайте шаблон, внесите необходимые данные и перешлите обратно:");
		
		TGSender sender = new TGSender();
		File fileToSend = MyFiles.getFile(ResourcesFiles.TEMPLATE_REFEREE);
		sender.setSendFile(fileToSend);
		sender.sendPreparedMessage();
	}
	
	@Override
	public PreValidationResponse preValidation(Answer answer)
	{
		if(Objects.equals(answer.getMessage(), "/NewReferee")) return new PreValidationResponse(REPEAT, stageNum);
		if(answer.hasDocument()) return new PreValidationResponse(NEXT_STAGE, 7);
		return new PreValidationResponse(NOT_FOUND, -1);
	}
	
	@Override
	public void addValidators()
	{
		validators.put(stageNum, (Answer) ->
		{
			TGSender.send("Вы и так уже в процессе добавления нового судьи, отправьте заполненный файл");
			return false;
		});
		validators.put(7, (Answer answer) ->
		{
			ExcelParser parser;
			
			try
			{
				File excelBookFile = MyFiles.getFile(MyFiles.TEMP_ROOT + answer.getFileName());
				parser = new ExcelParser(excelBookFile);
				
				if(!isBookOriginal(parser))
				{
					TGSender.send("❗️Структура файла была изменена. Вы можете лишь добавлять текст в зеленые ячейки");
					return false;
				}
				
				int row = 5;
				int count = 0;
				try
				{
					while(parser.readCell(row, 1).getNumericCellValue() >= 1)
					{
						Referee referee = new Referee();
						referee.setSurname(parser.readCell(row, 2).getStringCellValue());
						referee.setName(parser.readCell(row, 3).getStringCellValue());
						referee.setPatronymic(parser.readCell(row, 4).getStringCellValue());
						referee.setBirth(new Date(parser.readCell(row, 5).getDateCellValue().getTime()));
						referee.setCity(parser.readCell(row, 6).getStringCellValue());
						referee.setClubType(parser.readCell(row, 8).getStringCellValue());
						referee.setClubName(parser.readCell(row, 9).getStringCellValue());
						
						try
						{
							referee.setCategory(parser.readCell(row, 7).getStringCellValue());
						}
						catch(IllegalStateException e)
						{
							referee.setCategory(parser.readCell(row, 7).getNumericCellValue() + "");
						}
						
						DecimalFormat decimalFormat = new DecimalFormat("#");
						double phone = parser.readCell(row, 10).getNumericCellValue();
						referee.setPhone(decimalFormat.format(phone) + "");
						
						row++;
						count++;
						
						System.out.println("Добавлен судья " + referee);
						Main.sql.addReferee(referee);
					}
				}
				catch(IllegalStateException ignored)
				{
					TGSender.send(String.format("✅ Успешно добавлено %s аккаунтов",count));
				}
			}
			catch(IOException | InvalidFormatException e)
			{
				TGSender.send("Это не похоже на книгу Excel, Вам нужно прислать тот же файл, который вы скачали из бота и внесли в него данные");
				return false;
			} return false;
		});
	}
	
	private static boolean isBookOriginal(ExcelParser parser)
	{
		if(!Objects.equals(parser.readCell(0, 1).getStringCellValue(), "Шаблон регистрации нового судьи\n" + "Для использования в учетной базе FEMIDA")) return false;
		if(!Objects.equals(parser.readCell(4, 1).getStringCellValue(), "№")) return false;
		if(!Objects.equals(parser.readCell(4, 10).getStringCellValue(), "Номер телефона")) return false;
		
		return true;
	}
}
