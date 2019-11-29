package ru.otus.cell;

public interface Cassette {

    boolean isCellIsFull();

    int getFreeSlots();

    void resetBanknoteAmount();

    int getCellBalance();

    void saveBanknotesAmount();

    void decrementBanknotesAmount();
}
