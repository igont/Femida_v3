package org.example.DataBase.Interfaces;

import org.example.DataBase.SQL;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DataBase
{
	public Connection connection;
	public Statement statement;
	public final String name;
	private Map<String, Table> tables;

	public DataBase(String name)
	{
		tables = new HashMap<>();
		
		System.out.println("Connecting to Database: " + name + "...");
		System.out.println();
		this.name = name;
		try
		{
			logIn(name, SQL.USER, SQL.PASSWORD);
			System.out.println("✅Connected to database: " + name);
		}
		catch(SQLException e)
		{
			try
			{
				System.out.println("Не смогли зайти, вхожу под стандартным логином и паролем");
				logIn("postgres", "postgres", "postgres");

				System.out.println("Создаю нового пользователя");
				try
				{
					SQL.execute("create user " + SQL.USER + " with encrypted password '" + SQL.PASSWORD + "';", statement);
				}
				catch(SQLException exc)
				{
					System.out.println("Пользователь уже был создан");
				}
				System.out.println("Создаю базу данных");
				create();

				System.out.println("Захожу через нормального пользователя в эту базу данных");
				logIn(name, SQL.USER, SQL.PASSWORD);

				System.out.println("✅Connected to database: " + name);
			}
			catch(SQLException ex)
			{
				System.out.println("Не смогли зайти под стандартным логином и паролем");

				throw new RuntimeException(ex);
			}
		}
	}

	public void drop() throws SQLException
	{
		SQL.execute("drop database " + name, statement);
	}

	public void create() throws SQLException
	{
		SQL.execute("create database " + name + " with encoding \"UTF-8\";", statement);
	}

	private void logIn(String name, String user, String password) throws SQLException
	{
		connection = DriverManager.getConnection((SQL.DB_URL + name), user, password);
		statement = connection.createStatement();
	}

	public void addTable(Table table)
	{
		table.setDatabase(this);
		tables.put(table.getName(), table);
	}

	public Table getTable(String name)
	{
		return tables.get(name);
	}
}
