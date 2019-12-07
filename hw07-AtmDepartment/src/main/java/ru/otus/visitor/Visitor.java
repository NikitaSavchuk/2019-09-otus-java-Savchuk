package ru.otus.visitor;

import ru.otus.bankomat.ATM;
import ru.otus.cell.Cell;

public interface Visitor<T> {
    int visit(ATM atm);
    T visit(Cell cell);
}
