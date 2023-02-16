package main.java.org.example.bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Main;
import main.java.org.example.bot.Dialogue.IStage;
import main.java.org.example.bot.Dialogue.Validateable;
import main.java.org.example.bot.TG.TGSender;
import main.java.org.example.bot.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

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

				➕Зарегистрировать нового судью:
				/NewReferee

				➕Создать новое соревнование:
				/NewCompetition

				📃Вывести рейтинг всех судей:
				/GlobalRating
								
				⬇️Войти в систему
				/Register""";

		TGSender.send(text);
	}

	@Override
	public int preValidation(Update update)
	{
		if(update.hasMessage())
		{
			if(update.getMessage().hasText())
			{
				if(update.getMessage().getText().equals("/NewReferee")) return 1;
				if(update.getMessage().getText().equals("/GlobalRating")) return 2;
				if(update.getMessage().getText().equals("/NewCompetition")) return 3;
				if(update.getMessage().getText().equals("/Register")) return 4;
			}
			if(update.getMessage().hasContact())
			{
				Main.updateHandler.activeUser.phoneNumber = update.getMessage().getContact().getPhoneNumber();
				System.out.println(Main.updateHandler.activeUser.phoneNumber);
				return 5;
			}
		}
		return stageNum;
	}

	@Override
	public void addValidators()
	{
		validators.put(1, (Validateable validateable) ->
		{
			TGSender.send("Еще не доступно...");
			return false;
		});
		validators.put(2, (Validateable) ->
		{
			TGSender.send("Еще не доступно...");
			return false;
		});
		validators.put(3, (Validateable) ->
		{
			TGSender.send("Еще не доступно...");
			return false;
		});
		validators.put(4, (Validateable) ->
		{
			SendMessage sendMessage = new SendMessage();
			sendMessage.setChatId(User.getChatID());
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
		validators.put(5, (Validateable) ->
		{
			TGSender.send("Выполняем поиск по номеру: " + Main.updateHandler.activeUser.phoneNumber);
			return false;
		});
	}
}
