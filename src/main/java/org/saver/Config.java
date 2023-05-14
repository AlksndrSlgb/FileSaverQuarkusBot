package org.saver;

import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@ApplicationScoped
public class Config {

    @ConfigProperty(name = "user.addresses")
    private String addresses;

    @Produces
    public SendMessage sendMessage() {
        return new SendMessage();
    }

    @Produces()
    @DefaultBean
    public String getAddresses() {
        return addresses;
    }
}
