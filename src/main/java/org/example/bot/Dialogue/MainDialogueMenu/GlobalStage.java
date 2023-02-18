package main.java.org.example.bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Main;
import main.java.org.example.bot.Dialogue.Answer;
import main.java.org.example.bot.Dialogue.IStage;
import main.java.org.example.bot.SafeUpdateParser;
import main.java.org.example.bot.TG.TGSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GlobalStage extends IStage // Стадия приветствия
{
	public GlobalStage(List<IStage> list)
	{
		init(list.size());
	}

	@Override
	public void action()
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
	}

	@Override
	public int preValidation(Answer answer)
	{
		if(Objects.equals(answer.getMessage(), "/NewReferee")) return 1;
		if(Objects.equals(answer.getMessage(), "/GlobalRating")) return 2;
		if(Objects.equals(answer.getMessage(), "/NewCompetition")) return 3;
		if(Objects.equals(answer.getMessage(), "/Register")) return 4;

		if(answer.hasPhone())
		{
			Main.updateHandler.activeUser.phoneNumber = answer.getPhone();
			return 5;
		}

		return stageNum;
	}

	@Override
	public void addValidators()
	{
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
