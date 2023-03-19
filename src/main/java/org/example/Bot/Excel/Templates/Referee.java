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
		birth = new Date(new java.util.Date().getTime());
		city = "???";
		phone = "???";
		refereeID = -1;
	}
	
	@Override
	public String toString()
	{
		return "Referee{" + "surname='" + surname + '\'' + ", name='" + name + '\'' + ", patronymic='" + patronymic + '\'' + ", birth=" + birth + ", city='" + city + '\'' + ", phone='" + phone + '\'' + ", refereeID=" + refereeID + '}';
	}
	
	public int findRefereeID()
	{
		Connection connection = Main.sql.mainDatabase.connection;
		ResultSet resultSet;
		
		PreparedStatement preparedStatement;
		try
		{
			preparedStatement = connection.prepareStatement("select * from referee where name = ? and surname = ? and patronymic = ?;");
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, surname);
			preparedStatement.setString(3, patronymic);
			resultSet = preparedStatement.getResultSet();
			
			if(resultSet.next())
			{
				refereeID = resultSet.getInt("id");
				return refereeID;
			}
			else
			{
				System.out.printf("Referee not found [%s] [%s] [%s]\n", name, surname, patronymic);
				return -1;
			}
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
}
