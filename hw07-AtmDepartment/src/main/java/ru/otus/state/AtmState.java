package ru.otus.state;

import ru.otus.bankomat.ATM;

import java.util.Optional;

public interface AtmState {

    public ATM getSavedState();

    public Optional<ATM> createCopy(ATM atm);

}
