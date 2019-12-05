package ru.otus.cell;

import ru.otus.BanknotePar;
import ru.otus.atmException.CellIsFullException;
import ru.otus.atmException.CellOutOfAmountException;

public interface MoneyKeeper {

    void deposit(BanknotePar banknoteParValue, int banknotesAmount) throws CellIsFullException, CellOutOfAmountException;
}
