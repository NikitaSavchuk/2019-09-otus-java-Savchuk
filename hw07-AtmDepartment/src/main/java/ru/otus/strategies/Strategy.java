package ru.otus.strategies;

import ru.otus.BanknotePar;
import ru.otus.atmException.CellOutOfAmountException;
import ru.otus.cell.Cell;

import java.util.Map;

public interface Strategy {
    Map<BanknotePar, Integer> withdraw(Cell cell, int amount) throws CellOutOfAmountException;
}
