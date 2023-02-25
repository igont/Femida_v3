package main.java.org.example.Bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Bot.Dialogue.Answer;
import main.java.org.example.Bot.Dialogue.IStage;
import main.java.org.example.Bot.Files.MyFiles;
import main.java.org.example.Bot.Files.ResourcesFiles;
import main.java.org.example.Bot.TG.TGSender;

import java.io.File;
import java.util.List;

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
	public int preValidation(Answer answer)
	{
		return stageNum;
	}

	@Override
	public void addValidators()
	{

	}
}
