import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Collections;
import java.util.List;

public class DIYarrayListTest {
    private Integer[] integers = {65, 31, 36, 29};

    private void addToTheList(List<Integer> list) {
        for (int i = 0; i < 25; i++) {
            list.add(i);
        }
    }

    @Test
    @DisplayName("Add all")
    public void addAll() {
        System.out.println("\nAdd All:");
        List<Integer> list = new DIYarrayList<>();

        addToTheList(list);

        System.out.println("Перед добавлением :" + list);
        Collections.addAll(list, integers);
        System.out.println("После добавлением :" + list);
    }

    @Test
    @DisplayName("Copy")
    public void copy() {
        System.out.println("\nCopy:");
        List<Integer> firstList = new DIYarrayList<>();

        addToTheList(firstList);

        List<Integer> secondList = new DIYarrayList<>(firstList.size());
        System.out.println("Первый список :" + firstList);
        Collections.copy(secondList, firstList);
        System.out.println("Второй список :" + secondList);
        Assert.assertEquals("Списка разные", firstList, secondList);
    }

    @Test
    @DisplayName("Sort")
    public void sort() {
        System.out.println("\nSort:");
        List<Integer> list = new DIYarrayList<>();

        addToTheList(list);

        System.out.println("Список до добавления :" + list);
        Collections.addAll(list, integers);
        System.out.println("Список после добавления :" + list);
        Collections.sort(list);
        System.out.println("Список после сортировки :" + list);
    }
}
