package main.java.org.example.bot.TG;

import main.java.org.example.bot.User;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

import static main.java.org.example.Main.myBot;

public class TGSender
{
	private String text;
	private InlineKeyboardMarkup inlineKeyboardMarkup;
	private File sendFile;
	private File sendImage;

	public void setText(String msg)
	{
		this.text = msg;
	}

	public void setImage(File img)
	{
		this.sendImage = img;
	}

	public void setSendFile(File sendFile)
	{
		this.sendFile = sendFile;
	}

	public void setInlineKeyboardMarkup(InlineKeyboardMarkup inlineKeyboardMarkup)
	{
		this.inlineKeyboardMarkup = inlineKeyboardMarkup;
	}

	public Message sendPreparedMessage()
	{
		if(sendFile != null)
		{
			return sendFileMessage();
		}
		if(sendImage != null)
		{
			return sendImageMessage();
		}

		SendMessage message = new SendMessage();
		if(!text.isEmpty()) message.setText(text);
		if(!(inlineKeyboardMarkup == null)) message.setReplyMarkup(inlineKeyboardMarkup);
		message.setChatId(User.getChatID());
		message.setParseMode(ParseMode.MARKDOWN);

		Message result;

		result = send(message);
		return result;
	}

	private Message sendFileMessage()
	{
		SendDocument sendDocument = new SendDocument();
		sendDocument.setChatId(User.getChatID());
		sendDocument.setDocument(new InputFile(sendFile));

		return send(sendDocument);
	}

	private Message sendImageMessage()
	{
		SendPhoto sendPhoto = new SendPhoto();
		sendPhoto.setChatId(User.getChatID());
		sendPhoto.setPhoto(new InputFile(sendImage));
		sendPhoto.setCaption(text);

		if(inlineKeyboardMarkup != null) sendPhoto.setReplyMarkup(inlineKeyboardMarkup);

		return send(sendPhoto);
	}

	public Message send(SendDocument sendDocument)
	{
		Message result;
		try
		{
			result = myBot.execute(sendDocument);
		}
		catch(TelegramApiException e)
		{
			throw new RuntimeException(e);
		}
		return result;
	}

	public Message send(SendPhoto sendPhoto)
	{
		Message result;
		try
		{
			result = myBot.execute(sendPhoto);
		}
		catch(TelegramApiException e)// Скорее всего картинки нет
		{
			return null;
		}
		return result;
	}

	public static Message send(String s) // Отправляет текст в чат
	{
		SendMessage message = new SendMessage();
		message.setChatId(User.getChatID());
		message.setParseMode(ParseMode.MARKDOWN);
		message.setText(s);

		return send(message);
	}

	public static Message send(SendMessage message)
	{
		Message result;
		try
		{
			result = myBot.execute(message);
		}
		catch(TelegramApiException e)
		{
			throw new RuntimeException(e);
		}
		return result;
	}
}
