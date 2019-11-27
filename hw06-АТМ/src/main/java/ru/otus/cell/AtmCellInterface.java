package ru.otus.cell;

public interface AtmCellInterface {

    boolean isCellIsFull();

    int getFreeSlots();

    void resetBanknoteAmount();

    int getCellBalance();

    void saveBanknotesAmount();

    void decrementBanknotesAmount();
}
