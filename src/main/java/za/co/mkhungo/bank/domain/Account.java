package za.co.mkhungo.bank.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import za.co.mkhungo.bank.domain.enums.AccountStatus;
import za.co.mkhungo.bank.exception.InsufficientFundsException;
import za.co.mkhungo.bank.exception.InvalidAmountException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Noxolo.Mkhungo
 */
@Entity
@Table(name = "accounts",
        indexes = {
                @Index(name = "idx_account_number", columnList = "account_number", unique = true),
                //@Index(name = "idx_customer_id", columnList = "customer_id")
                //Customer has an account but that a whole domain ect...Nope,Not now!!!
        })
@Cacheable
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_sequence", allocationSize = 50)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true, length = 24)
    @Pattern(regexp = "^[A-Z]{2}\\d{10}[A-Z]{4}$", message = "Invalid account number format")
    private String accountNumber;

    @Column(nullable = false, precision = 19, scale = 2)
    @PositiveOrZero(message = "Balance must be positive or zero")
    @Digits(integer = 16, fraction = 2, message = "Invalid balance format")
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus status = AccountStatus.ACTIVE;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id", referencedColumnName = "id") // avoid bidirectional link
    @BatchSize(size = 20)
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private List<Transact> transactions;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @BatchSize(size = 20)
    @ToString.Exclude
    private List<AccountAudit> auditLogs = new ArrayList<>();

    @Version
    private Long version;  // Optimistic locking

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // JPA Callback Methods
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Domain-Driven Validation
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
        this.balance = balance.subtract(amount);
    }

    public void addTransaction(Transact transaction) {
        this.transactions.add(transaction);
    }

    public void addAuditLog(AccountAudit auditLog) {
        this.auditLogs.add(auditLog);
    }

    @Override
    public int hashCode() {return new HashCodeBuilder().append(id).toHashCode();}
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Account other)) return false;
        return new EqualsBuilder().append(id, other.id).isEquals();
    }
}
