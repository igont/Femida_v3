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
		if(!checkStage(update, currentStage))
		{
			if(currentStage.stageNum != 0) checkStage(update, stages.get(0)); // Нулевая стадия содержит глобальные команды
		}
	}

	private boolean checkStage(Update update, IStage checkedStage)
	{
		Answer answer = updateToAnswer(update);
		answer.nextStage = checkedStage.preValidation(update);

		if(checkedStage.getStageNum() == answer.nextStage) return false;

		if(checkedStage.validators.get(answer.nextStage).validate(answer.validateable))
		{
			changeStage(answer.nextStage);
			return true;
		}
		return false;
	}

	private Answer updateToAnswer(Update update)
	{
		Answer answer = new Answer();
		answer.validateable.update = update;

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

