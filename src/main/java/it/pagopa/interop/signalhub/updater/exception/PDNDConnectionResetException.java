package it.pagopa.interop.signalhub.updater.exception;
import lombok.Getter;


@Getter
public class PDNDConnectionResetException extends RuntimeException{
    private final Long eventId;

    public PDNDConnectionResetException(String message, Long eventId) {
        super(message);
        this.eventId = eventId;
    }
}
