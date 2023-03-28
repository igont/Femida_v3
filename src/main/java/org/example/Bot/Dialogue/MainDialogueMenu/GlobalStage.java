package main.java.org.example.Bot.Dialogue.MainDialogueMenu;

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
import java.util.Map;
import java.util.Objects;

import static main.java.org.example.Bot.Dialogue.Interfaces.ValidationResult.*;

public class GlobalStage extends IStage // Стадия приветствия
{
	public GlobalStage(String stageName)
	{
		super(stageName);
	}
	
	@Override
	public void action()
	{
	}
	
	@Override
	public String preValidation(Answer answer)
	{
		String messageText = answer.getMessage();
		String nextStageName = "";
		
		if(answer.hasMessage())
		{
			nextStageName = switch(messageText)
					{
						case "/commands", "/start" -> "send commands";
						case "/NewReferee" -> "new referee";
						case "/GlobalRating" -> "global rating";
						case "/NewCompetition" -> "new competition";
						case "/Account" -> "account";
						case "/PlanCompetition" -> "plan competition";
						case "/Register" -> "register";
						case "/About" -> "about";
						default -> "";
					};
		}
		
		if(answer.hasPhone())
		{
			Main.updateHandler.getActiveUser().phoneNumber = answer.getPhone();
			nextStageName = "check phone";
		}
		
		if(nextStageName.equals("")) return null;
		else return nextStageName;
	}
	
	@Override
	public void addValidators()
	{
		validators.put("new referee", (answer) -> true);
		validators.put("new competition", (answer) -> true);
		validators.put("register", (answer) -> true);
		
		validators.put("send commands", (answer) ->
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
			}
			else
			{
				head = referee.getFIO() + ", Вам доступны следующие команды:\n\n";
			}
			
			String about = """
					*ℹ️Информация о боте:*
					/About
					
					""";
			
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
					🙂*Мой аккаунт*
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
			
			commands += about + globalRating + account;
			TGSender.send(commands);
			return false;
		});
		
		validators.put("global rating", (answer) ->
		{
			String rating = Main.sql.getGlobalRating();
			TGSender.send("`Баллы | ФИО судьи\n------+-------------------\n" + rating + "`");
			return false;
		});
		
		validators.put("account", (answer) ->
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
		
		validators.put("check phone", (answer) ->
		{
			String phone = Main.updateHandler.getActiveUser().phoneNumber;
			//phone = new Referee(1).getPhone();
			
			phone = "8" + phone.substring(phone.length() - 10);
			
			TGSender.send("*Выполняем поиск по номеру:* " + phone);
			
			int id = Referee.findRefereeByPhone(phone);
			
			if(id == -1)
			{
				TGSender.send("❗️️️️Не удалось найти рефери с таким номером.\n\nЛибо вашей учетной записи не существует, тогда создайте ее: /Register\n\nЛибо она привязана на другой ваш номер телефона, тогда /Support\n\n Если вы не уверены, существует ли ваш аккаутн, найдите себя в поиске: /Search");
			}
			else
			{
				Referee referee = new Referee(id);
				TGSender.send(referee.toNiceString());
			}
			Main.updateHandler.getActiveUser().femidaID = id;
			return false;
		});
		
		validators.put("plan competition", (answer) ->
		{
			TGSender.send("❗️️️️Еще не доступно...");
			return false;
		});
		
		validators.put("about", (answer) ->
		{
			String s = """
					*Возможности FEMIDA:*
					Бот предназначен для ведения аккаунтов спортивных судей и организации мероприятий.
					
					*Для рядовых пользователей:*
					🔸 Ведение рейтинга и персонального опыта судьи для. Поможет в выборе при составлении судейской бригады руководителем. Чем более подходящая характеристика судьи, тем больше вероятность быть приглашенным.
					
					🔸 Доступ к архивным результатам прошедших соревнований, их можно получить с помощью поиска по критериям
					
					🔸 Доступ к всеобщему рейтингу судей в спортивной дисцеплине. Список судей сортируется по количестку баллов, набранному за всё время. Существуют разные виды сортировок
					
					🔸 Доступ ко всем функциям бота со всех типов устройств. Мобильные телефоны, персональные ПК, ноутбуки...
					
					
					*Для руководителй и организаторов:*
					🔹 Планирование будущих соревнований. Для согласования графиков и предварительного набора судейской бригады
					
					🔹 Регистрация судей на будущие соревнования. Для построения общей ситуации с судейской бригады на будущем соревновании
					
					🔹 Быстрый способ найти состав судейской бригады через рассылку по критериям. Отправка персональных приглашений на конкретные должности
					
					🔹 Автоматизированный поиск судей для бригады по критериям. Подходящие судьи будут автоматически уведомлены о планируемом мероприятии
					
					🔹 Заполнение отчетов о проведенных соревнований в доступной форме. Нет необходимости работать через персональный компьютер
					
					
					*Для администраторов*
					🔸 Интуитивно понятная система ввода данных. Структурированная таблица Excel, с помощью которых осуществляется ввод новых соревнований.
					
					🔸 Отказоустойчивая система. При ошибке в информации не произойдет ничего критического.
					
					🔸 Перед занесением информации в общую базу - она проходит дополнительну проверку. Лишь после одобрения людей, назначенных на должность проверяющих - информация попадает в базу
					""";
			TGSender.send(s);
			return false;
		});
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
