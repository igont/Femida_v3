package main.java.org.example.bot.Excel;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser
{
	private XSSFWorkbook book;

	private XSSFSheet sheet;

	public ExcelParser()
	{
	}

	public ExcelParser setWorkbook(XSSFWorkbook book)
	{
		this.book = book;
		return this;
	}

	public ExcelParser setSheet(int sheetNum)
	{
		this.sheet = book.getSheetAt(sheetNum);
		return this;
	}

	public String readCell(int row, int column)
	{
		XSSFCell cell = sheet.getRow(row).getCell(column);
		CellType cellType = cell.getCellType();
		return readDifferentCellTypes(cell, cellType);
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
