package main.java.org.example.DataBase.Interfaces;

import main.java.org.example.DataBase.SQL;

import java.sql.Array;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Table
{
	private DataBase currentBase;

	private String name;

	private List<Column> columns = new ArrayList<>();

	public Table(String name)
	{
		this.name = name;
	}

	public void drop()
	{
		try
		{
			SQL.execute("drop table " + name, currentBase.statement);
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void setDatabase(DataBase dataBase)
	{
		currentBase = dataBase;
	}

	public void init(List<Column> columns)
	{
		this.columns = columns;
		StringBuilder s = new StringBuilder("create table " + name + "(");
		for(Column column : columns)
		{
			s.append(column.name()).append(" ").append(column.type().getName()).append(", ");
		}
		String substring = s.substring(0, s.length() - 2) + ");";
		try
		{
			SQL.execute(substring, currentBase.statement);
			System.out.println("Таблица " + name + " создана");
		}
		catch(SQLException e)
		{
			System.out.println("Таблица " + name + " уже существует");
		}
	}

	public void addLine(List<Column> filledColumns)
	{
		String s = "insert into " + name + " (";
		for(int i = 0; i < filledColumns.size(); i++)
		{
			if(filledColumns.get(i).data() != null)
			{
				s += filledColumns.get(i).name() + ", ";
			}
		}
		s = s.substring(0, s.length() - 2);
		s += ") values (";

		for(int i = 0; i < filledColumns.size(); i++)
		{
			if(filledColumns.get(i).data() != null)
			{
				s += "?, ";
			}
		}
		s = s.substring(0, s.length() - 2) + ");";

		PreparedStatement ps;

		try
		{
			ps = currentBase.connection.prepareStatement(s);

			int counter = 0;
			for(Column column : filledColumns)
			{
				if(column.data() != null)
				{
					counter++;
					switch(column.type())
					{
						case INTEGER -> ps.setInt(counter, (int) column.data());
						case TEXT -> ps.setString(counter, (String) column.data());
						case DATE -> ps.setDate(counter, new Date(((java.util.Date) column.data()).getTime()));
						case INTEGER_ARRAY -> ps.setArray(counter, currentBase.connection.createArrayOf("integer", new Object[]{column.data()}));
						case TEXT_ARRAY -> ps.setArray(counter, currentBase.connection.createArrayOf("text", new Object[]{column.data()}));
						case REAL_ARRAY -> ps.setArray(counter, currentBase.connection.createArrayOf("float", new Object[]{column.data()}));
					}
				}

			}
			ps.execute();
		}
		catch(SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	public List<Column> getColumns()
	{
		return columns;
	}

	public String getName()
	{
		return name;
	}
}
