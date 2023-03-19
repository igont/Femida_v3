package main.java.org.example.Bot.TG;

import main.java.org.example.Bot.User;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpdateHandler
{
	/*
			Класс получает update от бота, распознает пользователя, от которого пришел запрос и сохраняет в общую базу пользователей.
			В общей базе хранятся аккаунты пользователей, которые содержат ключевую информацию, отличающую каждого пользователя
			от другого. Благодаря этим различиям, бот может определить, с кем он ведет диалог и что ожидать от каждого пользователя.
	 */
	private List<User> users;

	public User activeUser;

	public UpdateHandler()
	{
		users = new ArrayList<>();
	}
	public void authoriseUser(Update update) // Поиск пользователя в общей базе
	{
		String fromName = "";
		if(update.hasMessage()) fromName = update.getMessage().getFrom().getUserName();
		else if(update.hasCallbackQuery()) fromName = update.getCallbackQuery().getFrom().getUserName();

		User newUser = new User(fromName);

		boolean isFind = false;
		for(User user : users)
		{
			if(Objects.equals(user.name, newUser.name))
			{
				activeUser = user;
				isFind = true;
			}
		}

		if(!isFind)
		{
			activeUser = newUser;
			users.add(newUser);
		}
	}
}
