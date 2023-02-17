package main.java.org.example.bot.Excel.Templates;

import java.util.Date;

public class Referee
{
	private String surname; // Фамилия
	private String name; // Имя
	private String patronymic; // Отчество
	private Date birth;
	private String city;
	private String phone;
	private int refereeID;

	public Referee(String surname, String name, String patronymic, Date birth, String city, String phone, int refereeID)
	{
		this.surname = surname;
		this.name = name;
		this.patronymic = patronymic;
		this.birth = birth;
		this.city = city;
		this.phone = phone;
		this.refereeID = refereeID;
	}

	public Referee()
	{
		surname = "???";
		name = "???";
		patronymic = "???";
		birth = new Date();
		city = "???";
		phone = "???";
		refereeID = -1;
	}

	@Override
	public String toString()
	{
		return "Referee{" + "surname='" + surname + '\'' + ", name='" + name + '\'' + ", patronymic='" + patronymic + '\'' + ", birth=" + birth + ", city='" + city + '\'' + ", phone='" + phone + '\'' + ", refereeID=" + refereeID + '}';
	}

	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String surname)
	{
		this.surname = surname;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPatronymic()
	{
		return patronymic;
	}

	public void setPatronymic(String patronymic)
	{
		this.patronymic = patronymic;
	}

	public Date getBirth()
	{
		return birth;
	}

	public void setBirth(Date birth)
	{
		this.birth = birth;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public int getRefereeID()
	{
		return refereeID;
	}

	public void setRefereeID(int refereeID)
	{
		this.refereeID = refereeID;
	}
}
