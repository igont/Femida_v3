package main.java.org.example.DataBase.Interfaces;

import main.java.org.example.DataBase.Type;

import java.util.Objects;

public final class Column
{
	private String name;
	private Type type;
	private Object data;

	public Column(String name, Type type, Object data)
	{
		this.name = name;
		this.type = type;
		this.data = data;
	}

	public String name()
	{
		return name;
	}

	public Type type()
	{
		return type;
	}

	public Object data()
	{
		return data;
	}

	public Column setData(Object data)
	{
		this.data = data;
		return this;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == this) return true;
		if(obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Column) obj;
		return Objects.equals(this.name, that.name) && Objects.equals(this.type, that.type) && Objects.equals(this.data, that.data);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name, type, data);
	}

	@Override
	public String toString()
	{
		return "Column[" + "name=" + name + ", " + "type=" + type + ", " + "data=" + data + ']';
	}

}
