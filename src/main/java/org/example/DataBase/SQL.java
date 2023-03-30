package org.example.DataBase;

import org.apache.poi.ss.formula.functions.T;
import org.example.Bot.Excel.Templates.Competition;
import org.example.Bot.Excel.Templates.Referee;
import org.example.Bot.User;
import org.example.DataBase.Interfaces.Column;
import org.example.DataBase.Interfaces.DataBase;
import org.example.DataBase.Interfaces.Table;
import org.apache.poi.util.StringUtil;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Stream;

import static org.example.DataBase.Type.*;

public class SQL
{
	public DataBase mainDatabase;
	public static final String DB_URL = "jdbc:postgresql://localhost:5432/";
	public static final String USER = "postgres";
	public static final String PASSWORD = "postgres";
	private final Random random = new Random(123123123L);
	
	
	public SQL()
	{
		System.out.println();
		System.out.println("Launching PostgreSQL...");
		initDriver();
		
		mainDatabase = new DataBase("postgres");
		
		createRefereeTable();
		createCompetitionTable();
		createUserTable();
	}
	
	private static void initDriver()
	{
		try
		{
			Class.forName("org.postgresql.Driver");
		}
		catch(ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void addRandomlyData()
	{
		mainDatabase.getTable("referee").drop();
		mainDatabase.getTable("competitions").drop();
		
		Stream.generate(Referee::getRandomReferee).limit(10).forEach(this::addReferee);
		Stream.generate(Competition::getRandomCompetition).limit(10).forEach(this::addCompetition);
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
		refereeColumns.add(new Column("role", TEXT));
		
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
				case "role" -> referee.getRole().toString();
				default -> null;
			});
		}
		
		mainDatabase.getTable("referee").addLine(columns);
	}
	
	public void createUserTable()
	{
		List<Column> userColumns = new ArrayList<>();
		
		userColumns.add(new Column("name",TEXT));
		userColumns.add(new Column("femida_id",INTEGER));
		userColumns.add(new Column("phone", TEXT));
		
		Table table = new Table("users");
		mainDatabase.addTable(table);
		table.init(userColumns);
	}
	
	public  void addUser(User user)
	{
		List<Column> columns = mainDatabase.getTable("users").getColumns();
		for(Column column : columns)
		{
			column.setData(switch(column.name())
			{
				case "name" -> user.getName();
				case "femida_id" -> user.getFemidaID();
				case "phone" -> user.getPhoneNumber();
				default -> null;
			});
		}
		mainDatabase.getTable("users").addLine(columns);
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
	
	public void addCompetition(Competition competition)
	{
		List<Column> columns = mainDatabase.getTable("competitions").getColumns();
		
		for(Column column : columns)
		{
			column.setData(switch(column.name())
					{
						case "title" -> competition.getTitle();
						case "place" -> competition.getPlace();
						case "date" -> competition.getDate();
						case "members" -> competition.getMembers();
						case "carpets" -> competition.getCarpets();
						case "grades" -> competition.getGrades();
						case "positions" -> competition.getPositions();
						default -> null;
					});
		}
		
		mainDatabase.getTable("competitions").addLine(columns);
		reCountAllGrades();
	}
	
	public String getGlobalRating()
	{
		reCountAllGrades();
		ResultSet resultSet;
		StringBuilder result = new StringBuilder();
		
		int counter = 0;
		String lastPoints = "0";
		
		try
		{
			resultSet = mainDatabase.statement.executeQuery("Select * from referee order by calc_points desc;");
			
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
				
				String points = resultSet.getInt("calc_points") + "";
				
				String surname = resultSet.getString("surname");
				
				if(!lastPoints.equals(points)) counter++; // Одинаковый рейтинг получает одинаковое место
				lastPoints = points;
				
				switch(counter)
				{
					case 1 -> surname = "🥇" + surname;
					case 2 -> surname = "🥈" + surname;
					case 3 -> surname = "🥉" + surname;
					default -> surname = "  " + surname;
				}
				
				String name = getFirstLetter(resultSet.getString("name").toUpperCase());
				String patronymic = getFirstLetter(resultSet.getString("patronymic").toUpperCase());
				
				int spaces = 4 - points.length();
				
				String str = StringUtil.repeat((' '), spaces);
				
				String add = String.format("  %s|%s %s. %s.\n", points + str, surname, name, patronymic);
				
				result.append(add);
			}
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
		return result.toString();
	}
	
	private String getFirstLetter(String s)
	{
		try
		{
			return s.charAt(0) + "";
		}
		catch(StringIndexOutOfBoundsException ignored)
		{
		
		}
		return "";
	}
	
	public List<Referee> getAllReferees()
	{
		List<Referee> referees = new ArrayList<>();
		ResultSet resultSet;
		try
		{
			resultSet = mainDatabase.statement.executeQuery("Select id from referee order by calc_points desc;");
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
	
	public void reCountAllGrades()
	{
		String s = "select count(*) from referee";
		try
		{
			ResultSet resultSet = mainDatabase.statement.executeQuery(s);
			resultSet.next();
			
			int refereeAmount = resultSet.getInt("count");
			
			Deque<Float> countedGrades = new ArrayDeque<>(refereeAmount);
			
			while(refereeAmount > 0)
			{
				countedGrades.addFirst(getTotalGradeByID(refereeAmount));
				refereeAmount--;
			}
			
			changeCountedGrades(countedGrades);
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private void changeCountedGrades(Deque<Float> countedGrades) throws SQLException
	{
		int counter = 1;
		while(!countedGrades.isEmpty())
		{
			Float points = countedGrades.removeFirst();
			System.out.printf("id = %s  FIO = %s points = %s\n", counter, new Referee(counter).getFIO(), points);
			
			changeCountedGrade(counter, points);
			counter++;
		}
	}
	
	private void changeCountedGrade(int id, float totalGrade) throws SQLException
	{
		String s = "update referee set calc_points = " + totalGrade + " where id = " + id;
		mainDatabase.statement.execute(s);
	}
	
	public float getTotalGradeByID(int id)
	{
		
		float all = 0;
		
		all += getAll(id, 1); // Получаем оценку судьи за его первую должность в соревновании
		all += getAll(id, 2); // Если он был сразу на нескольких должностях - то уже будет массив оценок. перебираем все варианты
		all += getAll(id, 3);
		all += getAll(id, 4);
		all += getAll(id, 5); // 5 Должностей для человека - уже перебор, но всё может быть...
		
		return all;
	}
	
	private float getAll(int id, int pos)
	{
		ResultSet resultSet;
		int all = 0;
		
		try
		{
			String s = "select sum(grades[num[" + pos + "]]) from(select grades, array_positions(members," + id + ") as num from competitions) as uns;";
			resultSet = mainDatabase.statement.executeQuery(s);
			
			while(resultSet.next())
			{
				all += resultSet.getDouble("sum");
			}
		}
		catch(SQLException ignored)
		{
		}
		
		return all;
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
	
	public static Date convertDate(java.util.Date date)
	{
		return new Date(date.getTime());
	}
}
