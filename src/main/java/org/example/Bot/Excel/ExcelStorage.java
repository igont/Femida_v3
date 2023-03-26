package main.java.org.example.Bot.Excel;

import main.java.org.example.Bot.Excel.Templates.Referee;
import main.java.org.example.Bot.Files.MyFiles;
import main.java.org.example.DataBase.SQL;
import main.java.org.example.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.EmptyFileException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.SQLOutput;
import java.util.List;

import static main.java.org.example.Bot.Files.ResourcesFiles.*;

public class ExcelStorage
{
	private XSSFWorkbook refereeBook;
	private XSSFWorkbook competitionBook;
	
	public ExcelStorage()
	{
		System.setProperty("log4j.configurationFile","./path_to_the_log4j2_config_file/log4j2.xml");
		Logger log = LogManager.getLogger(ExcelStorage.class.getName());
		
		try
		{
			refereeBook = new XSSFWorkbook(new FileInputStream(MyFiles.getFile(TEMPLATE_REFEREE)));
			competitionBook = new XSSFWorkbook(new FileInputStream(MyFiles.getFile(TEMPLATE_COMPETITION)));
		}
		catch(IOException ignored)
		{
		
		}
		catch(EmptyFileException e)
		{
			MyFiles.saveTo(MyFiles.getFile(TEMPLATE_COMPETITION_ORIGINAL), MyFiles.getResourcesPath());
			MyFiles.saveTo(MyFiles.getFile(TEMPLATE_REFEREE_ORIGINAL), MyFiles.getResourcesPath());
		}
		
		save(refereeBook, MyFiles.getFile(TEMPLATE_REFEREE));
		save(competitionBook, MyFiles.getFile(TEMPLATE_COMPETITION));
		System.out.println();
		System.out.println("✅Excel templates successfully loaded");
	}
	
	
	public void updateRefereesInFiles()
	{
		clearRefereesData(MyFiles.getFile(TEMPLATE_COMPETITION));
		clearRefereesData(MyFiles.getFile(TEMPLATE_REFEREE));
		
		writeRefereesData(MyFiles.getFile(TEMPLATE_COMPETITION));
		writeRefereesData(MyFiles.getFile(TEMPLATE_REFEREE));
	}
	
	private void clearRefereesData(File bookFile)
	{
		XSSFWorkbook book = getBookFromFile(bookFile);
		XSSFSheet sheet = book.getSheetAt(1);
		
		int row = 3;
		
		while(sheet.getRow(row) != null)
		{
			sheet.removeRow(sheet.getRow(row++));
		}
		
		save(book, bookFile);
	}
	
	private void writeRefereesData(File bookFile)
	{
		List<Referee> allReferees = Main.sql.getAllReferees();
		XSSFWorkbook book = getBookFromFile(bookFile);
		XSSFSheet sheet = book.getSheetAt(1);
		
		for(int i = 0; i < allReferees.size(); i++)
		{
			Referee currentReferee = allReferees.get(i);
			
			sheet.createRow(i + 3).createCell(1).setCellValue(currentReferee.getSurname());
			sheet.getRow(i + 3).createCell(2).setCellValue(currentReferee.getName());
			sheet.getRow(i + 3).createCell(3).setCellValue(currentReferee.getPatronymic());
			sheet.getRow(i + 3).createCell(4).setCellValue(currentReferee.getSurname() + " " + currentReferee.getName() + " " + currentReferee.getPatronymic());
		}
		
		save(book, bookFile);
	}
	
	private void save(XSSFWorkbook book, File file)
	{
		try
		{
			book.write(new FileOutputStream(file));
			book.close();
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public XSSFWorkbook getRefereeBook()
	{
		updateRefereesInFiles();
		return refereeBook;
	}
	
	public XSSFWorkbook getCompetitionBook()
	{
		updateRefereesInFiles();
		return competitionBook;
	}
	
	public File getRefereeBookFile()
	{
		updateRefereesInFiles();
		return MyFiles.getFile(TEMPLATE_REFEREE);
	}
	
	public File getCompetitionBookFile()
	{
		updateRefereesInFiles();
		return MyFiles.getFile(TEMPLATE_COMPETITION);
	}
	
	private XSSFWorkbook getBookFromFile(File file)
	{
		try
		{
			return new XSSFWorkbook(new FileInputStream(file));
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}
