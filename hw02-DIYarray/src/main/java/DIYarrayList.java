import java.util.*;

public class DIYarrayList<T> implements List<T> {

    private final int DEFAULT_CAPACITY = 25;
    private Object[] values;
    private int capacity;

    DIYarrayList(int capacity) {
        this.capacity = capacity;
        values = new Object[capacity];
    }

    DIYarrayList() {
        this.capacity = 0;
        values = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return capacity;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(values, values.length);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        if (capacity + 1 > values.length)
            values = Arrays.copyOf(values, values.length * 2);
        values[capacity] = t;
        capacity = capacity + 1;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DIYarrayList<?> that = (DIYarrayList<?>) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public T get(int index) {
        if ((index < 0) || (index >= capacity)) throw new NoSuchElementException();
        return (T) values[index];
    }

    @Override
    public T set(int index, T element) {
        T oldValues = (T) values[index];
        values[index] = element;
        return oldValues;
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new DIYarrayListIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    public void sort(Comparator<? super T> c) {
        Arrays.sort((T[]) values, 0, capacity, c);
    }

    @Override
    public String toString() {
        return "DIYarrayList{" +
                "values=" + Arrays.toString(values) +
                ", capacity=" + capacity +
                '}';
    }

    private class DIYarrayListIterator implements ListIterator<T> {

        private int nextElement;
        private int lastReturned;

        DIYarrayListIterator(int index) {
            nextElement = index;
        }

        @Override
        public boolean hasNext() {
            return nextElement != capacity;
        }

        @Override
        public T next() {
            int i = nextElement;
            if (i >= capacity)
                throw new NoSuchElementException();
            Object[] values = DIYarrayList.this.values;
            nextElement = i + 1;
            return (T) values[lastReturned = i];
        }

        @Override
        public boolean hasPrevious() {
            return nextElement != 0;
        }

        @Override
        public T previous() {
            int i = nextElement - 1;
            nextElement = i;
            return (T) DIYarrayList.this.values[lastReturned = i];
        }

        @Override
        public int nextIndex() {
            return nextElement;
        }

        @Override
        public int previousIndex() {
            return nextElement - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            DIYarrayList.this.set(lastReturned, t);
        }

        @Override
        public void add(T t) {
            DIYarrayList.this.add(t);
            nextElement += 1;
            lastReturned = -1;
        }
    }
}
