package co.com.ajac.infrastructure.commands;

import co.com.ajac.infrastructure.api.commands.CommandProvider;
import io.vavr.collection.List;

public interface ProviderManager {
    List<CommandProvider> getCommandProviders();
}
