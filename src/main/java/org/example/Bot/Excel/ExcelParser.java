package main.java.org.example.Bot.Excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;

public class ExcelParser
{
	public final XSSFWorkbook book;

	private XSSFSheet sheet;

	public ExcelParser(File file) throws IOException, InvalidFormatException
	{
		book = new XSSFWorkbook(file);
		setSheet(0);
	}

	public ExcelParser setSheet(int sheetNum)
	{
		this.sheet = book.getSheetAt(sheetNum);
		return this;
	}

	public Cell readCell(int row, int column)
	{
		try
		{
			return sheet.getRow(row).getCell(column);

		}
		catch(NullPointerException e)
		{
			return null;
		}
	}

	private String readDifferentCellTypes(XSSFCell cell, CellType cellType)
	{
		switch(cellType)
		{
			case _NONE, BLANK ->
			{
				return "";
			}
			case NUMERIC ->
			{
				return cell.getNumericCellValue() + "";
			}
			case STRING ->
			{
				return cell.getStringCellValue();
			}
			case FORMULA ->
			{
				CellType cachedFormulaResultType = cell.getCachedFormulaResultType();
				return readDifferentCellTypes(cell, cachedFormulaResultType);
			}
			case BOOLEAN ->
			{
				return cell.getBooleanCellValue() + "";
			}
			case ERROR ->
			{
				return cell.getErrorCellString();
			}
			default -> throw new IllegalStateException("Unexpected value: " + cellType);
		}
	}
}
