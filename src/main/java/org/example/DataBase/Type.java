package org.example.DataBase;

public enum Type
{
	INTEGER("INTEGER"),
	SMALLSERIAL("SMALLSERIAL"),
	TEXT("TEXT"),
	DATE("DATE"),
	REAL("REAL"),
	INTEGER_ARRAY("integer[]"),
	TEXT_ARRAY("text[]"),
	REAL_ARRAY("real[]");
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
