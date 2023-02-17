package main.java.org.example.bot.Files;

import main.java.org.example.Main;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

public class MyFiles
{
	private static final String RESOURCES_ROOT;

	static
	{
		String className = Main.class.getName().replace('.', '/');
		String classJar = Main.class.getResource("/" + className + ".class").toString();
		String path = "";

		String[] split = classJar.split("/");
		for(int i = 1; i < split.length; i++)
		{
			path += split[i] + "/";
			if(Objects.equals(split[i], "Femida_v0.3")) break;
		}
		if(path.startsWith("home"))
		{
			path = "/" + path;
		}
		RESOURCES_ROOT = path + "Resources/";
	}

	public static File getFile(ResourcesFiles type)
	{
		return Paths.get(RESOURCES_ROOT + type.getFileName()).toFile();
	}

	private static void getResourcesPath()
	{

	}
}
