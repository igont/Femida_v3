package org.example.Bot;

import org.example.Bot.Dialogue.MainDialogueMenu.MainDialogue;
import org.example.Bot.Dialogue.IDialogue;
import org.example.Bot.Dialogue.Role;
import org.example.Bot.TG.SafeUpdateParser;
import org.example.DataBase.SQL;
import org.example.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class User
{
	private String name;
	private int femidaID;
	
	private IDialogue dialogue;
	private String phoneNumber;
	
	public User()
	{
		this.name = "???";
		femidaID = -1;
		phoneNumber = "";
		dialogue = new MainDialogue();
	}
	
	public User(String name)
	{
		this();
		this.name = name;
		
		Connection connection = Main.sql.mainDatabase.connection;
		ResultSet resultSet;
		
		PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("select * from users where name = ?;");
			preparedStatement.setString(1, name);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
			{
				System.out.printf("❗User not found name: [%s]\n", name);
			}
			else if(resultSet.next())
			{
				femidaID = resultSet.getInt("femida_id");
				phoneNumber = resultSet.getString("phone");
				
				System.out.printf("✅User found name: [%s]\n", name);
			}
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void save()
	{
		try
		{
			String s = String.format("Delete from users where name = '%s'", name);
			SQL.execute(s, Main.sql.mainDatabase.statement);
			Main.sql.addUser(this);
			System.out.println("saved");
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	
	public String getName()
	{
		return name;
	}
	
	public int getFemidaID()
	{
		return femidaID;
	}
	
	public IDialogue getDialogue()
	{
		return dialogue;
	}
	
	public void setFemidaID(int femidaID)
	{
		this.femidaID = femidaID;
		SafeUpdateParser.getActiveUser().save();
	}
	
	public void setDialogue(IDialogue dialogue)
	{
		this.dialogue = dialogue;
	}
	
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
		SafeUpdateParser.getActiveUser().save();
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getPhoneNumber()
	{
		return phoneNumber;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return femidaID == user.femidaID && Objects.equals(name, user.name) && Objects.equals(dialogue, user.dialogue) && Objects.equals(phoneNumber, user.phoneNumber);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(name, femidaID, dialogue, phoneNumber);
	}
}
