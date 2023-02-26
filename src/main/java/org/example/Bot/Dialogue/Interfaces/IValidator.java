package main.java.org.example.Bot.Dialogue.Interfaces;

import main.java.org.example.Bot.Dialogue.Answer;

public interface IValidator // Подтверждает переход на следующую стадию (или не подтверждает)
{
	Boolean validate(Answer answer);
}
