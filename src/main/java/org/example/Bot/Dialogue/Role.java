package main.java.org.example.Bot.Dialogue;

public class Role
{
	private boolean NEW_REFEREE;
	private boolean NEW_COMPETITION;
	private boolean PLAN_COMPETITION;
	private boolean VERIFY_ACCOUNT;
	
	public Role(Status status)
	{
		switch(status)
		{
			case ADMIN ->
			{
				NEW_REFEREE = true;
				NEW_COMPETITION = true;
				PLAN_COMPETITION = true;
				VERIFY_ACCOUNT = true;
			}
			case ACCOUNT_VERIFIER ->
			{
				NEW_REFEREE = false;
				NEW_COMPETITION = false;
				PLAN_COMPETITION = false;
				VERIFY_ACCOUNT = true;
			}
			case VISITOR ->
			{
				NEW_REFEREE = false;
				NEW_COMPETITION = false;
				PLAN_COMPETITION = false;
				VERIFY_ACCOUNT = false;
			}
		}
	}
	
	public boolean isNEW_REFEREE()
	{
		return NEW_REFEREE;
	}
	
	public boolean isNEW_COMPETITION()
	{
		return NEW_COMPETITION;
	}
	
	public boolean isPLAN_COMPETITION()
	{
		return PLAN_COMPETITION;
	}
	
	public boolean isVERIFY_ACCOUNT()
	{
		return VERIFY_ACCOUNT;
	}
}

enum Status
{
	ADMIN, ACCOUNT_VERIFIER, VISITOR
}
