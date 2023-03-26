package main.java.org.example.Bot.Files;

import main.java.org.example.Main;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class MyFiles
{
	public static final String RESOURCES_ROOT;
	public static final String TEMP_ROOT;
	public static final String SOURCES_ROOT;
	
	static
	{
		String origin = new File("").getAbsolutePath() + "/";
		
		RESOURCES_ROOT = origin + "Resources/";
		TEMP_ROOT = RESOURCES_ROOT + "Temp/";
		SOURCES_ROOT = RESOURCES_ROOT + "Sources/";
		
		System.out.println("RESOURCES_ROOT: " + RESOURCES_ROOT);
	}
	
	public static File getFile(ResourcesFiles type)
	{
		File file = Paths.get(RESOURCES_ROOT + type.getFileName()).toFile();
		if(!Files.exists(file.toPath()))
		{
			saveTo(Paths.get(SOURCES_ROOT + type.getFileName()).toFile(), file.getParentFile() + "");
			//throw new RuntimeException("Файл не существует: " + file.toPath());
		}
		return file;
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
	
	public static void copyTo(ResourcesFiles source, ResourcesFiles dest)
	{
		try
		{
			FileUtils.copyFileToDirectory(MyFiles.getFile(source), MyFiles.getFile(dest).getParentFile());
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static void saveTo(File source, String dest)
	{
		try
		{
			FileUtils.copyFileToDirectory(source, new File(dest));
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
