package main.java.org.example.Bot.Excel.Templates;

import main.java.org.example.Bot.Dialogue.Possibility;
import main.java.org.example.Bot.Dialogue.Role;
import main.java.org.example.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Referee
{
	private String surname; // Фамилия
	private String name; // Имя
	private String patronymic; // Отчество
	private Date birth;
	private String city;
	private String phone;
	private String category;
	private int refereeID;
	private String clubType;
	private String clubName;
	private Role role;
	
	
	public Referee(int id)
	{
		Connection connection = Main.sql.mainDatabase.connection;
		ResultSet resultSet;
		
		PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("select * from referee where id = ?;");
			preparedStatement.setInt(1, id);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
			{
				//System.out.printf("❗️Referee not found id: [%s]\n", id);
			}
			else if(resultSet.next())
			{
				surname = resultSet.getString("surname");
				name = resultSet.getString("name");
				patronymic = resultSet.getString("patronymic");
				birth = resultSet.getDate("birth");
				city = resultSet.getString("city");
				phone = resultSet.getString("phone");
				category = resultSet.getString("category");
				clubType = resultSet.getString("club_type");
				clubName = resultSet.getString("club_name");
				role = new Role(resultSet.getString("role"));
				refereeID = id;
			}
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public Referee()
	{
		role = new Role();
		surname = "";
		name = "";
		patronymic = "";
		birth = null;
		city = "";
		phone = "";
		refereeID = -1;
		category = "";
	}
	
	public static Referee getRandomReferee()
	{
		List<String> surNames = List.of("Бишкеков", "Арбузов", "Архипов", "Осипов", "Белов", "Белоус", "Бондаренко", "Букин", "Волков", "Давыдов", "Духов", "Егоров", "Квасов");
		List<String> names = List.of("Игорь", "Василий", "Петр", "Анатолий", "Антон", "Вячеслав", "Артем", "Виктор", "Павел", "Сергей", "Виталий", "Евгений", "Борис");
		List<String> patronymics = List.of("Бебрович", "Ильич", "Николаевич", "Георгиевич", "Хасанович", "Дудкович", "Юрьевич", "Николаевич", "Константинович", "Вадимович", "Жмыхович");
		
		List<String> clubs = List.of("Дружный", "Богатырь", "Победитель", "Мордобой", "Недалекий", "Разливуха", "Неподкупный", "Синяк", "Перелом");
		List<String> regions = List.of("Мухосранск", "Татарстан", "Москва", "Санкт-Петербург", "Челябинск", "Оренбург", "Новосибирск", "Московская область", "Калининград", "Ленинградская область", "Кемеровская область");
		
		Referee referee = new Referee();
		referee.setSurname(getRandomValue(surNames));
		referee.setName(getRandomValue(names));
		referee.setPatronymic(getRandomValue(patronymics));
		
		referee.setClubType("СК");
		referee.setClubName(getRandomValue(clubs));
		referee.setCity(getRandomValue(regions));
		referee.role = Role.getRandomRole();
		referee.phone = randomPhone();
		
		return referee;
	}
	
	private static String randomPhone()
	{
		StringBuilder s = new StringBuilder();
		Random rand = new Random();
		
		s.append(8);
		s.append(rand.nextInt(900) + 100);
		s.append(rand.nextInt(900) + 100);
		s.append(rand.nextInt(9000) + 1000);
		
		return s.toString();
	}
	
	private static <T> T getRandomValue(List<T> list)
	{
		Random random = new Random();
		return list.get(random.nextInt(list.size()));
	}
	
	@Override
	public String toString()
	{
		return "Referee{" + "surname='" + surname + '\'' + ", name='" + name + '\'' + ", patronymic='" + patronymic + '\'' + ", birth=" + birth + ", city='" + city + '\'' + ", phone='" + phone + '\'' + ", refereeID=" + refereeID + '}';
	}
	
	public String toNiceString()
	{
		String birthS = birth + "";
		if(birth == null) birthS = "???";
		
		String out = """
				*Фамилия:* `%s`
				*Имя:* `%s`
				*Отчество:* `%s`
				
				*Дата рождения:* `%s`
				*Город:* `%s`
				*Номер телефона:* `%s`
				
				*Категория:* `%s`
				*Клуб:* `%s`""";
		
		String possibilities = """
				\n\n*Доступные команды:*
				`%s`
				""";
		
		String disclaimer = """
				`---------------------------------
				Эта информация видна только вам и
				администратору`""";
		
		out = String.format(out, surname, name, patronymic, birthS, city, phone, category, clubType + " " + clubName);
		
		Set<Map.Entry<Possibility, Boolean>> entries = role.getCopyOfPossibilities().entrySet();
		if(entries.size() != 0)
		{
			StringBuilder b = new StringBuilder();
			entries.stream().filter(Map.Entry::getValue).forEach(v -> b.append(v.getKey()).append("\n"));
			
			possibilities = String.format(possibilities, b);
		}
		else
		{
			possibilities = "\n\n";
		}
		
		return out + possibilities + disclaimer;
	}
	
	public static int findRefereeID(String surname, String name, String patronymic)
	{
		Connection connection = Main.sql.mainDatabase.connection;
		ResultSet resultSet;
		
		PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("select * from referee where surname = ? and name = ? and patronymic = ?;");
			preparedStatement.setString(1, surname);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, patronymic);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
			{
				System.out.printf("❗️Referee not found [%s] [%s] [%s]\n", surname, name, patronymic);
				return -1;
			}
			else if(resultSet.next())
			{
				return resultSet.getInt("id");
			}
			return -1;
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static int findRefereeByPhone(String phone)
	{
		Connection connection = Main.sql.mainDatabase.connection;
		ResultSet resultSet;
		
		PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("select * from referee where phone = ?;");
			preparedStatement.setString(1, phone);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
			{
				System.out.printf("❗️Referee not found phone: [%s]\n", phone);
				return -1;
			}
			else if(resultSet.next())
			{
				return resultSet.getInt("id");
			}
			return -1;
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static int findRefereeByFIO(String surname, String name, String patronymic)
	{
		Connection connection = Main.sql.mainDatabase.connection;
		ResultSet resultSet;
		try
		{
			PreparedStatement preparedStatement;
			preparedStatement = connection.prepareStatement("select * from referee where surname = ? and name = ? and patronymic = ?;");
			preparedStatement.setString(1, surname);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, patronymic);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet == null)
			{
				System.out.printf("❗️Referee not found FIO: [%s] [%s] [%s]\n", surname, name, patronymic);
				return -1;
			}
			else if(resultSet.next())
			{
				return resultSet.getInt("id");
			}
			return -1;
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void setSurname(String surname)
	{
		this.surname = surname;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setPatronymic(String patronymic)
	{
		this.patronymic = patronymic;
	}
	
	public void setBirth(Date birth)
	{
		this.birth = birth;
	}
	
	public void setCity(String city)
	{
		this.city = city;
	}
	
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	
	public void setCategory(String category)
	{
		this.category = category;
	}
	
	public void setClubType(String clubType)
	{
		this.clubType = clubType;
	}
	
	public void setClubName(String clubName)
	{
		this.clubName = clubName;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public String getClubName()
	{
		return clubName;
	}
	
	public String getClubType()
	{
		return clubType;
	}
	
	public String getCity()
	{
		return city;
	}
	
	public int getRefereeID()
	{
		return refereeID;
	}
	
	public Date getBirth()
	{
		return birth;
	}
	
	public String getCategory()
	{
		return category;
	}
	
	public String getPatronymic()
	{
		return patronymic;
	}
	
	public String getSurname()
	{
		return surname;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getFIO()
	{
		return surname + " " + name + " " + patronymic;
	}
	
	public Role getRole()
	{
		return role;
	}
	
	public void setRole(Role role)
	{
		this.role = role;
	}
}
