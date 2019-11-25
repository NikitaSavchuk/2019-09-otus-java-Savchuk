package ru.otus.cell;

import lombok.Getter;
import lombok.Setter;
import ru.otus.BanknotePar;
import ru.otus.atmException.CellOutOfAmountException;

class AtmCell {
    private final int MAX_CAPACITY = 1000;
    @Getter
    private final BanknotePar BANKNOTE_VALUE;

    @Setter @Getter
    private int banknotesAmount;
    @Setter @Getter
    private int savedBanknoteAmount;

    AtmCell(BanknotePar banknoteValue, int banknotesAmount)  throws CellOutOfAmountException {
       if (banknotesAmount < 0 || banknotesAmount >= MAX_CAPACITY) throw new CellOutOfAmountException("Недопустимое значение!");
        this.banknotesAmount = banknotesAmount;
        this.BANKNOTE_VALUE = banknoteValue;
    }

    boolean isCassetteIsFull() {
        return this.banknotesAmount >= MAX_CAPACITY;
    }

    int getFreeSlots() {
        return MAX_CAPACITY - banknotesAmount;
    }

    void resetBanknoteAmount() {
        banknotesAmount = savedBanknoteAmount;
    }

    int getCassetteBalance() {
        return BANKNOTE_VALUE.getValue() * banknotesAmount;
    }

    void saveBanknotesAmount() {
        savedBanknoteAmount = banknotesAmount;
    }

    void decrementBanknotesAmount() {
        banknotesAmount--;
    }
}
