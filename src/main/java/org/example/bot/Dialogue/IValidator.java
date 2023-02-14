package main.java.org.example.bot.Dialogue;

public interface IValidator // Подтверждает переход на следующую стадию (или не подтверждает)
{
	boolean validate(Validateable validateable);
}
