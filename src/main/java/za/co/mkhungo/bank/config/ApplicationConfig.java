package za.co.mkhungo.bank.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

/**
 * @author Noxolo.Mkhungo
 */
@EnableAsync
@Configuration
@PropertySource("classpath:locale/base.properties")
public class ApplicationConfig {

    @Value("${aws.region}")
    private String region;

    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder()
                .region(Region.of(region)) // region must be defined
                .credentialsProvider(DefaultCredentialsProvider.create())
                .overrideConfiguration(this::snsClientRetryConfig)
                .build();
    }

    private ClientOverrideConfiguration snsClientRetryConfig(ClientOverrideConfiguration.Builder builder) {
        return builder.retryPolicy(disableSdkRetriesPolicy()).build();
    }

    private RetryPolicy disableSdkRetriesPolicy() {
        return RetryPolicy.builder()
                .numRetries(0)  // Disable SDK retries; Resilience4j manages retries instead
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:locale/base");
        source.setCacheSeconds(3600); // 1 hour cache
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(LocaleContextHolder.getLocale());
        return slr;
    }
}
