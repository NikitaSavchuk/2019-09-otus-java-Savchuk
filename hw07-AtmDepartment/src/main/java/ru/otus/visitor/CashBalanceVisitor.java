package ru.otus.visitor;

import ru.otus.bankomat.ATM;

public class CashBalanceVisitor implements Visitor<Integer> {

    @Override
    public Integer visit(ATM atm) {
        return atm.getAtmCashBalance();
    }
}
