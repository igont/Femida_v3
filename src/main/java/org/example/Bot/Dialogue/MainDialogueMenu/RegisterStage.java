package org.example.Bot.Dialogue.MainDialogueMenu;

import org.example.Bot.TG.SafeUpdateParser;
import org.example.Bot.TG.TGSender;
import org.example.Bot.Dialogue.Answer;
import org.example.Bot.Dialogue.IStage;
import org.example.Bot.Excel.Templates.Referee;
import org.example.DataBase.SQL;
import org.example.Main;

import java.util.*;

public class RegisterStage extends IStage
{
	public RegisterStage(String name)
	{
		super(name);
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
	public String preValidation(Answer answer)
	{
		if(Objects.equals(answer.getMessage(), "/Cancel")) return "cancel";
		if(Objects.equals(answer.getMessage(), "/Continue")) return "continue";
		if(answer.getMessage().startsWith("/")) return null;
		return subStage;
	}
	
	@Override
	public void addValidators()
	{
		validators.put("cancel", (answer) ->
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
				referee.setPhone(SafeUpdateParser.getActiveUser().getPhoneNumber());
				
				TGSender.send("Для автоматического входа будет использован Ваш текущий номер телефона: " + referee.getPhone());
				TGSender.send("❔Ваша дата рождения: пример формата: 31.12.1999)");
			}
			
			return false;
		});
		
		validators.put("get birth", (answer) ->
		{
			subStage = "get city";
			
			String[] split = answer.getMessage().split("\\.");
			Calendar calendar = new GregorianCalendar();
			
			int day = Integer.parseInt(split[0]);
			int month = Integer.parseInt(split[1]);
			int year = Integer.parseInt(split[2]);
			
			calendar.set(year, month, day);
			referee.setBirth(SQL.convertDate(calendar.getTime()));
			
			TGSender.send("Дата рождения установлена: " + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR));
			TGSender.send("❔Ваш город проживания:");
			return false;
		});
		
		validators.put("get city", (answer) ->
		{
			referee.setCity(answer.getMessage());
			subStage = "get club";
			
			TGSender.send("❔Тип и название вашего клуба: \n\n(например: спортивный клуб Богатырь записывается как \"СК Богатырь\")");
			return false;
		});
		
		validators.put("get club", (answer) ->
		{
			String[] s = answer.getMessage().split(" ");
			referee.setClubType(s[0]);
			referee.setClubName(s[1]);
			
			TGSender.send(referee.toNiceString());
			TGSender.send("Запрос будет отправлен на рассмотрение\n\nПродолжить: /Continue\nОтменить /Cancel");
			return false;
		});
		
		validators.put("continue", (answer) ->
		{
			TGSender.send("Всё отлично!");
			return false;
		});
	}
}
