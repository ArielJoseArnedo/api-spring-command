package co.com.ajac.infrastructure.commands;

import co.com.ajac.infrastructure.api.commands.CommandProvider;
import co.com.ajac.infrastructure.api.commands.ProviderManager;
import co.com.ajac.messaging.publishers.PublisherProvider;
import io.vavr.collection.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CommandProcessor implements SpringProcessor {

    private final ProviderManager providerManager;
    private final PublisherProvider publisher;

    @Autowired
    public CommandProcessor(ProviderManager providerManager, PublisherProvider publisher) {
        this.providerManager = providerManager;
        this.publisher = publisher;
    }

    @Override
    public List<CommandProvider> commandProviders() {
        return providerManager.getCommandProviders();
    }

    @Override
    public PublisherProvider publisher() {
        return publisher;
    }
}
