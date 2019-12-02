package ru.otus.visitor;

import ru.otus.bankomat.ATM;

public interface Visitor<T> {
    T visit(ATM atm);
}
