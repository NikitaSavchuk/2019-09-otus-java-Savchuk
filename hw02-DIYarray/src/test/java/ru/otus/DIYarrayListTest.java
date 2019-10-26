package ru.otus;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Collections;
import java.util.List;

public class DIYarrayListTest {
    private Integer[] integers = {7, 2, 3, 13, 5, 6, 1, 8, 9, 16, 11, 12, 4, 14, 15, 16, 10, 18, 19, 20, 65, 31, 36, 29};
    private Integer[] integers2 = {99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83, 82, 81, 80, 79, 78, 75, 70};

    @Test
    @DisplayName("Add all")
    public void addAll() {
        System.out.println("Add All:");
        List<Integer> list = new DIYarrayList<>();
        boolean result = Collections.addAll(list, integers);
        Assert.assertTrue(result);
        System.out.println("Список: " + list);
    }

    @Test
    @DisplayName("Copy")
    public void copy() {
        System.out.println("Copy:");
        List<Integer> firstList = new DIYarrayList<>();
        Collections.addAll(firstList, integers);
        List<Integer> secondList = new DIYarrayList<>(firstList.size());
        Collections.addAll(secondList, integers2);
        System.out.println("Первый список :" + firstList);
        System.out.println("Второй список  до copy  :" + secondList);
        Collections.copy(secondList, firstList);
        System.out.println("Второй список после copy:" + secondList);
        Assert.assertEquals("Списки разные", firstList.size(), secondList.size());
    }

    @Test
    @DisplayName("Sort")
    public void sort() {
        System.out.println("Sort:");
        List<Integer> list = new DIYarrayList<>();
        Collections.addAll(list, integers);
        System.out.println("Список после добавления :" + list);
        Collections.sort(list);
        System.out.println("Список после сортировки :" + list);
        int listSize = list.size();
        for (int i = 0; i < listSize - 1; i++) {
            Assert.assertTrue(list.get(i) <= list.get(i + 1));
        }
    }
}
