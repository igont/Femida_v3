package org.example.Bot.Dialogue.Interfaces;

public enum ValidationResult
{
	NEXT_STAGE, // Стандартный запрос переключения стадии
	REPEAT, // Повторный вход в эту же стадию
	NOT_FOUND // Не понятно, что хочет пользователь
}
