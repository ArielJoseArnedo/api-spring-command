package co.com.ajac.infrastructure.commands;

import co.com.ajac.infrastructure.api.controllers.ControllerManager;
import co.com.ajac.infrastructure.api.controllers.ControllerProvider;
import co.com.ajac.messaging.publishers.PublisherProvider;
import io.vavr.collection.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CommandProcessor implements SpringProcessor {

    private final ControllerManager controllerManager;
    private final PublisherProvider publisher;

    @Autowired
    public CommandProcessor(ControllerManager controllerManager, PublisherProvider publisher) {
        this.controllerManager = controllerManager;
        this.publisher = publisher;
    }


    @Override
    public List<ControllerProvider> controllerProviders() {
        return controllerManager.getControllerProviders();
    }

    @Override
    public PublisherProvider publisher() {
        return publisher;
    }
}
