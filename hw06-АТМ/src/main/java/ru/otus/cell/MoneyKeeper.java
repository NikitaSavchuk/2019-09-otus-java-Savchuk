package ru.otus.cell;

import ru.otus.BanknotePar;
import ru.otus.atmException.CellIsFullException;
import ru.otus.atmException.CellOutOfAmountException;

import java.util.Map;

public interface MoneyKeeper {

    void deposit(BanknotePar banknoteParValue, int banknotesAmount) throws CellIsFullException, CellOutOfAmountException;

    Map<BanknotePar, Integer> withdraw(int cashAmount) throws CellOutOfAmountException;
}
