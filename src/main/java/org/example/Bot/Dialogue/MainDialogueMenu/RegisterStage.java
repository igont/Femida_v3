package main.java.org.example.Bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Bot.Dialogue.Answer;
import main.java.org.example.Bot.Dialogue.IStage;
import main.java.org.example.Bot.Dialogue.Interfaces.PreValidationResponse;
import main.java.org.example.Bot.Dialogue.Interfaces.ValidationResult;
import main.java.org.example.Bot.Excel.Templates.Referee;
import main.java.org.example.Bot.TG.TGSender;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Objects;

public class RegisterStage extends IStage
{
	public RegisterStage(List<IStage> list)
	{
		init(list.size());
	}
	
	private int subStage = 1;
	
	Referee referee = new Referee();
	
	@Override
	public void action()
	{
		TGSender.send("ℹ️Вам необходимо ответить на все вопросы бота, после чего, подтвердить отправку.\n\nℹ️Выйти из режима создания аккаунта можно, выбрав команту /Cancel из Меню снизу.");
		TGSender.send("❔Ваша настоящая фамилия:");
	}
	
	@Override
	public PreValidationResponse preValidation(Answer answer)
	{
		if(Objects.equals(answer.getMessage(), "/Cancel"))
		{
			return new PreValidationResponse(ValidationResult.NEXT_STAGE, 0);
		}
		return new PreValidationResponse(ValidationResult.NEXT_STAGE, subStage);
	}
	
	@Override
	public void addValidators()
	{
		validators.put(0, (Answer) ->
		{
			TGSender.send("Регистрация аккаунта отменена");
			return true;
		});
		
		validators.put(1, (Answer) ->
		{
			referee.setSurname(Answer.getMessage());
			System.out.println(referee.getSurname());
			subStage++;
			
			TGSender.send("❔Ваше настоящее имя:");
			
			return false;
		});
		validators.put(2, (Answer) ->
		{
			referee.setName(Answer.getMessage());
			System.out.println(referee.getName());
			subStage++;
			
			return false;
		});
	}
}
