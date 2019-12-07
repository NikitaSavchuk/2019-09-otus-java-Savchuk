package ru.otus.cell;



import ru.otus.BanknotePar;
import ru.otus.atmException.CellIsFullException;

import java.util.Map;

public interface Cassette {

    boolean isCellIsFull();

    int getFreeSlots();

    void resetBanknoteAmount();

    int getCellBalance();

    void saveBanknotesAmount();

    void addBanknotesAmount(int banknotesAmount)  throws CellIsFullException;

    void extractBanknotesAmount();

    int extractCashSum(Map<BanknotePar, Integer> cashMap, int cashSum);
}
