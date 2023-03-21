package main.java.org.example.DataBase;

import main.java.org.example.Bot.Excel.Templates.Competition;
import main.java.org.example.Bot.Excel.Templates.Referee;
import main.java.org.example.DataBase.Interfaces.Column;
import main.java.org.example.DataBase.Interfaces.DataBase;
import main.java.org.example.DataBase.Interfaces.Table;
import main.java.org.example.Main;
import org.apache.poi.util.StringUtil;

import java.sql.*;
import java.util.*;

import static main.java.org.example.DataBase.Type.*;

public class SQL
{
	public DataBase mainDatabase;
	public static final String DB_URL = "jdbc:postgresql://localhost:5432/";
	public static final String USER = "postgres";
	public static final String PASSWORD = "postgres";
	
	public SQL()
	{
		initDriver();
		
		mainDatabase = new DataBase("postgres");
		createRefereeTable();
		createCompetitionTable();
	}
	
	private static void initDriver()
	{
		System.out.println("Initialising PostgreSQL...");
		try
		{
			Class.forName("org.postgresql.Driver");
		}
		catch(ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private void createRefereeTable()
	{
		List<Column> refereeColumns = new ArrayList<>();
		
		refereeColumns.add(new Column("id", SMALLSERIAL));
		refereeColumns.add(new Column("surname", TEXT));
		refereeColumns.add(new Column("name", TEXT));
		refereeColumns.add(new Column("patronymic", TEXT));
		refereeColumns.add(new Column("city", TEXT));
		refereeColumns.add(new Column("phone", TEXT));
		refereeColumns.add(new Column("calc_points", REAL));
		refereeColumns.add(new Column("category", TEXT));
		refereeColumns.add(new Column("birth", DATE));
		refereeColumns.add(new Column("club_type", TEXT));
		refereeColumns.add(new Column("club_name", TEXT));
		
		Table table = new Table("referee");
		mainDatabase.addTable(table);
		table.init(refereeColumns);
	}
	
	public void addReferee(Referee referee)
	{
		List<Column> columns = mainDatabase.getTable("referee").getColumns();
		
		for(Column column : columns)
		{
			column.setData(switch(column.name())
					{
						case "surname" -> referee.getSurname();
						case "name" -> referee.getName();
						case "patronymic" -> referee.getPatronymic();
						case "city" -> referee.getCity();
						case "phone" -> referee.getPhone();
						case "category" -> referee.getCategory();
						case "birth" -> referee.getBirth();
						case "club_type" -> referee.getClubType();
						case "club_name" -> referee.getClubName();
						default -> null;
					});
		}
		
		mainDatabase.getTable("referee").addLine(columns);
	}
	
	public void addCompetition(Competition competition)
	{
		List<Column> columns = mainDatabase.getTable("competitions").getColumns();
		
		for(Column column : columns)
		{
			column.setData(switch(column.name())
					{
						case "title" -> competition.title();
						case "place" -> competition.place();
						case "date" -> competition.date();
						case "members" -> competition.members();
						case "carpets" -> competition.carpets();
						case "grades" -> competition.grades();
						case "positions" -> competition.positions();
						default -> null;
					});
		}
		
		mainDatabase.getTable("competitions").addLine(columns);
	}
	
	private void createCompetitionTable()
	{
		List<Column> competitionColumns = new ArrayList<>();
		
		competitionColumns.add(new Column("id", SMALLSERIAL));
		competitionColumns.add(new Column("title", TEXT));
		competitionColumns.add(new Column("place", TEXT));
		competitionColumns.add(new Column("date", DATE));
		competitionColumns.add(new Column("members", INTEGER_ARRAY));
		competitionColumns.add(new Column("carpets", TEXT_ARRAY));
		competitionColumns.add(new Column("grades", REAL_ARRAY));
		competitionColumns.add(new Column("positions", TEXT_ARRAY));
		
		Table table = new Table("competitions");
		mainDatabase.addTable(table);
		table.init(competitionColumns);
	}
	
	public String getGlobalRating()
	{
		ResultSet resultSet;
		String result = "";
		try
		{
			
			resultSet = Main.sql.mainDatabase.statement.executeQuery("Select * from referee order by calc_points desc;");
			
			while(true)
			{
				try
				{
					if(!resultSet.next()) break;
				}
				catch(SQLException e)
				{
					throw new RuntimeException(e);
				}
				String points = "?";
				String surname = "?";
				String name = "?";
				String patronymic = "?";
				
				points = resultSet.getInt("calc_points") + "";
				surname = resultSet.getString("surname");
				
				try
				{
					name = resultSet.getString("name").toUpperCase().charAt(0) + "";
					
				}
				catch(StringIndexOutOfBoundsException ignored)
				{
				}
				try
				{
					patronymic = resultSet.getString("patronymic").toUpperCase().charAt(0) + "";
					
				}
				catch(StringIndexOutOfBoundsException ignored)
				{
				}
				
				int spaces = 4 - points.length();
				
				String str = StringUtil.repeat((' '), spaces);
				
				String add = String.format("  %s| %s %s. %s.\n", points + str, surname, name, patronymic);
				
				result += add;
			}
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public static List<Referee> getAllReferees()
	{
		List<Referee> referees = new ArrayList<>();
		ResultSet resultSet;
		try
		{
			resultSet = Main.sql.mainDatabase.statement.executeQuery("Select id from referee order by calc_points desc;");
			while(resultSet.next())
			{
				referees.add(new Referee(resultSet.getInt("id")));
			}
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
		return referees;
	}
	
	public static void execute(PreparedStatement preparedStatement)
	{
		try
		{
			preparedStatement.execute();
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static boolean execute(String str, Statement statement) throws SQLException // Отправляем запрос, который ничего не возвращает
	{
		return statement.execute(str);
	}
	
	public static ResultSet executeQuery(PreparedStatement preparedStatement)
	{
		try
		{
			return preparedStatement.executeQuery();
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static ResultSet executeQuery(String str, Statement statement) throws SQLException // Отправляем запрос, который что-то возвращает
	{
		return statement.executeQuery(str);
	}
}
