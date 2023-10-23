package co.com.ajac.infrastructure.commands;

import co.com.ajac.base.errors.AppError;
import co.com.ajac.messaging.events.Event;
import co.com.ajac.concurrency.FutureEither;
import co.com.ajac.infrastructure.api.commands.*;
import com.fasterxml.jackson.databind.JsonNode;
import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.ajac.infrastructure.api.util.StgMonad.getOption;

public interface SpringProcessor extends Processor, CommandUtil {

    default Mono<ServerResponse> execute(ServerRequest request) {
        return toExecuteCommandAndRequest(findCommandAndRequest(request));
    }

    default FutureEither<AppError, Tuple2<Command, Request>> findCommandAndRequest(ServerRequest request) {
        return processRequest((Option<String> pathUrlOpt, Future<JsonNode> commandBodyFuture) ->
            findProvider(pathUrlOpt, commandProviders())
              .flatMap(commandProvider -> proccessBody(commandBodyFuture)
                .flatMap(tuple2 -> findCommand(tuple2._1(), commandProvider)
                  .flatMap(command -> findRequest(tuple2._1(), tuple2._2(), commandProvider)
                    .map(commandRequest -> Tuple.of(command, commandRequest)))
                )),
          request);
    }

    default FutureEither<AppError, Tuple2<Option<String>, Option<JsonNode>>> proccessBody(Future<JsonNode> futureBody) {
        return FutureEither.of(futureBody.map(body -> {
              final Option<String> commandName = getOption(body.path("command")).map(JsonNode::asText);
              final Option<JsonNode> commandBody = getOption(body.path("body"));
              return Either.right(Tuple.of(commandName, commandBody));
          })
        );
    }

    default FutureEither<AppError, Tuple2<Command, Request>> processRequest
      (
        Function2<Option<String>, Future<JsonNode>, FutureEither<AppError, Tuple2<Command, Request>>> function3,
        ServerRequest request
      ) {
        final Option<String> pathUrlOpt = getOption(request.path());
        final Future<JsonNode> body = Future.fromCompletableFuture(request.bodyToMono(JsonNode.class).toFuture());
        return function3.apply(pathUrlOpt, body);
    }

    @SuppressWarnings("unchecked")
    default Mono<ServerResponse> toExecuteCommandAndRequest(FutureEither<AppError, Tuple2<Command, Request>> commandAndRequest) {
        return Mono.fromFuture(commandAndRequest
          .flatMap(commandRequestTuple2 -> (FutureEither<AppError, Tuple2<Option<Response>, List<Event>>>) commandRequestTuple2._1().execute(commandRequestTuple2._2()))
          .getValue()
          .toCompletableFuture())
          .flatMap(this::onSuccess)
          .onErrorResume(t -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Unexpected error has occurred"));
    }

    @SuppressWarnings("unchecked")
    default Mono<ServerResponse> onSuccess(Either<AppError, Tuple2<Option<Response>, List<Event>>> resultEither) {
        return resultEither
          .fold(
            this::responseError,
            response -> {
                Future.of(() ->
                  publisher().publish(response._2)
                );
                return response._1()
                  .map(commandResponse -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(commandResponse)
                  ).getOrElse(ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).bodyValue("Command executed successfully"));
            });

    }

    default Mono<ServerResponse> responseError(AppError appError) {
        return ServerResponse
          .badRequest()
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(makeResponseError(appError));
    }
}
