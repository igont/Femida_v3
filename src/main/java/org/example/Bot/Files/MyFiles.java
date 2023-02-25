package main.java.org.example.Bot.Files;

import main.java.org.example.Main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class MyFiles
{
	public static final String RESOURCES_ROOT;
	public static final String TEMP_ROOT;

	static
	{
		String className = Main.class.getName().replace('.', '/');
		String classJar = Main.class.getResource("/" + className + ".class").toString();
		System.out.println("classJar: " + classJar);
		System.out.println("className: " + className);
		String path = "";

		String[] split = classJar.split("/");
		for(int i = 1; i < split.length; i++)
		{
			if(Objects.equals(split[i], "Femida.jar!")) break;
			path += split[i] + "/";
			if(Objects.equals(split[i], "Femida_v0.3")) break;
		}

		if(path.startsWith("home"))
		{
			path = "/" + path;
		}

		RESOURCES_ROOT = path + "Resources/";
		TEMP_ROOT = RESOURCES_ROOT + "Temp/";
		System.out.println("RESOURCES_ROOT" + RESOURCES_ROOT);
		System.out.println();
	}

	public static File getFile(ResourcesFiles type)
	{
		File file = Paths.get(RESOURCES_ROOT + type.getFileName()).toFile();
		if(Files.exists(file.toPath()))
		{
			return file;
		}
		else
		{
			throw new RuntimeException("Файл не существует: " + file.toPath());
		}
	}

	public static File getFile(String path)
	{
		return Paths.get(path).toFile();
	}

	public static String getResourcesPath()
	{
		return RESOURCES_ROOT;
	}

	public static String getTempPath()
	{
		return RESOURCES_ROOT + "Temp/";
	}
}
