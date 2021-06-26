package co.com.ajac.infrastructure.commands;

import co.com.ajac.infrastructure.api.commands.CommandUtil;
import co.com.ajac.infrastructure.api.commands.Processor;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface SpringProcessor extends Processor, CommandUtil {

    default Mono<ServerResponse> execute(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
          .body(BodyInserters.fromValue("Hello, Spring!"));
    }
}
