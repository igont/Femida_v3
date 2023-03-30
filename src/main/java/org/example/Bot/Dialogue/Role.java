package org.example.Bot.Dialogue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Role
{
	private final Map<Possibility, Boolean> possibility;
	
	public Role(String s)
	{
		this();
		try
		{
			s = s.substring(0, s.length() - 1);
			String[] trueRoles = s.split("\\|");
			Arrays.stream(trueRoles).forEach(role -> possibility.put(Possibility.valueOf(Possibility.class, role), true));
		}
		catch(StringIndexOutOfBoundsException ignored)
		{
		
		}
	}
	
	public Role()
	{
		possibility = new HashMap<>();
		Arrays.stream(Possibility.values()).forEach(p -> possibility.put(p, false));
	}
	
	public void changeRole(Possibility possibility, boolean flag)
	{
		this.possibility.put(possibility, flag);
	}
	
	public boolean getPossibility(Possibility possibility)
	{
		return this.possibility.get(possibility);
	}
	
	@Override
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		
		possibility.entrySet().stream().filter(Map.Entry::getValue).forEach(p -> s.append(p.getKey()).append("|"));
		return s.toString();
	}
	
	public static Role getRandomRole()
	{
		Random random = new Random();
		Role role = new Role();
		
		Arrays.stream(Possibility.values()).forEach(p -> role.possibility.put(p, random.nextBoolean()));
		
		return role;
	}
	
	public Map<Possibility, Boolean> getCopyOfPossibilities()
	{
		return new HashMap<>(possibility);
	}
}

