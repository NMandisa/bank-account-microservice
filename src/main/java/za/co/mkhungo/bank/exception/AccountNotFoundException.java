package za.co.mkhungo.bank.exception;

import java.io.Serializable;

/**
 * @author Noxolo.Mkhungo
 */
public class AccountNotFoundException extends RuntimeException implements Serializable {
    public AccountNotFoundException(String message) {
        super( message);
    }
    public AccountNotFoundException() {super();}
    public AccountNotFoundException(String message, Throwable cause) {super(message,cause);}
}
