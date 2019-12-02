package ru.otus.cell;


import ru.otus.BanknotePar;
import ru.otus.atmException.CellIsFullException;
import ru.otus.atmException.CellOutOfAmountException;

import java.io.Serializable;
import java.util.*;

import static java.lang.String.format;

public class Cell implements MoneyKeeper, Serializable {

    private final Map<Integer, AtmCell> CELL = new TreeMap<>();

    public Cell() {
        try {
            for (BanknotePar banknoteValue : BanknotePar.values()) {
                CELL.put(banknoteValue.getNominal(), new AtmCell(banknoteValue, 50));
            }
        } catch (CellOutOfAmountException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deposit(BanknotePar banknoteParValue, int banknotesAmount) throws CellIsFullException, CellOutOfAmountException {
        if (banknoteParValue == null || banknotesAmount <= 0) {
            throw new CellOutOfAmountException(format("Некорретно указана сумма '%s' или номинал '%s'", banknotesAmount, banknoteParValue));
        }
        AtmCell cell = CELL.get(banknoteParValue.getNominal());

        cell.addBanknotesAmount(banknotesAmount);
    }

    public Map<Integer, AtmCell> getCell() {
        return CELL;
    }

    public int getCellBalance() {
        return CELL.values().stream().mapToInt(Cassette::getCellBalance).sum();
    }
}
