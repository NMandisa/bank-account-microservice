package za.co.mkhungo.bank.domain.enums;

/**
 * @author Noxolo.Mkhungo
 */
public enum TransactionStatus {
    PENDING("Transaction is awaiting processing"),
    COMPLETED("Transaction has been successfully processed"),
    FAILED("Transaction could not be processed"),
    REVERSED("Transaction has been undone"),
    CANCELED("Transaction was canceled before completion"),
    IN_PROGRESS("Transaction is actively being processed"),
    AUTHORIZED("Transaction is approved but not yet completed"),
    SETTLED("Transaction has been finalized"),
    REFUNDED("Transaction amount was returned to the payer");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
