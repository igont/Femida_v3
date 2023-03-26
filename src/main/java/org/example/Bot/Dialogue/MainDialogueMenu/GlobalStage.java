package main.java.org.example.Bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Bot.Dialogue.Interfaces.PreValidationResponse;
import main.java.org.example.Bot.Dialogue.Possibility;
import main.java.org.example.Bot.Dialogue.Role;
import main.java.org.example.Bot.Excel.Templates.Referee;
import main.java.org.example.DataBase.SQL;
import main.java.org.example.Main;
import main.java.org.example.Bot.Dialogue.Answer;
import main.java.org.example.Bot.Dialogue.IStage;
import main.java.org.example.Bot.TG.SafeUpdateParser;
import main.java.org.example.Bot.TG.TGSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static main.java.org.example.Bot.Dialogue.Interfaces.ValidationResult.*;

public class GlobalStage extends IStage // Стадия приветствия
{
	public GlobalStage(List<IStage> list)
	{
		init(list.size());
	}
	
	@Override
	public void action()
	{
	}
	
	@Override
	public PreValidationResponse preValidation(Answer answer)
	{
		if(Objects.equals(answer.getMessage(), "/commands")) return new PreValidationResponse(FORCE_REPEAT, 0);
		if(Objects.equals(answer.getMessage(), "/start")) return new PreValidationResponse(FORCE_REPEAT, 0);
		
		if(Objects.equals(answer.getMessage(), "/NewReferee")) return new PreValidationResponse(NEXT_STAGE, 1);
		if(Objects.equals(answer.getMessage(), "/GlobalRating")) return new PreValidationResponse(NEXT_STAGE, 2);
		if(Objects.equals(answer.getMessage(), "/NewCompetition")) return new PreValidationResponse(NEXT_STAGE, 3);
		if(Objects.equals(answer.getMessage(), "/Account")) return new PreValidationResponse(NEXT_STAGE, 4);
		if(Objects.equals(answer.getMessage(), "/PlanCompetition")) return new PreValidationResponse(NEXT_STAGE, 6);
		if(Objects.equals(answer.getMessage(), "/Register")) return new PreValidationResponse(NEXT_STAGE, 7);
		
		if(answer.hasPhone())
		{
			Main.updateHandler.getActiveUser().phoneNumber = answer.getPhone();
			return new PreValidationResponse(NEXT_STAGE, 5);
		}
		
		return new PreValidationResponse(NOT_FOUND, -1);
	}
	
	@Override
	public void addValidators()
	{
		validators.put(0, (Answer) ->
		{
			int id = Main.updateHandler.getActiveUser().femidaID;
			Referee referee = new Referee(id);
			
			String head = "";
			if(id == -1)
			{
				if(!Objects.equals(Main.updateHandler.getActiveUser().phoneNumber, ""))
				{
					head = """
						Аккаунта с вашим номером телефона нет в списках судей.
						
						Для вас доступны стандартные действия:
						
						""";
				}
				else
				{
					head = """
						Для получения полного списка команд перейдите в ваш /Account, затем вновь вызовите список команд /commands из Меню.
						
						Пока для вас доступны базовые действия:
						
						""";
				}
			}else
			{
				head = referee.getFIO() + ", Вам доступны следующие команды:\n\n";
			}
			
			
			String newReferee = """
					➕*Зарегистрировать нового судью:*
					/NewReferee
					
					""";
			
			String newCompetition = """
					➕*Создать новое соревнование:*
					/NewCompetition
					
					""";
			
			String globalRating = """
					📃*Показать рейтинг судей:*
					/GlobalRating
					
					""";
			
			String planCompetition = """
					🕐*Запланировать соревнование*
					/PlanCompetition
					
					""";
			
			String account = """
					😐*Мой аккаунт*
					/Account
					
					""";
			
			String admin = """
					*Получить список админских команд*
					/getAdminPanel
					""";
			
			String commands = head;
			
			if(id != -1)
			{
				Role role = referee.getRole();
				
				if(role.getPossibility(Possibility.NEW_REFEREE)) commands += newReferee;
				if(role.getPossibility(Possibility.NEW_COMPETITION)) commands += newCompetition;
				if(role.getPossibility(Possibility.PLAN_COMPETITION)) commands += planCompetition;
				if(role.getPossibility(Possibility.ADMIN)) commands += admin;
			}
			
			commands += globalRating + account;
			TGSender.send(commands);
			return false;
		});
		
		//NewReferee
		validators.put(1, (Answer answer) -> true);
		
		// GlobalRating
		validators.put(2, (Answer) ->
		{
			String rating = Main.sql.getGlobalRating();
			TGSender.send("`Баллы | ФИО судьи\n------+-------------------\n" + rating + "`");
			return false;
		});
		
		//NewCompetition
		validators.put(3, (Answer) -> true);
		
		//Register
		validators.put(4, (Answer) ->
		{
			if(Main.updateHandler.getActiveUser().femidaID == -1)
			{
				sendPhoneButton();
			}
			else
			{
				Referee referee = new Referee(Main.updateHandler.getActiveUser().femidaID);
				TGSender.send(referee.toNiceString());
			}
			
			
			return false;
		});
		
		//Phone validation
		validators.put(5, (Answer) ->
		{
			String phone = Main.updateHandler.getActiveUser().phoneNumber;
			//phone = new Referee(1).getPhone();
			
			phone = "8" + phone.substring(phone.length() - 10);
			
			TGSender.send("*Выполняем поиск по номеру:* " + phone);
			
			int id = Referee.findRefereeByPhone(phone);
			
			if(id == -1)
			{
				TGSender.send("❗️️️️Не удалось найти рефери с таким номером\nМожете отправить заявку на создание учетной записи /Register");
			}
			else
			{
				Referee referee = new Referee(id);
				TGSender.send(referee.toNiceString());
			}
			Main.updateHandler.getActiveUser().femidaID = id;
			return false;
		});
		
		validators.put(6, (Answer) ->
		{
			TGSender.send("❗️️️️Еще не доступно...");
			return false;
		});
		validators.put(7, (Answer) ->true);
	}
	
	private static void sendPhoneButton()
	{
		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(SafeUpdateParser.getChatID());
		sendMessage.setText("Разрешите доступ к номеру телефона для автоматической регистрации (Конпка снизу)");
		
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(true);
		
		List<KeyboardRow> keyboard = new ArrayList<>();
		
		KeyboardRow keyboardFirstRow = new KeyboardRow();
		KeyboardButton keyboardButton = new KeyboardButton();
		
		keyboardButton.setText("Отправить номер");
		keyboardButton.setRequestContact(true);
		keyboardFirstRow.add(keyboardButton);
		
		keyboard.add(keyboardFirstRow);
		replyKeyboardMarkup.setKeyboard(keyboard);
		
		TGSender.send(sendMessage);
	}
}
