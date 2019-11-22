package ru.otus;

import ru.otus.atmException.CellIsFullException;
import ru.otus.atmException.CellOutOfAmountException;

import java.util.Collections;
import java.util.Map;

public class Bankomat implements ATM {
    private final Cell CELL = new Cell();

    @Override
    public void deposit(BanknotePar banknoteParValue, int banknotesAmount) {
        try {
            CELL.deposit(banknoteParValue, banknotesAmount);
            System.out.println("Внесено: " + banknoteParValue.getValue() * banknotesAmount);
        } catch (CellOutOfAmountException | CellIsFullException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<BanknotePar, Integer> withdraw(int cashAmount) {
        try {
            return CELL.withdraw(cashAmount);
        } catch (CellOutOfAmountException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    @Override
    public int getAtmCashBalance() {
        int cassettesBalance = CELL.getCellBalance();
        System.out.println("Текущий баланс: " + cassettesBalance);
        return cassettesBalance;
    }
}
