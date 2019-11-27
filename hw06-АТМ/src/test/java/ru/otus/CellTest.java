package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.atmException.CellIsFullException;
import ru.otus.atmException.CellOutOfAmountException;
import ru.otus.cell.Cell;

import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

    private Cell cell;

    @BeforeEach
    void setUp() {
        cell = new Cell();
    }

    @Test
    void depositThrowsCellIsFullException() {
        assertThrows(CellIsFullException.class, () -> {
            cell.deposit(BanknotePar.ONE_THOUSAND, 1000);
        });
    }

    @Test
    void withdrawException() {
        assertThrows(CellOutOfAmountException.class, () -> {
            cell.withdraw(500_000);
        });
    }
}
