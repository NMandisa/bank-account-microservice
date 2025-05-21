package za.co.mkhungo.bank.constants;

/**
 * @author Noxolo.Mkhungo
 */
public final class MessageKeys {
    public static final String ACCOUNT_NOT_FOUND = "account.not.found";
    public static final String ACCOUNT_WITHDRAW_TRANSACTION_START ="account.withdrawal.transaction.start";
    //Every key being referenced from her not directly from properties files as constants
    private MessageKeys() {
        // Prevent instantiation
    }
}
