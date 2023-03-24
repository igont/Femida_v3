package main.java.org.example.Bot.Excel.Templates;

import main.java.org.example.Bot.Excel.RefereePosition;
import main.java.org.example.DataBase.SQL;
import org.checkerframework.checker.units.qual.C;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Competition
{
	private String title;
	private String place;
	private Date date;
	private int[] members;
	private String[] carpets;
	private float[] grades;
	private RefereePosition[] positions;
	
	public Competition()
	{
		title = "???";
		place = "???";
		date = null;
		members = null;
		carpets = null;
		grades = null;
		positions = null;
	}
	
	public Competition(String title, String place, Date date, int[] members, String[] carpets, float[] grades, RefereePosition[] positions)
	{
		this.title = title;
		this.place = place;
		this.date = date;
		this.members = members;
		this.carpets = carpets;
		this.grades = grades;
		this.positions = positions;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getPlace()
	{
		return place;
	}
	
	public Date getDate()
	{
		return date;
	}
	
	public int[] getMembers()
	{
		return members;
	}
	
	public String[] getCarpets()
	{
		return carpets;
	}
	
	public float[] getGrades()
	{
		return grades;
	}
	
	public RefereePosition[] getPositions()
	{
		return positions;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public void setPlace(String place)
	{
		this.place = place;
	}
	
	public void setDate(Date date)
	{
		this.date = date;
	}
	
	public void setMembers(int[] members)
	{
		this.members = members;
	}
	
	public void setCarpets(String[] carpets)
	{
		this.carpets = carpets;
	}
	
	public void setGrades(float[] grades)
	{
		this.grades = grades;
	}
	
	public void setPositions(RefereePosition[] positions)
	{
		this.positions = positions;
	}
	
	public static Competition getRandomCompetition()
	{
		List<String> names = List.of("Всероссийский чемпионат", "Отборочный тур", "Просто мордобой", "Первенство Москвы", "Показательное соревнование");
		List<String> regions = List.of("Мухосранск", "Татарстан", "Москва", "Санкт-Петербург", "Челябинск", "Оренбург", "Новосибирск", "Московская область", "Калининград", "Ленинградская область", "Кемеровская область");
		
		Competition competition = new Competition();
		competition.setTitle(getRandomValue(names));
		competition.setPlace(getRandomValue(regions));
		competition.setDate(SQL.convertDate(getRandomDate()));
		
		Random random = new Random();
		List<Integer> ids = Stream.generate(() -> random.nextInt(1, 11)).limit(random.nextInt(3, 10)).toList();
		int[] idArr = new int[ids.size()];
		float[] pointsArr = new float[ids.size()];
		RefereePosition[] refPosArr = new RefereePosition[ids.size()];
		
		for(int i = 0; i < ids.size(); i++)
		{
			idArr[i] = ids.get(i);
			pointsArr[i] = random.nextInt(1, 10);
			refPosArr[i] = RefereePosition.getRandomRefereePosition();
		}
		competition.setMembers(idArr);
		competition.setGrades(pointsArr);
		competition.setPositions(refPosArr);
		
		return competition;
	}
	
	private static <T> T getRandomValue(List<T> list)
	{
		Random random = new Random();
		return list.get(random.nextInt(list.size()));
	}
	
	private static Date getRandomDate()
	{
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		int year = 2023;
		
		int maxDay = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_YEAR);
		int currentDay = new GregorianCalendar().get(Calendar.DAY_OF_YEAR);
		
		if(currentDay < maxDay) maxDay = currentDay;
		
		int dayOfYear = new Random().nextInt(1, maxDay);
		
		gregorianCalendar.set(Calendar.YEAR, year);
		gregorianCalendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
		
		return gregorianCalendar.getTime();
	}
	
}
