package main.java.org.example.DataBase;

public enum Type
{
	INTEGER("INTEGER"),
	SMALLSERIAL("SMALLSERIAL"),
	TEXT("TEXT"),
	DATE("DATE"),
	REAL("REAL"),
	INTEGER_ARRAY("INTEGER ARRAY"),
	TEXT_ARRAY("TEXT ARRAY"),
	REAL_ARRAY("REAL ARRAY");
	//SMALLINT("SMALLINT"),
	//BIGINT("BIGINT"),
	//SERIAL("SERIAL"),
	//BIGSERIAL("BIGSERIAL"),
	//NUMERIC("NUMERIC"),
	//FLOAT("FLOAT"),
	//DOUBLE("DOUBLE"),
	//CHAR("CHAR"),
	//VARCHAR("VARCHAR"),
	//BOOLEAN("BOOLEAN"),
	//TIME("TIME"),
	//ENUM("ENUM"),

	private final String name;

	Type(String s)
	{
		name = s;
	}

	public String getName()
	{
		return name;
	}
}
