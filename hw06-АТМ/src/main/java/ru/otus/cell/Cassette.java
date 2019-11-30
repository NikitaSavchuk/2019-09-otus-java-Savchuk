package ru.otus.cell;

import ru.otus.atmException.CellIsFullException;

public interface Cassette {

    boolean isCellIsFull();

    int getFreeSlots();

    void resetBanknoteAmount();

    int getCellBalance();

    void saveBanknotesAmount();

    void addBanknotesAmount(int banknotesAmount)  throws CellIsFullException;

    void extractBanknotesAmount();
}
