package co.com.ajac.infrastructure.commands;

import co.com.ajac.base.events.Publisher;
import co.com.ajac.infrastructure.api.commands.CommandProvider;
import co.com.ajac.infrastructure.api.commands.ProviderManager;
import io.vavr.collection.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CommandProcessor implements SpringProcessor {

    private final ProviderManager providerManager;
    private final Publisher publisher;

    @Autowired
    public CommandProcessor(ProviderManager providerManager, Publisher publisher) {
        this.providerManager = providerManager;
        this.publisher = publisher;
    }

    @Override
    public List<CommandProvider> commandProviders() {
        return providerManager.getCommandProviders();
    }

    @Override
    public Publisher publisher() {
        return publisher;
    }
}
