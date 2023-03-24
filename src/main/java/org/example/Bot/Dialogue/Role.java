package main.java.org.example.Bot.Dialogue;

import java.util.HashMap;
import java.util.Map;

public class Role
{
	private final Map<Possibility, Boolean> roles = new HashMap<>();
	
	public Role()
	{
		roles.put(Possibility.NEW_COMPETITION, false);
		roles.put(Possibility.NEW_REFEREE, false);
		roles.put(Possibility.VERIFY_ACCOUNT, false);
		roles.put(Possibility.PLAN_COMPETITION, false);
	}
	
	public void changeRole(Possibility possibility, boolean flag)
	{
		roles.put(possibility, flag);
	}
	
	public boolean getPossibility(Possibility possibility)
	{
		return roles.get(possibility);
	}
	
	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		roles.values().forEach(b -> s.append(b).append("|"));
		return s.toString();
	}
}

