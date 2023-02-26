package main.java.org.example.Bot.Dialogue.Interfaces;

public class PreValidationResponse
{
	private ValidationResult validationResult;
	private int nextStage;

	public PreValidationResponse(ValidationResult validationResult, int nextStage)
	{
		this.validationResult = validationResult;
		this.nextStage = nextStage;
	}

	public ValidationResult getValidationResult()
	{
		return validationResult;
	}

	public int getNextStage()
	{
		return nextStage;
	}
}
