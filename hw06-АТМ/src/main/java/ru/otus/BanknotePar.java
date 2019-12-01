package ru.otus;

import lombok.Getter;

public enum BanknotePar {

    FIFTY(50),
    ONE_HUNDRED(100),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000),
    THREE_THOUSAND(3000),
    FIVE_THOUSAND(5000);

    @Getter
    private final int nominal;

    BanknotePar(int nominal) {
        this.nominal = nominal;
    }

    @Override
    public String toString() {
        return String.valueOf(nominal);
    }
}
