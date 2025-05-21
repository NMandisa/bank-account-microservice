package za.co.mkhungo.bank.utils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import za.co.mkhungo.bank.domain.Account;

/**
 * @author Noxolo.Mkhungo
 */
@Component
@Scope("prototype")
public final class AccountValidationUtils {

    private AccountValidationUtils() {} //b/c Utility classes should not have instances

    //1. check null
    public static void validateAccountId(Long accountId) {
        //TODO
    }
    //2. check account status
    public static void validateAccountStatus(Account account) {
        //TODO
    }
}
