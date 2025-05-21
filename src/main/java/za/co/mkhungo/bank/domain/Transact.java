package za.co.mkhungo.bank.domain;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;
import za.co.mkhungo.bank.domain.enums.TransactionStatus;
import za.co.mkhungo.bank.domain.enums.TransactionType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Noxolo.Mkhungo
 */
@Entity
@Table(name = "transaction_ledger")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Immutable
public class Transact implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, updatable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TransactionType type;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TransactionStatus status;
    private BigDecimal amount;
    private LocalDateTime time;

    public Transact(TransactionType type, TransactionStatus status,BigDecimal amount, LocalDateTime time) {
      this.type = type;
      this.status = status;
      this.time = time;
      this.amount = amount;
    }
}
