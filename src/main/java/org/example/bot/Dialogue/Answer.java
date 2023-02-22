package main.java.org.example.bot.Dialogue;

import main.java.org.example.Main;
import main.java.org.example.bot.Files.MyFiles;
import main.java.org.example.bot.SafeUpdateParser;
import main.java.org.example.bot.TG.TGSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.Objects;

import static main.java.org.example.Main.myBot;

public class Answer
{
	private String from = "";
	private String message;
	private String phone = "";
	private int currentStage;
	private String fileName = "";
	private int nextStage = -1;
	private Update update;
	private Long chatID;

	public Answer(Update update)
	{
		currentStage = Main.updateHandler.activeUser.dialogue.getCurrentStage();
		this.update = update;
		from = SafeUpdateParser.getName();
		chatID = SafeUpdateParser.getChatID();

		if(update.hasMessage())
		{
			message = update.getMessage().getText();//.replace("@ARC_Femida_bot",""); // Просто текст
			if(update.getMessage().hasDocument())
			{
				String fileName = update.getMessage().getDocument().getFileName();

				String[] split = fileName.split("\\."); // TODO Переделать на поиск окончания .xlsx
				System.out.println(fileName);
				if(split.length == 2 & Objects.equals(split[1], "xlsx"))
				{
					GetFile getFile = new GetFile(update.getMessage().getDocument().getFileId());
					String filePath;
					try
					{
						filePath = myBot.execute(getFile).getFilePath();
						myBot.downloadFile(filePath, new File(MyFiles.getTempPath() + fileName)); //   сохранение файла в память
						this.fileName = fileName;
					}
					catch(TelegramApiException e)
					{
						throw new RuntimeException(e);
					}
				}
				else
				{
					TGSender.send("Принимаются только файлы xlsx. Вам нужно прислать тот же файл, который вы скачали из бота и внесли в него данные");
				}
			}
			if(update.getMessage().hasContact())
			{
				phone = update.getMessage().getContact().getPhoneNumber();
			}
		}
	}

	public String getPhone()
	{
		return phone;
	}

	public String getFrom()
	{
		return from;
	}

	public int getNextStage()
	{
		return nextStage;
	}

	public Answer setNextStage(int nextStage)
	{
		this.nextStage = nextStage;
		return this;
	}

	public String getMessage()
	{
		return message;
	}


	public Update getUpdate()
	{
		return update;
	}

	public boolean hasPhone()
	{
		return !Objects.equals(phone, "");
	}

	@Override
	public String toString()
	{
		return "Answer{" + "from='" + from + '\'' + ", message='" + message + '\'' + ", phone='" + phone + '\'' + ", currentStage=" + currentStage + ", fileName='" + fileName + '\'' + ", nextStage=" + nextStage + ", chatID=" + chatID + '}';
	}

	public boolean hasDocument()
	{
		return !Objects.equals(fileName, "");
	}

	public String getFileName()
	{
		return fileName;
	}
}
