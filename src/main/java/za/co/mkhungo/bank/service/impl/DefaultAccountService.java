package za.co.mkhungo.bank.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import za.co.mkhungo.bank.constants.MessageKeys;
import za.co.mkhungo.bank.domain.Account;
import za.co.mkhungo.bank.domain.AccountAudit;
import za.co.mkhungo.bank.domain.Transact;
import za.co.mkhungo.bank.domain.enums.TransactionStatus;
import za.co.mkhungo.bank.domain.enums.TransactionType;
import za.co.mkhungo.bank.domain.event.WithdrawalEvent;
import za.co.mkhungo.bank.domain.event.publisher.impl.WithdrawalEventPublisherImpl;
import za.co.mkhungo.bank.exception.AccountNotFoundException;
import za.co.mkhungo.bank.exception.TransactionFailedException;
import za.co.mkhungo.bank.helper.MessageLookup;
import za.co.mkhungo.bank.repository.AccountRepository;
import za.co.mkhungo.bank.service.AccountService;
import za.co.mkhungo.bank.utils.AccountValidationUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Noxolo.Mkhungo
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultAccountService implements AccountService {

    private final AccountRepository accountRepository;
    private final WithdrawalEventPublisherImpl defaultWithdrawEventPublisher;

    /**
     * Performs a withdrawal transaction
     * @param accountId
     * @param amount The amount to withdraw
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Retryable(
            include = {OptimisticLockingFailureException.class},
            backoff = @Backoff(delay = 100)
    )
    public void withdraw(Long accountId, BigDecimal amount) {
        try {
            //The correct way of logging for L10,I18N purposes and demo
            log.info(MessageLookup.getMessage(MessageKeys.ACCOUNT_WITHDRAW_TRANSACTION_START), accountId, amount);
            AccountValidationUtils.validateAccountId(accountId);

            Account account = accountRepository.findByIdForUpdate(accountId)
                    .orElseThrow(() -> new AccountNotFoundException(
                            MessageLookup.getMessage(MessageKeys.ACCOUNT_NOT_FOUND, accountId)));
            AccountValidationUtils.validateAccountStatus(account);
            performWithdrawal(account, amount);

            // Publish event
            defaultWithdrawEventPublisher.publish(WithdrawalEvent.builder().amount(amount)
                    .accountId(account.getId()).status("SUCCESSFUL").build());

            //Should be using the correct way by so that these messages are localized this was purely for demo purposes
            //to avoid waiting time
            log.info("Withdrawal completed for account {}", accountId);
        } catch (DataAccessException ex) {
            //Should be using the correct way by so that these messages are localized this was purely for demo purposes
            //to avoid wasting time
            log.error(MessageLookup.getMessage("error.database.withdraw"),accountId,ex);
            throw new TransactionFailedException(
                    MessageLookup.getMessage("transaction.database.error"), ex);
        }
    }

    private void performWithdrawal(Account account, BigDecimal amount) {
        account.withdraw(amount); // Encapsulated domain logic
        account.addTransaction(new Transact(
                TransactionType.WITHDRAW,
                TransactionStatus.COMPLETED,
                amount,
                LocalDateTime.now()
        ));
        account.addAuditLog(new AccountAudit(
                "Withdrawal processed",
                amount,
                LocalDateTime.now(),
                "user"  // Hard coded on PURPOSE but the reality is should be sourced from Security Context(Out of Scope)
        ));
        accountRepository.saveAndFlush(account);
        //Should be using the correct way by so that these messages are localized this was purely for demo purposes
        //to avoid wasting time
        log.debug("Account {} balance updated to {}", account.getId(), account.getBalance());
    }
}
