package ru.otus.bankomat;

import ru.otus.BanknotePar;
import ru.otus.cell.Cell;

import java.util.Map;

public interface ATM {

    void deposit(BanknotePar banknotePar, int banknotesAmount);

    Map<BanknotePar, Integer> withdraw(int cashAmount);

    int getAtmCashBalance();

    void printCell();

    void setStartState();

    void restoreStartState();

    String getId();

    void showCurrentAtmCashBalance();

    Cell getCell();
}
