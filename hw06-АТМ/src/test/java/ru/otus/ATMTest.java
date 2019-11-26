package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.bankomat.ATM;
import ru.otus.bankomat.Bankomat;

import java.util.Map;

import static org.junit.Assert.*;

class ATMTest {

    private ATM atm;

    @BeforeEach
    void setUp() {
        atm = new Bankomat();
        atm.getAtmCashBalance();
        atm.printCell();
//        new balance = 482500
    }

    @Test
    void depositCashPositive() {
        atm.deposit(BanknotePar.FIVE_HUNDRED, 500);
        assertEquals("Баланас банкомата не соответствует", 732_500, atm.getAtmCashBalance());
    }

    @Test
    void giveCashPositive() {
        atm.withdraw(82500);
        assertEquals("Баланас банкомата не соответствует", 400_000, atm.getAtmCashBalance());
    }

    @Test
    void giveCashNegative() {
        atm.withdraw(17000);
        assertNotEquals("Баланас банкомата не соответствует", 500_000, (atm.getAtmCashBalance()));
    }

    @Test
    void giveCashAll() {
        Map<BanknotePar, Integer> withdraw = atm.withdraw(482_500);
//        withdraw.forEach((k, v) -> System.out.println(k + " - " + v));
        assertEquals("Баланас банкомата не соответствует", 0, atm.getAtmCashBalance());
    }


    @Test
    void giveCashMoreCurrent() {
        atm.withdraw(483_000);
    }

    @Test
    void giveCashMinus() {
        atm.withdraw(-1);
    }

    @Test
    void depositCashFullException() {
        atm.deposit(BanknotePar.FIVE_HUNDRED, 1000);
    }
}
