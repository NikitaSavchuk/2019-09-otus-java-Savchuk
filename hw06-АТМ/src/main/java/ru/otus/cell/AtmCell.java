package ru.otus.cell;

import ru.otus.BanknotePar;
import ru.otus.atmException.CellIsFullException;
import ru.otus.atmException.CellOutOfAmountException;

import static java.lang.String.format;

public class AtmCell implements Cassette {
    private final int MAX_CAPACITY = 1000;
    private final BanknotePar BANKNOTE_VALUE;

    private int banknotesAmount;
    private int savedBanknoteAmount;

    AtmCell(BanknotePar banknoteValue, int banknotesAmount) throws CellOutOfAmountException {
        if (banknotesAmount < 0 || banknotesAmount >= MAX_CAPACITY)
            throw new CellOutOfAmountException("Недопустимое значение!");
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

    public void changeBanknotesAmount(int banknotesAmount) throws CellIsFullException {
        if (banknotesAmount > 0) {
            if (isCellIsFull() || getFreeSlots() < banknotesAmount) {
                throw new CellIsFullException(format("Ячейка переполнена, нельзя внести больше, свободных ячеек %s, а вносится %s"
                        , getFreeSlots(), banknotesAmount));
            } else {
                this.banknotesAmount += banknotesAmount;
            }
        } else {
            throw new IllegalArgumentException("Недопустимое значение!");
        }
    }

    @Override
    public boolean isCellIsFull() {
        return banknotesAmount >= MAX_CAPACITY;
    }

    @Override
    public int getFreeSlots() {
        return MAX_CAPACITY - banknotesAmount;
    }

    @Override
    public void resetBanknoteAmount() {
        banknotesAmount = savedBanknoteAmount;
    }

    @Override
    public int getCellBalance() {
        return BANKNOTE_VALUE.getValue() * banknotesAmount;
    }

    @Override
    public void saveBanknotesAmount() {
        savedBanknoteAmount = banknotesAmount;
    }

    @Override
    public void decrementBanknotesAmount() {
        banknotesAmount--;
    }
}
