package co.com.ajac.infrastructure.commands;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> route(SpringProcessor springProcessor) {

        return RouterFunctions
          .route(
            RequestPredicates.GET("/v1/commands").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), springProcessor::execute
          );
    }
}
