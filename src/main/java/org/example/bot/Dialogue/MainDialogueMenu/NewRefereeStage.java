package main.java.org.example.bot.Dialogue.MainDialogueMenu;

import main.java.org.example.bot.Dialogue.IStage;
import main.java.org.example.bot.Dialogue.Validateable;
import main.java.org.example.bot.Files.MyFiles;
import main.java.org.example.bot.Files.ResourcesFiles;
import main.java.org.example.bot.TG.TGSender;
import org.telegram.telegrambots.meta.api.objects.Update;

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
	public int preValidation(Update update)
	{
		return stageNum;
	}

	@Override
	public void addValidators()
	{

	}
}
