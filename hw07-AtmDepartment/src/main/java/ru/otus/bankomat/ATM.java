package ru.otus.bankomat;

import ru.otus.BanknotePar;
import ru.otus.strategies.WithdrawStrategy;
import ru.otus.visitor.Visitor;

import java.util.Map;

public interface ATM {

    void deposit(BanknotePar banknotePar, int banknotesAmount);

    Map<BanknotePar, Integer> withdraw(int cashAmount);

    int getAtmCashBalance();

    void printCell();

    void setStartState();

    void restoreStartState();

    <T> T accept(Visitor<T> visitor);

    WithdrawStrategy getWithdrawStrategy();

    void setWithdrawStrategy(WithdrawStrategy withdrawStrategy);

    String getId();

    void showCurrentAtmCashBalance();
}
