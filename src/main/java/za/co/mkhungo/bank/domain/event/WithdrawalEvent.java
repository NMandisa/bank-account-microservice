package za.co.mkhungo.bank.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Noxolo.Mkhungo
 */
@Data
@Builder
@AllArgsConstructor
public class WithdrawalEvent {
    private BigDecimal amount;
    private Long accountId;
    private String status;
}
