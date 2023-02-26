package main.java.org.example.Bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Bot.Dialogue.Interfaces.PreValidationResponse;
import main.java.org.example.Main;
import main.java.org.example.Bot.Dialogue.Answer;
import main.java.org.example.Bot.Dialogue.IStage;
import main.java.org.example.Bot.TG.SafeUpdateParser;
import main.java.org.example.Bot.TG.TGSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

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

		if(answer.hasPhone())
		{
			Main.updateHandler.activeUser.phoneNumber = answer.getPhone();
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
				/Register""";

			TGSender.send(text);
			return false;
		});

		//NewReferee
		validators.put(1, (Answer answer) -> true);

		// GlobalRating
		validators.put(2, (Answer) ->
		{
			TGSender.send("Еще не доступно...");
			return false;
		});

		//NewCompetition
		validators.put(3, (Answer) -> true);

		//Register
		validators.put(4, (Answer) ->
		{
			SendMessage sendMessage = new SendMessage();
			sendMessage.setChatId(SafeUpdateParser.getChatID());
			sendMessage.setText("Разрешите доступ к номеру телефона для автоматической регистрации");

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
			TGSender.send("Выполняем поиск по номеру: " + Main.updateHandler.activeUser.phoneNumber);
			return false;
		});

	}
}
