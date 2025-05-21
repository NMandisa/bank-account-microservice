package za.co.mkhungo.bank.helper;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author Noxolo.Mkhungo
 */
@Component
@Scope("prototype")
public final class MessageLookup {

    private static  MessageSource messageSource ;

    private MessageLookup () {}// should no be instantiated obviously

    public static String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
