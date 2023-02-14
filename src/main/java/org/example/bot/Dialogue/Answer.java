package main.java.org.example.bot.Dialogue;

public class Answer
{
	public Answer()
	{
		validateable = new Validateable();
	}
	public int nextStage;
	public Validateable validateable;

	@Override
	public String toString()
	{
		return "Answer{" + "nextStage=" + nextStage + ", validateable=" + validateable.toString() + '}';
	}
}
