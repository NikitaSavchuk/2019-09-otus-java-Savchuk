package ru.otus.cell;

import ru.otus.BanknotePar;
import ru.otus.atmException.CellOutOfAmountException;

public class AtmCell {
    private final int MAX_CAPACITY = 1000;
    private final BanknotePar BANKNOTE_VALUE;

    private int banknotesAmount;
    private int savedBanknoteAmount;

    AtmCell(BanknotePar banknoteValue, int banknotesAmount)  throws CellOutOfAmountException {
       if (banknotesAmount < 0 || banknotesAmount >= MAX_CAPACITY) throw new CellOutOfAmountException("Недопустимое значение!");
        this.banknotesAmount = banknotesAmount;
        this.BANKNOTE_VALUE = banknoteValue;
    }

    @Override
    public String toString() {
        return "Номинал кассеты " + BANKNOTE_VALUE + " Количество банкнот " + banknotesAmount;
    }

    public BanknotePar getBANKNOTE_VALUE() {
        return BANKNOTE_VALUE;
    }

    public int getBanknotesAmount() {
        return banknotesAmount;
    }

    public void setBanknotesAmount(int banknotesAmount) {
        this.banknotesAmount = banknotesAmount;
    }

    boolean isCassetteIsFull() {
        return banknotesAmount >= MAX_CAPACITY;
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
