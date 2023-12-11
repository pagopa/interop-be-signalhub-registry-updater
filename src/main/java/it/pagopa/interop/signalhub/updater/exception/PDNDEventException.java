package it.pagopa.interop.signalhub.updater.exception;

import lombok.Getter;

@Getter
public class PDNDEventException extends RuntimeException {
    private final Long eventId;

    public PDNDEventException(String message, Long eventId) {
        super(message);
        this.eventId = eventId;
    }

}
