package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.bankomat.ATM;
import ru.otus.department.Department;

import java.util.Optional;

public class DepartmentTest {

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
    }

    @Test
    void depositPositive() {
        department.getAtmList().forEach(System.out::println);
        department.showTotalCashAmount();

        Optional<ATM> optionalATM1 = department.getAtm("atm №1");
        ATM atm1 = optionalATM1.orElseThrow(() -> new RuntimeException("Такого банкомата нет"));

        showTotalCashAndRestore();

        atm1.deposit(BanknotePar.ONE_HUNDRED, 700);

        showTotalCashAndRestore();

        department.showAllAtms();
    }

    @Test
    void giveCashPositive() {
        department.getAtmList().forEach(System.out::println);
        department.showTotalCashAmount();

        Optional<ATM> optionalATM2 = department.getAtm("atm №2");
        optionalATM2.ifPresentOrElse(atm -> atm.withdraw(395_000), () -> System.out.println("Такого банкомата нет"));

        showTotalCashAndRestore();

        department.showAllAtms();
    }

    private void showTotalCashAndRestore() {
        department.showTotalCashAmount();
        department.restoreAllAtmsToStartState();
        department.showTotalCashAmount();
    }

}
