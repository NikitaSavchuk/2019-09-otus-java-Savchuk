package ru.otus.messagesystem;

public abstract class Message<T extends Addressee> {
    private final Address from;
    private final Address to;

    protected Message(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    public Address getFrom() {
        return from;
    }

    Address getTo() {
        return to;
    }

    public abstract void exec(T addressee);
}
