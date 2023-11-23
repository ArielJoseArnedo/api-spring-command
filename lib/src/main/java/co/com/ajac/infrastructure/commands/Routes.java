package co.com.ajac.infrastructure.commands;

import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

public interface Routes {
    <T extends ServerResponse> RouterFunction<T> endpoints(HandlerFunction<T> handlerFunction);
}
