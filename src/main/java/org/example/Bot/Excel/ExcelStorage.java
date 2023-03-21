package main.java.org.example.Bot.Excel;

import main.java.org.example.Bot.Excel.Templates.Referee;
import main.java.org.example.Bot.Files.MyFiles;
import main.java.org.example.DataBase.SQL;
import org.apache.poi.EmptyFileException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.List;

import static main.java.org.example.Bot.Files.ResourcesFiles.*;

public class ExcelStorage
{
	private XSSFWorkbook refereeBook;
	private XSSFWorkbook competitionBook;
	
	public ExcelStorage()
	{
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
			MyFiles.copyTo(TEMPLATE_COMPETITION_ORIGINAL, TEMPLATE_COMPETITION);
			MyFiles.copyTo(TEMPLATE_REFEREE_ORIGINAL, TEMPLATE_REFEREE);
		}
		
		save(refereeBook, MyFiles.getFile(TEMPLATE_REFEREE));
		save(competitionBook, MyFiles.getFile(TEMPLATE_COMPETITION));
		
		refereeListChanged();
	}
	
	private boolean refereeListChanges;
	
	public void refereeListChanged()
	{
		refereeListChanges = true;
	}
	
	public void updateRefereesInFiles()
	{
		if(refereeListChanges)
		{
			clearRefereesData(MyFiles.getFile(TEMPLATE_COMPETITION));
			clearRefereesData(MyFiles.getFile(TEMPLATE_REFEREE));
			
			writeRefereesData(MyFiles.getFile(TEMPLATE_COMPETITION));
			writeRefereesData(MyFiles.getFile(TEMPLATE_REFEREE));
			
			refereeListChanges = false;
		}
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
		List<Referee> allReferees = SQL.getAllReferees();
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
