package org.saver;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class FileSaverBot extends TelegramLongPollingBot {

    private final SendMessage message;

    @Inject
    private String addresses;

    @ConfigProperty(name = "server.path")
    private String path;

    @ConfigProperty(name = "bot.username")
    private String botUserName;

    @ConfigProperty(name = "bot.token")
    private String botToken;

    @Inject
    public FileSaverBot(SendMessage sendMessage) {
        this.message = sendMessage;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        List<String> addressList = Arrays.asList(addresses.split(","));
        if (addressList.contains(String.valueOf(update.getMessage().getChatId()))){
            if (!update.getMessage().hasDocument()) {
                sendMessage("Send like a file, please.", update);
            } else {
                Document document = update.getMessage().getDocument();
                GetFile getFile = new GetFile();
                getFile.setFileId(document.getFileId());
                String author = update.getMessage().getChat().getFirstName();
                String data = LocalDate.now().toString();
                String savePath = path + author + "/" + data + "/" + document.getFileName();
                var file = new java.io.File(savePath);
                downloadToFile(getFile, file, update);
                sendMessage("Saved " + document.getFileName() + "\nв " + savePath, update);
            }
        } else {
            sendMessage("You are out of clients.", update);
        }
    }

    private void downloadToFile(GetFile getFile, java.io.File file, Update update) {
        try {
            File saveFile = execute(getFile);
            downloadFile(saveFile, file);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            sendMessage("Something went wrong, please try again.", update);
        }
    }

    public void sendMessage(String text, Update update) {
        message.setChatId(update.getMessage().getChatId());
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String text, String address) {
        message.setChatId(address);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}