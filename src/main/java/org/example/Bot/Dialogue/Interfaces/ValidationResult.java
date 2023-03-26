package main.java.org.example.Bot.Dialogue.Interfaces;

public enum ValidationResult
{
	NEXT_STAGE, // Стандартный запрос переключения стадии
	REPEAT, // Повторный вход в эту же стадию
	FORCE_REPEAT, // Возвращение в главное меню
	NOT_FOUND // Не понятно, что хочет пользователь
}
