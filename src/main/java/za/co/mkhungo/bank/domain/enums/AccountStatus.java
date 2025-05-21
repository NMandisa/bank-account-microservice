package za.co.mkhungo.bank.domain.enums;

/**
 * @author Noxolo.Mkhungo
 * Enum for account status
 */

public enum AccountStatus {
    ACTIVE("Account is fully operational"),
    INACTIVE("Account is temporarily unused"),
    FROZEN("Account is restricted due to security or compliance issues"),
    CLOSED("Account has been permanently shut down");

    private final String description;

    AccountStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}
