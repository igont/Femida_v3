package main.java.org.example.DataBase;

import main.java.org.example.Bot.Excel.Templates.Competition;
import main.java.org.example.Bot.Excel.Templates.Referee;
import main.java.org.example.DataBase.Interfaces.Column;
import main.java.org.example.DataBase.Interfaces.DataBase;
import main.java.org.example.DataBase.Interfaces.Table;

import java.sql.*;
import java.util.*;

import static main.java.org.example.DataBase.Type.*;

public class SQL
{
	public DataBase mainDatabase;
	public static final String DB_URL = "jdbc:postgresql://localhost:5432/";
	public static final String USER = "igor_admin";
	public static final String PASSWORD = "123321321123";

	public SQL()
	{
		initDriver();

		mainDatabase = new DataBase("main");
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

		refereeColumns.add(new Column("id", SMALLSERIAL, null));
		refereeColumns.add(new Column("surname", TEXT, null));
		refereeColumns.add(new Column("name", TEXT, null));
		refereeColumns.add(new Column("patronymic", TEXT, null));
		refereeColumns.add(new Column("city", TEXT, null));
		refereeColumns.add(new Column("phone", TEXT, null));
		refereeColumns.add(new Column("calc_points", REAL, null));
		refereeColumns.add(new Column("category", TEXT, null));
		refereeColumns.add(new Column("birth_year", INTEGER, null));
		refereeColumns.add(new Column("club_type", TEXT, null));
		refereeColumns.add(new Column("club_name", TEXT, null));

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
						case "birth_year" -> referee.getBirth();
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
			column.setData(
					switch(column.name())
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

		competitionColumns.add(new Column("id", SMALLSERIAL, null));
		competitionColumns.add(new Column("title", TEXT, null));
		competitionColumns.add(new Column("place", TEXT, null));
		competitionColumns.add(new Column("date", DATE, null));
		competitionColumns.add(new Column("members", INTEGER_ARRAY, null));
		competitionColumns.add(new Column("carpets", TEXT_ARRAY, null));
		competitionColumns.add(new Column("grades", REAL_ARRAY, null));
		competitionColumns.add(new Column("positions", TEXT_ARRAY, null));

		Table table = new Table("competitions");
		mainDatabase.addTable(table);
		table.init(competitionColumns);
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
