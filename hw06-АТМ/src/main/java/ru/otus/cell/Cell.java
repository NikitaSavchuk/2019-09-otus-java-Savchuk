package ru.otus.cell;

import lombok.Getter;
import ru.otus.BanknotePar;
import ru.otus.atmException.CellIsFullException;
import ru.otus.atmException.CellOutOfAmountException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.String.format;

public class Cell  implements MoneyKeeper {
    @Getter
    private final Map<Integer, AtmCell> CELL = new TreeMap<>();

    public Cell() {
        try {
            for (BanknotePar banknoteValue : BanknotePar.values()) {
                CELL.put(banknoteValue.getValue(), new AtmCell(banknoteValue, 50));
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
        AtmCell cell = CELL.get(banknoteParValue.getValue());

        if (cell.isCassetteIsFull() || cell.getFreeSlots() < banknotesAmount) {
            throw new CellIsFullException(format("Ячейка переполнена, нельзя внести больше, свободных ячеек %s, а вносится %s", cell.getFreeSlots(), banknotesAmount));
        }
        cell.setBanknotesAmount(cell.getBanknotesAmount() + banknotesAmount);
    }

    @Override
    public Map<BanknotePar, Integer> withdraw(int cashAmount) throws CellOutOfAmountException {
        if (cashAmount > getCellBalance()) {
            throw new CellOutOfAmountException(format("Недостаточно средств для снятия, запрошено %s , текущий баланс %s", cashAmount, getCellBalance()));
        } else if (cashAmount <= 0) {
            System.out.println("Введено недопустимое значение: " + cashAmount);
            return Collections.emptyMap();
        }
        Map<BanknotePar, Integer> cashMap = new HashMap<>();
        Map<Integer, AtmCell> sortedCell = new TreeMap<>(CELL);
        int cashSum = cashAmount;

        for (AtmCell cassette : sortedCell.values()) {
            BanknotePar banknotePar = cassette.getBANKNOTE_VALUE();
            cassette.saveBanknotesAmount();

            while (cashSum > 0 && banknotePar.getValue() <= cashSum && cassette.getBanknotesAmount() > 0) {
                int num = cashMap.getOrDefault(banknotePar, 0);
                cashMap.put(banknotePar, num + 1);
                cashSum -= banknotePar.getValue();
                cassette.decrementBanknotesAmount();
            }
            if (cashSum == 0) {
                break;
            }
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

    public int getCellBalance() {
        int sum = 0;
        for (AtmCell atmCell : CELL.values()) {
            int freeSlots = atmCell.getCassetteBalance();
            sum += freeSlots;
        }
        return sum;
    }
}
