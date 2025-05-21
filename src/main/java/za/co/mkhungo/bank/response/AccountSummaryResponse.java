package za.co.mkhungo.bank.response;

import java.math.BigDecimal;

/**
 * @author Noxolo.Mkhungo
 */
public record AccountSummaryResponse(
        Long accountId,
        BigDecimal runningBalance,
        String message
) { }
