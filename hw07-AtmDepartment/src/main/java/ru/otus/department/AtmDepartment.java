package ru.otus.department;

import ru.otus.bankomat.ATM;

public interface AtmDepartment {

    void addAtm(ATM atm);

    int getTotalCashAmount();

    void showTotalCashAmount();

    void showAllAtms();

    void restoreAllAtmsToStartState();
}
