package it.pagopa.interop.signalhub.updater.exception;

import lombok.Getter;

@Getter
public class PDNDClientException extends RuntimeException {
    private final Long eventId;

    public PDNDClientException(String message, Long eventId) {
        super(message);
        this.eventId = eventId;
    }

}
