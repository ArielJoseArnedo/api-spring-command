package co.com.ajac.infrastructure.commands;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> route(Routes routes, SpringProcessor springProcessor) {
        return routes.endpoints();
    }
}
