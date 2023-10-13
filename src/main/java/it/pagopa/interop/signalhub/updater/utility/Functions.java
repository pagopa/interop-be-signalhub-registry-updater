package it.pagopa.interop.signalhub.updater.utility;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
public class Functions {

    private Functions(){}

    public static <T> Function<Throwable, Mono<T>> catchWebClientException() {
        return ex -> {
            log.error("Error with Client - {}", ex.getMessage());
            return Mono.empty();
        };
    }


}
