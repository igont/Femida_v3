package main.java.org.example.bot.Dialogue;

import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.util.List;

import static main.java.org.example.Main.myBot;

public abstract class IDialogue
{
	public List<IStage> stages;
	private IStage currentStage;
	public int num;

	public void start()
	{

	}

	public void changeStage(int nextStage)
	{
		currentStage = stages.get(nextStage);
		currentStage.action();
	}

	public void receiveUpdate(Update update)
	{
		Answer answer = updateToAnswer(update);

		answer.nextStage = currentStage.preValidation(update);
		System.out.println(answer);

		if(currentStage.getStageNum() == answer.nextStage) return;


		if(currentStage.validators.get(answer.nextStage).validate(answer.validateable))
		{
			changeStage(answer.nextStage);
		}
	}

	private Answer updateToAnswer(Update update)
	{
		Answer answer = new Answer();

		if(update.hasMessage())
		{
			answer.validateable.message = update.getMessage().getText(); // Просто текст
			if(update.getMessage().hasDocument())
			{
				String fileName = update.getMessage().getDocument().getFileName();

				String[] split = fileName.split(".");
				if(split.length == 2 & split[1] == "xlsx")
				{
					GetFile getFile = new GetFile(update.getMessage().getDocument().getFileId());
					String filePath;
					try
					{
						filePath = myBot.execute(getFile).getFilePath();
						//myBot.downloadFile(filePath,new File("C:\\Users\\masha\\Desktop\\" + fileName)); //   сохранение файла в память
						answer.validateable.inputStream = myBot.downloadFileAsStream(filePath);
					}
					catch(TelegramApiException e)
					{
						throw new RuntimeException(e);
					}
				}
			}
		}
		return answer;
	}

}

