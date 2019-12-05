package ru.otus.visitor;

import ru.otus.bankomat.ATM;
import ru.otus.cell.Cell;

public class CashBalanceVisitor implements Visitor<Integer> {

    @Override
    public int visit(ATM atm) {
        return visit(atm.getCell());
    }

    @Override
    public Integer visit(Cell cell) {
        return cell.getCellBalance();
    }
}
