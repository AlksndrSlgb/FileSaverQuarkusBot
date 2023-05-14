package org.saver;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@ApplicationScoped
@Startup
@AllArgsConstructor
public class RegistrarBotService {
    private FileSaverBot photoSaver;

    @Inject
    private String addresses;
    public void makeRegistry() {
        try {
            register();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private TelegramBotsApi register() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(photoSaver);
        String[] list = addresses.split(",");
        for (String address : list) {
            photoSaver.sendMessage("Hi!", address);
        }
        return botsApi;
    }

    @PostConstruct
    public void init() {
        makeRegistry();
    }

}
