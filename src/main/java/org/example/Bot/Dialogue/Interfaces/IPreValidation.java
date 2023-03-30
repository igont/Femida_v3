package org.example.Bot.Dialogue.Interfaces;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface IPreValidation // Определяет номер следующей стадии
{
	int preValidate(Update update);
}
