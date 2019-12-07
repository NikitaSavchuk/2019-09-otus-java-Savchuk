package ru.otus.visitor;

public interface Element {

    <T> int accept(Visitor<T> visitor);
}
