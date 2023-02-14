package main.java.org.example.bot.Dialogue.MainDialogueMenu;

import main.java.org.example.Main;
import main.java.org.example.bot.Dialogue.IStage;
import main.java.org.example.bot.Dialogue.Validateable;
import main.java.org.example.bot.TG.TGSender;
import main.java.org.example.bot.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class StartStage extends IStage // Стадия приветствия
{
	public StartStage(List<IStage> list)
	{
		init(list.size());
	}
	@Override
	public void action()
	{
		//TGSender.send("""
		//		Добро пожаловать в систему учета рейтинга спортивных судей "FEMIDA".
		//
		//		С помощью бота вы сможете выполнять следующие действия:
		//
		//		-Зарегистрировать нового судью:
		//		/NewReferee
		//
		//		-Вывести рейтинг всех судей:
		//		/GlobalRating
		//
		//		-Создать новое соревнование:
		//		/NewCompetition""");

		SendMessage sendMessage = new SendMessage();
		sendMessage.setChatId(User.getChatID());
		sendMessage.setText("Номер телефона необходим для автоматической авторизации в системе");

		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		replyKeyboardMarkup.setSelective(true);
		replyKeyboardMarkup.setResizeKeyboard(true);
		replyKeyboardMarkup.setOneTimeKeyboard(true);
		replyKeyboardMarkup.setSelective(true);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow keyboardFirstRow = new KeyboardRow();
		KeyboardButton keyboardButton = new KeyboardButton();

		keyboardButton.setText("Share your number >");
		keyboardButton.setRequestContact(true);
		keyboardFirstRow.add(keyboardButton);

		keyboard.add(keyboardFirstRow);
		replyKeyboardMarkup.setKeyboard(keyboard);

		TGSender.send(sendMessage);
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
			}
			if(update.getMessage().hasContact())
			{
				Main.updateHandler.activeUser.phoneNumber = update.getMessage().getContact().getPhoneNumber();
				System.out.println(Main.updateHandler.activeUser.phoneNumber);
				return stageNum;
			}
		}
		return stageNum;
	}

	@Override
	public void addValidators()
	{
		validators.put(1,(Validateable validateable) ->
		{
			System.out.println(validateable.update.getMessage().getContact());
			return false;
		});
		validators.put(2,(Validateable) -> true);
		validators.put(3,(Validateable) -> true);
	}
}
