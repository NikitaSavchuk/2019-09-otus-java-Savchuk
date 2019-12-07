package ru.otus;

import java.util.*;

public class Person {
    private String address;
    private int houseNumber;
    private int age;
    private String name;
    private int height;
    private int weight;
    private int[] intArray;
    private Map<Integer, String> map;
    private List<Byte> byteList;
    private String nullString = null;

    Person() {
        System.out.println("new Person");
        houseNumber = 84;
        address = "Савушкина, д 84";
        age = 30;
        name = "Василий";
        height = 176;
        weight = 75;
        intArray = new int[]{1, 2, 3, 4, 5, 6, 7};

        map = new HashMap<>();
        map.put(1, "Один");
        map.put(2, "Два");

        byteList = new ArrayList<>();
        byteList.add(Byte.valueOf("1"));
        byteList.add(Byte.valueOf("2"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (houseNumber != person.houseNumber) return false;
        if (age != person.age) return false;
        if (height != person.height) return false;
        if (weight != person.weight) return false;
        if (!Objects.equals(address, person.address)) return false;
        if (!Objects.equals(name, person.name)) return false;
        if (!Arrays.equals(intArray, person.intArray)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Objects.equals(map, person.map)) return false;
        if (!Objects.equals(byteList, person.byteList)) return false;
        return Objects.equals(nullString, person.nullString);
    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + houseNumber;
        result = 31 * result + age;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + height;
        result = 31 * result + weight;
        result = 31 * result + Arrays.hashCode(intArray);
        result = 31 * result + (map != null ? map.hashCode() : 0);
        result = 31 * result + (byteList != null ? byteList.hashCode() : 0);
        result = 31 * result + (nullString != null ? nullString.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "address='" + address + '\'' +
                ", houseNumber=" + houseNumber +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", intArray=" + Arrays.toString(intArray) +
                ", map=" + map +
                ", byteList=" + byteList +
                ", nullString='" + nullString + '\'' +
                '}';
    }
}