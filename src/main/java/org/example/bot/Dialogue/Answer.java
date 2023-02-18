package main.java.org.example.bot.Dialogue;

import main.java.org.example.Main;
import main.java.org.example.bot.SafeUpdateParser;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.util.Objects;

import static main.java.org.example.Main.myBot;

public class Answer
{
	private String from = "";
	private String message;
	private String phone = "";
	private int currentStage;
	private int nextStage = -1;
	private InputStream DocumentInputStream = null;
	private Update update;

	public Answer(Update update)
	{
		currentStage = Main.dialogue.getCurrentStage();
		this.update = update;
		from = SafeUpdateParser.getName();

		if(update.hasMessage())
		{
			message = update.getMessage().getText(); // Просто текст
			if(update.getMessage().hasDocument())
			{
				String fileName = update.getMessage().getDocument().getFileName();

				String[] split = fileName.split("\\."); // TODO Переделать на поиск окончания .xlsx
				if(split.length == 2 & split[1] == "xlsx")
				{
					GetFile getFile = new GetFile(update.getMessage().getDocument().getFileId());
					String filePath;
					try
					{
						filePath = myBot.execute(getFile).getFilePath();
						//myBot.downloadFile(filePath,new File("C:\\Users\\masha\\Desktop\\" + fileName)); //   сохранение файла в память
						DocumentInputStream = myBot.downloadFileAsStream(filePath);
					}
					catch(TelegramApiException e)
					{
						throw new RuntimeException(e);
					}
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

	public InputStream getDocumentInputStream()
	{
		return DocumentInputStream;
	}

	public Update getUpdate()
	{
		return update;
	}

	public boolean hasPhone()
	{
		return !Objects.equals(phone, "");
	}
	public boolean hasDocument()
	{
		return DocumentInputStream != null;
	}

	@Override
	public String toString()
	{
		return "Answer{" + "from='" + from + '\'' + ", message='" + message + '\'' + ", phone='" + phone + '\'' + ", currentStage=" + currentStage + ", nextStage=" + nextStage + ", DocumentInputStream=" + DocumentInputStream + ", update=" + "" + '}';
	}
}
