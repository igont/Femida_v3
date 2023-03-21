package main.java.org.example.Bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Bot.Dialogue.Interfaces.PreValidationResponse;
import main.java.org.example.Bot.Excel.Templates.Referee;
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
		if(Objects.equals(answer.getMessage(), "/start")) return new PreValidationResponse(FORCE_REPEAT, 0);
		if(Objects.equals(answer.getMessage(), "/NewReferee")) return new PreValidationResponse(NEXT_STAGE, 1);
		if(Objects.equals(answer.getMessage(), "/GlobalRating")) return new PreValidationResponse(NEXT_STAGE, 2);
		if(Objects.equals(answer.getMessage(), "/NewCompetition")) return new PreValidationResponse(NEXT_STAGE, 3);
		if(Objects.equals(answer.getMessage(), "/Register")) return new PreValidationResponse(NEXT_STAGE, 4);
		if(Objects.equals(answer.getMessage(), "/PlanCompetition")) return new PreValidationResponse(NEXT_STAGE, 6);
		if(Objects.equals(answer.getMessage(), "/Account")) return new PreValidationResponse(NEXT_STAGE, 7);
		
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
			String text = """
					Добро пожаловать в систему учета рейтинга спортивных судей "FEMIDA".
					
					С помощью бота вы сможете выполнять следующие действия:
					
					➕*Зарегистрировать нового судью:*
					/NewReferee
					
					➕*Создать новое соревнование:*
					/NewCompetition
					
					📃*Вывести рейтинг всех судей:*
					/GlobalRating
									
					⬇️*Войти в систему:*
					/Register
					
					🕐*Запланировать соревнование*
					/PlanCompetition
					
					😐*Мой аккаунт*
					/Account
					""";
			
			TGSender.send(text);
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
			return false;
		});
		
		//Phone validation
		validators.put(5, (Answer) ->
		{
			String phone = Main.updateHandler.getActiveUser().phoneNumber;
			
			phone = "8" + phone.substring(phone.length() - 10);
			
			TGSender.send("*Выполняем поиск по номеру:* " + phone);
			
			int id = Referee.findRefereeByPhone(phone);
			
			if(id == -1)
			{
				TGSender.send("❗️️️️Не удалось найти рефери с таким номером");
			}
			else
			{
				Referee referee = new Referee(id);
				TGSender.send(referee.toNiceString());
			}
			return false;
		});
		
		validators.put(6, (Answer) ->
		{
			TGSender.send("❗️️️️Еще не доступно...");
			return false;
		});
		
		validators.put(7, (Answer) ->
		{
			TGSender.send("❗️️️️Еще не доступно...");
			return false;
		});
		
	}
}
