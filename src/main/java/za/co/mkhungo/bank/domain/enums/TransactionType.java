package za.co.mkhungo.bank.domain.enums;

/**
 * @author Noxolo.Mkhungo
 */
public enum TransactionType {
    DEPOSIT("Adds funds to the account"),
    WITHDRAW("Removes funds from the account"),
    TRANSFER("Moves funds between accounts"),
    PAYMENT("Funds used for purchases or bill payments"),
    REFUND("Reversal of a previous payment"),
    FEE("Charges applied to the account"),
    INTEREST("Interest earned on the account balance"),
    TAX("Tax deductions applied"),
    REVERSAL("Undo a previous transaction"),
    LOAN_DISBURSEMENT("Funds issued as a loan"),
    LOAN_REPAYMENT("Repayment of loan obligations"),
    CASHBACK("Money refunded as part of a reward program");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}
