package za.co.mkhungo.bank.exception;

import java.io.Serializable;

/**
 * @author Noxolo.Mkhungo
 */
public class JsonSerializationException extends RuntimeException implements Serializable {
    public JsonSerializationException(String message) {
        super( message);
    }
    public JsonSerializationException() {super();}
    public JsonSerializationException(String message, Throwable cause) {super(message,cause);}
}
