package main.java.org.example.Bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Bot.Dialogue.Answer;
import main.java.org.example.Bot.Dialogue.IStage;
import main.java.org.example.Bot.Dialogue.Interfaces.PreValidationResponse;
import main.java.org.example.Bot.Dialogue.Interfaces.ValidationResult;
import main.java.org.example.Bot.Excel.Templates.Referee;
import main.java.org.example.Bot.TG.TGSender;
import main.java.org.example.Main;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;

import java.sql.SQLOutput;
import java.util.*;

public class RegisterStage extends IStage
{
	public RegisterStage(Map<String, IStage> stages)
	{
		init(stages.size() + "");
	}
	
	private String subStage = "get surname";
	
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
			return new PreValidationResponse(ValidationResult.NEXT_STAGE, "0");
		}
		if(answer.getMessage().startsWith("/")) return new PreValidationResponse(ValidationResult.NEXT_STAGE, "-1");
		return new PreValidationResponse(ValidationResult.NEXT_STAGE, subStage + "");
	}
	
	@Override
	public void addValidators()
	{
		validators.put("0", (answer) ->
		{
			TGSender.send("✅Регистрация аккаунта отменена");
			return true;
		});
		
		validators.put("get surname", (answer) ->
		{
			referee.setSurname(answer.getMessage());
			subStage = "get name";
			
			TGSender.send("❔Ваше настоящее имя:");
			return false;
		});
		validators.put("get name", (answer) ->
		{
			referee.setName(answer.getMessage());
			subStage = "get patronymic";
			
			TGSender.send("❔Ваше настоящее отчество:");
			return false;
		});
		validators.put("get patronymic", (answer) ->
		{
			referee.setPatronymic(answer.getMessage());
			subStage = "get birth";
			
			int id = Referee.findRefereeByFIO(referee.getSurname(), referee.getName(), referee.getPatronymic());
			
			if(id != -1)
			{
				TGSender.send("Аккаунт с таким ФИО уже существует. Обратитесь в поддержку для смены номер телефона /Support");
			}
			else
			{
				referee.setPhone(Main.updateHandler.getActiveUser().phoneNumber);
				
				TGSender.send("Для автоматического входа будет использован Ваш текущий номер телефона: " + referee.getPhone());
				TGSender.send("Ваша дата рождения: пример формата: 31.12.1999)");
			}
			
			return false;
		});
		validators.put("get birth", (answer) ->
		{
			referee.setPatronymic(answer.getMessage());
			subStage = "get birth";
			
			String[] split = answer.getMessage().split("\\.");
			Calendar calendar = new GregorianCalendar();
			
			int day = Integer.parseInt(split[0]);
			int month = Integer.parseInt(split[1]);
			int year = Integer.parseInt(split[2]);
			
			calendar.set(year,month,day);
			TGSender.send(day + "." + month + "." + year);
			TGSender.send("Дата рождения установлена: " + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR));
			return false;
		});
	}
}
