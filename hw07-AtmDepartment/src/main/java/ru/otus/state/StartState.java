package ru.otus.state;

import ru.otus.bankomat.ATM;

import java.io.*;
import java.util.Optional;

public class StartState implements AtmState {
    private ATM savedCopy;

    public StartState(ATM savedCopy) {
        this.savedCopy = createCopy(savedCopy).orElseThrow(() -> new RuntimeException("Не удалось создать копию"));
    }

    @Override
    public ATM getSavedState() {
        return createCopy(savedCopy).orElseThrow(() -> new RuntimeException("Не удалось создать копию"));
    }

    @Override
    public Optional<ATM> createCopy(ATM atm) {
        ATM atmCopy = null;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(atm);
            byte[] bytes = outputStream.toByteArray();

            try (ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
                atmCopy = (ATM) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(atmCopy);
    }
}
