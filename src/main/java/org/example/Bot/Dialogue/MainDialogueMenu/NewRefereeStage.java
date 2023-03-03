package main.java.org.example.Bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Bot.Dialogue.Answer;
import main.java.org.example.Bot.Dialogue.IStage;
import main.java.org.example.Bot.Dialogue.Interfaces.PreValidationResponse;
import main.java.org.example.Bot.Excel.ExcelParser;
import main.java.org.example.Bot.Files.MyFiles;
import main.java.org.example.Bot.Files.ResourcesFiles;
import main.java.org.example.Bot.TG.TGSender;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
		validators.put(6, (Answer answer) ->
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

			}
			catch(IOException | InvalidFormatException e)
			{
				TGSender.send("Это не похоже на книгу Excel, Вам нужно прислать тот же файл, который вы скачали из бота и внесли в него данные");
				return false;
			}
			return false;
		});
	}

	private static boolean isBookOriginal(ExcelParser parser)
	{
		boolean find = false;
		if(Objects.equals(parser.readCell(0, 1).getStringCellValue(), "Шаблон проведенного соревнования\n" + "Для использования в учетной " + "базе FEMIDA"))
			if(Objects.equals(parser.readCell(4, 2).getStringCellValue(), "Фамилия"))
				if(Objects.equals(parser.readCell(11, 7).getStringCellValue(), "Рейтинг"))
					if(Objects.equals(parser.readCell(6, 2).getStringCellValue(), "Название соревнования:"))
					{
						find = true;
					}
		return find;
	}
}
