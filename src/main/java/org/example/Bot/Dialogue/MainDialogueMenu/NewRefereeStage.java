package main.java.org.example.Bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Bot.Dialogue.Answer;
import main.java.org.example.Bot.Dialogue.IStage;
import main.java.org.example.Bot.Dialogue.Interfaces.PreValidationResponse;
import main.java.org.example.Bot.Excel.ExcelParser;
import main.java.org.example.Bot.Excel.Templates.Referee;
import main.java.org.example.Bot.Files.MyFiles;
import main.java.org.example.Bot.TG.TGSender;
import main.java.org.example.Main;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.*;

import static main.java.org.example.Bot.Dialogue.Interfaces.ValidationResult.*;

public class NewRefereeStage extends IStage
{
	public NewRefereeStage(Map<String, IStage> stages)
	{
		init(stages.size() + "");
	}
	
	@Override
	public void action()
	{
		TGSender.send("Скачайте шаблон, внесите необходимые данные и перешлите обратно:");
		
		TGSender sender = new TGSender();
		File fileToSend = Main.excelStorage.getRefereeBookFile();
		sender.setSendFile(fileToSend);
		sender.sendPreparedMessage();
	}
	
	
	@Override
	public PreValidationResponse preValidation(Answer answer)
	{
		if(Objects.equals(answer.getMessage(), "/NewReferee")) return new PreValidationResponse(REPEAT, stageName);
		if(answer.hasDocument()) return new PreValidationResponse(NEXT_STAGE, "7");
		return new PreValidationResponse(NOT_FOUND, "-1");
	}
	
	@Override
	public void addValidators()
	{
		validators.put(stageName + "", (answer) ->
		{
			action();
			
			//TGSender.send("Вы и так уже в процессе добавления нового судьи, отправьте заполненный файл");
			return false;
		});
		validators.put("7", (answer) ->
		{
			ExcelParser parser;
			List<Referee> refereeToAdd = new ArrayList<>();
			
			try
			{
				File excelBookFile = MyFiles.getFile(MyFiles.TEMP_ROOT + answer.getFileName());
				parser = new ExcelParser(excelBookFile);
				
				//				if(!isBookOriginal(parser))
				//				{
				//					TGSender.send("❗️Структура файла была изменена. Вы можете лишь добавлять текст в зеленые ячейки");
				//					return false;
				//				}
				
				int row = 5;
				
				while(getLineNum(parser, row) > 0)
				{
					Referee referee = new Referee();
					
					String surname = parser.getCell(row, 2).getStringCellValue();
					String name = parser.getCell(row, 3).getStringCellValue();
					String patronymic = parser.getCell(row, 4).getStringCellValue();
					
					if(Objects.equals(surname, ""))
					{
						TGSender.send(String.format("❗️%s строка: Фамилия не указана", row - 4));
						return false;
					}
					if(Objects.equals(name, ""))
					{
						TGSender.send(String.format("❗️%s строка: Имя не указано", row - 4));
						return false;
					}
					if(Objects.equals(patronymic, ""))
					{
						TGSender.send(String.format("❗️%s строка: Отчество не указано", row - 4));
						return false;
					}
					
					referee.setSurname(surname);
					referee.setName(name);
					referee.setPatronymic(patronymic);
					
					int id = Referee.findRefereeID(surname, name, patronymic);
					
					if(id > -1)
					{
						TGSender.send(String.format("" + "❗️ Аккаунт %s %s %s уже существует.\n" + "ℹ️ Проверьте список судей на второй странице документа.\n\n" + "Пришлите исправленную версию следующим сообщением", surname, name, patronymic));
						return false;
					}
					
					
					try
					{
						referee.setBirth(new Date(parser.getCell(row, 5).getDateCellValue().getTime()));
					}
					catch(NullPointerException ignored)
					{
					}
					referee.setCity(parser.getCell(row, 6).getStringCellValue());
					referee.setClubType(parser.getCell(row, 8).getStringCellValue());
					referee.setClubName(parser.getCell(row, 9).getStringCellValue());
					
					
					try
					{
						referee.setCategory(parser.getCell(row, 7).getStringCellValue());
					}
					catch(IllegalStateException e)
					{
						referee.setCategory(Math.round(parser.getCell(row, 7).getNumericCellValue()) + "");
					}
					
					
					referee.setPhone(parsePhoneNumber(row, 10, parser));
					row++;
					
					refereeToAdd.add(referee);
				}
				for(Referee referee : refereeToAdd)
				{
					Main.sql.addReferee(referee);
				}
				TGSender.send(String.format("✅ Успешно добавлено аккаунтов: %s", refereeToAdd.size()));
			}
			catch(IOException | InvalidFormatException e)
			{
				TGSender.send("Это не похоже на книгу Excel, Вам нужно прислать тот же файл, который вы скачали из бота и внесли в него данные");
				return false;
			}
			return false;
		});
	}
	
	private static int getLineNum(ExcelParser parser, int row)
	{
		if(parser.getCell(row, 1).getCellType() == CellType.NUMERIC)
		{
			return (int) parser.getCell(row, 1).getNumericCellValue();
		}
		else if(parser.getCell(row, 1).getCellType() == CellType.FORMULA)
		{
			if(parser.getCell(row, 1).getCachedFormulaResultType() == CellType.NUMERIC)
			{
				return (int) parser.getCell(row, 1).getNumericCellValue();
			}
		}
		return -1;
	}
	
	private String parsePhoneNumber(int row, int col, ExcelParser parser)
	{
		String phone;
		try
		{
			DecimalFormat decimalFormat = new DecimalFormat("#");
			double phoneD = parser.getCell(row, col).getNumericCellValue();
			phone = decimalFormat.format(phoneD);
		}
		catch(IllegalStateException e)
		{
			phone = parser.getCell(row, col).getStringCellValue();
		}
		phone = phone.replaceAll(" ", "");
		phone = phone.replaceAll("\\(", "");
		phone = phone.replaceAll("\\)", "");
		phone = phone.replaceAll("-", "");
		
		if(phone.length() > 10)
		{
			phone = phone.substring(phone.length() - 10);
		}
		
		if(phone.equals("0")) return "";
		
		phone = "8" + phone;
		return phone;
	}
	
	private static boolean isBookOriginal(ExcelParser parser)
	{
		if(!Objects.equals(parser.getCell(0, 1).getStringCellValue(), "Шаблон регистрации нового судьи\n" + "Для использования в учетной базе FEMIDA")) return false;
		if(!Objects.equals(parser.getCell(4, 1).getStringCellValue(), "№")) return false;
		if(!Objects.equals(parser.getCell(4, 10).getStringCellValue(), "Номер телефона")) return false;
		
		return true;
	}
}
