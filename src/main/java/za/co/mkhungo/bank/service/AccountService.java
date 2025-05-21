package za.co.mkhungo.bank.service;

import java.math.BigDecimal;

/**
 * @author Noxolo.Mkhungo
 */
public interface AccountService {
    void withdraw(Long accountId, BigDecimal amount);
}
