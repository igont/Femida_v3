package main.java.org.example.Bot.Excel.Templates;

import main.java.org.example.DataBase.Interfaces.Table;
import main.java.org.example.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import static com.itextpdf.text.pdf.XfaXpathConstructor.XdpPackage.Config;

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
				System.out.printf("❗️Referee not found id: [%s]\n", id);
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
				refereeID = id;
			}
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public Referee(String surname, String name, String patronymic, Date birth, String city, String phone, String category, int refereeID, String clubType, String clubName)
	{
		this.surname = surname;
		this.name = name;
		this.patronymic = patronymic;
		this.birth = birth;
		this.city = city;
		this.phone = phone;
		this.category = category;
		this.refereeID = refereeID;
		this.clubType = clubType;
		this.clubName = clubName;
	}
	
	public Referee()
	{
		surname = "???";
		name = "???";
		patronymic = "???";
		birth = null;
		city = "???";
		phone = "???";
		refereeID = -1;
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
				*Клуб:* `%s`
				
				`---------------------------------
				Эта информация видна только вам и
				администратору`""";
		
		return String.format(out, surname, name, patronymic, birthS, city, phone, category, clubType + " " + clubName);
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
	
	public void setRefereeID(int refereeID)
	{
		this.refereeID = refereeID;
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
}
