package it.pagopa.interop.signalhub.updater.exception;

public class PDNDBatchAlreadyExistException extends RuntimeException {

    public PDNDBatchAlreadyExistException() {
        super("Batch already in running");
    }
}
