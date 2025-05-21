package za.co.mkhungo.bank.domain;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Noxolo.Mkhungo
 */
@Entity
@Table(name = "account_audit_log")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Immutable
public class AccountAudit implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String eventType; // WITHDRAWAL

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "account_id", updatable = false)
    private Account account;

    @Column(updatable = false)
    private String initiatedBy; // User/System

    public AccountAudit(String eventType, BigDecimal amount, LocalDateTime timestamp,String initiatedBy) {
        this.eventType = eventType;
        this.amount = amount;
        this.timestamp = timestamp;
        this.initiatedBy = initiatedBy;
    }

}
