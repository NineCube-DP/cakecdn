package pl.ninecube.oss.cakecdn.exception;

public class TechnicalException extends RuntimeException {

    public TechnicalException(String description) {
        super(description);
    }
}
