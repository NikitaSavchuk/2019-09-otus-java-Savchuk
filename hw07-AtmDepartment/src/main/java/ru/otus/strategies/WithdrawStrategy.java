package ru.otus.strategies;

import ru.otus.BanknotePar;
import ru.otus.atmException.CellOutOfAmountException;
import ru.otus.cell.AtmCell;
import ru.otus.cell.Cell;

import java.io.Serializable;
import java.util.*;

import static java.lang.String.format;

public class WithdrawStrategy implements Strategy, Serializable {

    private Cell cell;

    public Strategy clone() throws CloneNotSupportedException {
        return (Strategy) super.clone();
    }

    @Override
    public Map<BanknotePar, Integer> withdraw(Cell cell, int cashAmount) throws CellOutOfAmountException {
        if (cashAmount > cell.getCellBalance()) {
            throw new CellOutOfAmountException(format("Недостаточно средств для снятия, запрошено %s , текущий баланс %s", cashAmount, cell.getCellBalance()));
        } else if (cashAmount <= 0) {
            System.out.println("Введено недопустимое значение: " + cashAmount);
            return Collections.emptyMap();
        }
        this.cell = cell;
        return getBanknoteParMap(cashAmount);
    }

    private Map<BanknotePar, Integer> getBanknoteParMap(int cashAmount) {
        Map<BanknotePar, Integer> cashMap = new HashMap<>();
        Map<Integer, AtmCell> sortedCell = new TreeMap<>(Comparator.reverseOrder());
        sortedCell.putAll(cell.getCell());
        int cashSum = cashAmount;

        for (AtmCell cell : sortedCell.values()) {
            cashSum = cell.extractCashSum(cashMap, cashSum);
            if (cashSum == 0) break;
        }
        if (cashSum > 0) {
            sortedCell.values().forEach(AtmCell::resetBanknoteAmount);
            System.out.println("Некорретная сумма: " + cashAmount);
            return Collections.emptyMap();
        }
        System.out.println("Выдано: " + cashAmount);
        cashMap.forEach((banknotePar, amount) -> System.out.println(format("Номинал банкноты '%s', количество '%s'", banknotePar, amount)));

        return cashMap;
    }
}
