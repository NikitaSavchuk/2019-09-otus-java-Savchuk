package ru.otus.cell;

import ru.otus.BanknotePar;
import ru.otus.atmException.CellIsFullException;
import ru.otus.atmException.CellOutOfAmountException;

import java.util.*;

import static java.lang.String.format;

public class Cell implements MoneyKeeper {
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

    @Override
    public Map<BanknotePar, Integer> withdraw(int cashAmount) throws CellOutOfAmountException {
        if (cashAmount > getCellBalance()) {
            throw new CellOutOfAmountException(format("Недостаточно средств для снятия, запрошено %s , текущий баланс %s", cashAmount, getCellBalance()));
        } else if (cashAmount <= 0) {
            System.out.println("Введено недопустимое значение: " + cashAmount);
            return Collections.emptyMap();
        }
        return getBanknoteParMap(cashAmount);
    }

    private Map<BanknotePar, Integer> getBanknoteParMap(int cashAmount) {
        Map<BanknotePar, Integer> cashMap = new HashMap<>();
        Map<Integer, AtmCell> sortedCell = new TreeMap<>(Comparator.reverseOrder());
        sortedCell.putAll(CELL);
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

    public Map<Integer, AtmCell> getCell() {
        return CELL;
    }

    public int getCellBalance() {
        return CELL.values().stream().mapToInt(Cassette::getCellBalance).sum();
    }
}
