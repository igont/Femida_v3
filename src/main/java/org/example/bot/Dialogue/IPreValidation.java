package main.java.org.example.bot.Dialogue;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface IPreValidation // Определяет номер следующей стадии
{
	int preValidate(Update update);
}
