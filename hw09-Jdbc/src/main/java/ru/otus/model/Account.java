package ru.otus.model;

import lombok.*;
import ru.otus.annotations.Id;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private long no;
    private String type;
    private BigDecimal rest;

    public Account(String type, BigDecimal rest) {
        this.type = type;
        this.rest = rest;
    }
}


