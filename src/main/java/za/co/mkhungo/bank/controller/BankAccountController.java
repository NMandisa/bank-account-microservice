package za.co.mkhungo.bank.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.mkhungo.bank.response.ApiResponse;
import za.co.mkhungo.bank.response.AccountSummaryResponse;
import za.co.mkhungo.bank.service.AccountService;

import java.math.BigDecimal;

/**
 * @author Noxolo.Mkhungo
 */
@Slf4j
@RestController
@RequestMapping("/bank")
@AllArgsConstructor
public class BankAccountController {

    private final AccountService accountService;

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<AccountSummaryResponse>> withdraw(@RequestParam("accountId") Long accountId,
                                                                        @RequestParam("amount") BigDecimal amount) {
        //Depending on the integration expectations on the response and business rules
        //Error handled by by GlobalExceptionHandler
        //TODO
        accountService.withdraw(accountId, amount);
        log.info("Withdrawal ....");//Whatever relevant data but not confidential
        return ResponseEntity.ok(ApiResponse.success(
                new AccountSummaryResponse(accountId, amount,
                        "Withdrawal processed successfully")
        ));

    }
}
