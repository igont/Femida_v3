package org.example.Bot.Dialogue.MainDialogueMenu;

import org.example.Bot.Dialogue.Answer;
import org.example.Bot.Dialogue.IStage;
import org.example.Bot.TG.TGSender;
import org.example.Main;

public class AdminPanelStage extends IStage
{
	public AdminPanelStage(String name)
	{
		super(name);
	}
	
	@Override
	public void action()
	{
		String s = """
				*🐘DataBase*
				/DropReferee
				/DropCompetitions
				/DropUsers
				
				/AddRandomReferee
				/AddRandomCompetitions
				
				/CreateBackup
				/LoadBackup
				
				
				*🤖Bot*
				/ChangeRole
				""";
		TGSender.send(s);
	}
	
	@Override
	public String preValidation(Answer answer)
	{
		String messageText = answer.getMessage();
		String nextStageName = "";
		
		if(answer.hasMessage())
		{
			nextStageName = switch(messageText)
			{
				case "/DropReferee" -> "drop referee";
				case "/DropCompetitions" -> "drop competitions";
				case "/DropUsers" -> "drop users";
				case "/AddRandomReferee" -> "coming soon";
				case "/AddRandomCompetitions" -> "add random data";
				case "/CreateBackup" -> "coming soon";
				case "/LoadBackup" -> "coming soon";
				case "/ChangeRole" -> "coming soon";
				default -> "";
			};
		}
		
		if(nextStageName.equals("")) return null;
		else return nextStageName;
	}
	
	@Override
	public void addValidators()
	{
		validators.put("coming soon", (answer) ->
		{
			TGSender.send("❗️️️️Еще не доступно...");
			return false;
		});
		
		validators.put("drop referee", (answer) ->
		{
			boolean drop = Main.sql.mainDatabase.getTable("referee").drop();
			if(drop)
			{
				TGSender.send("✅ Drop referee success");
			}
			else
			{
				TGSender.send("❗Drop referee failure");
			}
			return false;
		});
		
		validators.put("drop competitions", (answer) ->
		{
			boolean drop = Main.sql.mainDatabase.getTable("competitions").drop();
			if(drop)
			{
				TGSender.send("✅ Drop competitions success");
			}
			else
			{
				TGSender.send("❗Drop competitions failure");
			}
			return false;
		});
		
		validators.put("drop users", (answer) ->
		{
			boolean drop = Main.sql.mainDatabase.getTable("users").drop();
			if(drop)
			{
				TGSender.send("✅ Drop users success");
			}
			else
			{
				TGSender.send("❗Drop users failure");
			}
			return false;
		});
		
		validators.put("add random data", (answer) ->
		{
			Main.sql.addRandomlyData();
			return false;
		});
	}
}
