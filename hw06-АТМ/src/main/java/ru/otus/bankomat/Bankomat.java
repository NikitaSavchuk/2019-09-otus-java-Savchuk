package ru.otus.bankomat;

import ru.otus.BanknotePar;
import ru.otus.atmException.CellIsFullException;
import ru.otus.atmException.CellOutOfAmountException;
import ru.otus.cell.Cell;

import java.util.Collections;
import java.util.Map;

public class Bankomat implements ATM {
    private final Cell CELL = new Cell();

    @Override
    public void deposit(BanknotePar banknoteParValue, int banknotesAmount) {
        try {
            CELL.deposit(banknoteParValue, banknotesAmount);
            System.out.println("Внесено: " + banknoteParValue.getNominal() * banknotesAmount);
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

    @Override
    public void printCell() {
        CELL.getCell().values().forEach(System.out::println);
    }
}
