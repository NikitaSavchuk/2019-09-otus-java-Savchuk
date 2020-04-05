package ru.otus.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@EqualsAndHashCode(exclude = "id")
public class AddressDataSet {
    @Id
    @GeneratedValue
    @Column(name = "ADDRESS_ID")
    private Long id;
    private String street;
}
