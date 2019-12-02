package ru.otus.bankomat;


import ru.otus.BanknotePar;
import ru.otus.state.StartState;
import ru.otus.strategies.WithdrawStrategy;
import ru.otus.visitor.Visitor;
import ru.otus.atmException.CellIsFullException;
import ru.otus.atmException.CellOutOfAmountException;
import ru.otus.cell.Cell;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static java.lang.String.format;

public class Bankomat implements ATM, Serializable {
    private Cell CELL = new Cell();
    private WithdrawStrategy withdrawStrategy;
    private String id;
    private StartState startState;

    public Bankomat(String id) {
        this.id = id;
        this.withdrawStrategy = new WithdrawStrategy();
        setStartState();
    }

    public Bankomat(String id, WithdrawStrategy withdrawStrategy) {
        this.id = id;
        this.withdrawStrategy = withdrawStrategy;
        setStartState();
    }

    @Override
    public void setStartState() {
        startState = new StartState(this);
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void restoreStartState() {
        Bankomat bankomat = (Bankomat) startState.getSavedState();
        CELL = bankomat.CELL;
        withdrawStrategy = bankomat.withdrawStrategy;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public WithdrawStrategy getWithdrawStrategy() {
        return withdrawStrategy;
    }

    @Override
    public void setWithdrawStrategy(WithdrawStrategy withdrawStrategy) {
        this.withdrawStrategy = withdrawStrategy;
    }

    public Cell getCELL() {
        return CELL;
    }

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
            return withdrawStrategy.withdraw(CELL, cashAmount);
        } catch (CellOutOfAmountException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    @Override
    public void showCurrentAtmCashBalance() {
        System.out.println(format("Текущий баланс в банкомате %s: %s", id, getAtmCashBalance()));
    }

    @Override
    public int getAtmCashBalance() {
        return CELL.getCellBalance();
    }

    @Override
    public void printCell() {
        CELL.getCell().values().forEach(System.out::println);
    }

    @Override
    public String toString() {
        return id;
    }
}
