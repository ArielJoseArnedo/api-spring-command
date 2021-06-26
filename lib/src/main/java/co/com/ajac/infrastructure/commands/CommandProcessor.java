package co.com.ajac.infrastructure.commands;

import co.com.ajac.infrastructure.api.commands.CommandProvider;
import co.com.ajac.infrastructure.api.events.EventPublisher;
import io.vavr.collection.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CommandProcessor implements SpringProcessor {

    private final ProviderManager providerManager;
    private final EventPublisher eventPublisher;

    @Autowired
    public CommandProcessor(ProviderManager providerManager, EventPublisher eventPublisher) {
        this.providerManager = providerManager;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<CommandProvider> commandProviders() {
        return providerManager.getCommandProviders();
    }

    @Override
    public EventPublisher eventPublisher() {
        return eventPublisher;
    }
}
