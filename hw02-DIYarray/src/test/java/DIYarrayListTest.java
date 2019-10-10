import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Collections;
import java.util.List;

public class DIYarrayListTest {
    private Integer[] integers = {7, 2, 3, 13, 5, 6, 1, 8, 9, 16, 11, 12, 4, 14, 15, 16, 10, 18, 19, 20, 65, 31, 36, 29};

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
        System.out.println("Первый список :" + firstList);
        Collections.copy(secondList, firstList);
        System.out.println("Второй список :" + secondList);
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
