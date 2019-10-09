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
        print(list);
    }

    private void print(List<Integer> list) {
        for (Integer srt : list) {
            System.out.print(srt + " ");
        }
    }

    @Test
    @DisplayName("Copy")
    public void copy() {
        List<Integer> firstList = new DIYarrayList<>();
        Collections.addAll(firstList, integers);
        List<Integer> secondList = new DIYarrayList<>(firstList.size());
        Collections.copy(secondList, firstList);
        Assert.assertEquals("Списки разные", firstList.size(), secondList.size());
    }

    @Test
    @DisplayName("Sort")
    public void sort() {
        List<Integer> list = new DIYarrayList<>();
        Collections.addAll(list, integers);
        Collections.sort(list);
        int listSize = list.size();
        print(list);
        for (int i = 0; i < listSize - 1; i++) {
            Assert.assertTrue(list.get(i) <= list.get(i + 1));
        }
    }
}
