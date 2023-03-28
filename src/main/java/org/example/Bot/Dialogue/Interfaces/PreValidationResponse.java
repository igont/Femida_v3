package main.java.org.example.Bot.Dialogue.Interfaces;

public class PreValidationResponse
{
	private ValidationResult validationResult;
	private String nextStage;

	public PreValidationResponse(ValidationResult validationResult, String nextStage)
	{
		this.validationResult = validationResult;
		this.nextStage = nextStage;
	}

	public ValidationResult getValidationResult()
	{
		return validationResult;
	}

	public String getNextStage()
	{
		return nextStage;
	}
}
