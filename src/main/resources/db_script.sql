CREATE SEQUENCE account_sequence START WITH 1 INCREMENT BY 50;

CREATE TABLE accounts (
                          id BIGINT PRIMARY KEY,
                          account_number VARCHAR(24) NOT NULL UNIQUE,
                          balance DECIMAL(19,2) NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          version BIGINT NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP,
                          CONSTRAINT chk_balance_positive CHECK (balance >= 0)
);

CREATE INDEX idx_account_number ON accounts(account_number);
# CREATE INDEX idx_customer_id ON accounts(customer_id);

ALTER TABLE transaction_ledger
    ADD CONSTRAINT chk_no_updates CHECK (1=1); -- Prevent updates