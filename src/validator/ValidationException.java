package validator;

/**
 * Clasa ValidationException
 * Exceptie particularizata
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructorul clasei
     * @param message - string (mesajul afisat la aruncarea exceptiei)
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Getter mesaj exceptie
     * @return mesaj
     */
    public String getMessage() {
        return super.getMessage();
    }
}
