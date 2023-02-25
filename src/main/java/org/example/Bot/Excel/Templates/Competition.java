package main.java.org.example.Bot.Excel.Templates;

import main.java.org.example.Bot.Excel.RefereePosition;

import java.util.Date;
import java.util.List;

public record Competition(String title, String place, Date date, int[] members, String[] carpets, float[] grades, RefereePosition[] positions)
{

}
