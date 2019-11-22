package ru.otus;

import java.util.Map;

public interface ATM {

    void deposit(BanknotePar banknotePar, int banknotesAmount);

    Map<BanknotePar, Integer> withdraw(int cashAmount);

    int getAtmCashBalance();
}
