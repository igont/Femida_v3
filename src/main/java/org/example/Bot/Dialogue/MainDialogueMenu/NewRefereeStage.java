package main.java.org.example.Bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Bot.Dialogue.Answer;
import main.java.org.example.Bot.Dialogue.IStage;
import main.java.org.example.Bot.Dialogue.Interfaces.PreValidationResponse;
import main.java.org.example.Bot.Files.MyFiles;
import main.java.org.example.Bot.Files.ResourcesFiles;
import main.java.org.example.Bot.TG.TGSender;

import java.io.File;
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
	}
}
