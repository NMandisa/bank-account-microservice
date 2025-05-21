package za.co.mkhungo.bank;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import za.co.mkhungo.bank.domain.Account;
import za.co.mkhungo.bank.domain.enums.AccountStatus;
import za.co.mkhungo.bank.repository.AccountRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * @author Noxolo.Mkhungo
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final AccountRepository accountRepository;

    @PostConstruct
    @Transactional
    public void init() {
        try {
            log.info("Starting data initialization...");
        if (accountRepository.count() == 0) {
            List<Account> dummyAccounts = List.of(
                    createAccount(generateValidAccountNumber("ZA","ZAR"), "10000.00", AccountStatus.ACTIVE),
                    createAccount(generateValidAccountNumber("ZA","ZAR"), "5000.00", AccountStatus.ACTIVE),
                    createAccount(generateValidAccountNumber("ZA","ZAR"), "250.50", AccountStatus.ACTIVE),
                    createAccount(generateValidAccountNumber("ZA","ZAR"), "0.00", AccountStatus.INACTIVE),
                    createAccount(generateValidAccountNumber("ZA","ZAR"), "800000.00", AccountStatus.FROZEN),
                    createAccount(generateValidAccountNumber("ZA","ZAR"), "100000.00", AccountStatus.ACTIVE)
            );

            accountRepository.saveAllAndFlush(dummyAccounts);
            log.info("Inserted {} dummy accounts", dummyAccounts.size());
        } else {
            log.info("Database already contains data, skipping initialization");
        }
        } catch (Exception e) {
            log.error("Data initialization failed", e);
            throw new IllegalStateException("Failed to initialize dummy data", e);
        }
    }

    private Account createAccount(String accountNumber, String balance, AccountStatus status) {
        return Account.builder()
                .accountNumber(accountNumber)
                .balance(new BigDecimal(balance))
                .status(status)
                .version(0L)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private String generateValidAccountNumber(String countryCode, String currencyCode) {
        Random random = new Random();
        return String.format("%s%010d%s%c",
                countryCode,                                // 2 letters
                random.nextInt(1_000_000_000),       // 10 digits
                currencyCode,                              // 3 letters
                (char) (random.nextInt(26) + 'A')   // 1 random letter
        );
    }
}
