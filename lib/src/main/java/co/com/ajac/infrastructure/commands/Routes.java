package co.com.ajac.infrastructure.commands;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

public interface Routes {
    RouterFunction<ServerResponse> endpoints();
}
