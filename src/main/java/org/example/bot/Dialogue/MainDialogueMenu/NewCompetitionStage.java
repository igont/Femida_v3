package main.java.org.example.bot.Dialogue.MainDialogueMenu;

import main.java.org.example.bot.Dialogue.Answer;
import main.java.org.example.bot.Dialogue.IStage;
import main.java.org.example.bot.Excel.ExcelParser;
import main.java.org.example.bot.Files.MyFiles;
import main.java.org.example.bot.Files.ResourcesFiles;
import main.java.org.example.bot.TG.TGSender;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
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
				System.out.println("Книга открылась, считываю данные...");
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
