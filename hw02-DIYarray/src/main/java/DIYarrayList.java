import java.util.*;

public class DIYarrayList<T> implements List<T> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] values;
    private int size;

    DIYarrayList(int capacity) {
        size = capacity;
        values = new Object[capacity];
    }

    DIYarrayList() {
        size = 0;
        values = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return size;
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
        if (size + 1 > values.length)
            values = Arrays.copyOf(values, values.length * 2);
        values[size] = t;
        size = size + 1;
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
        checkIndex((index < 0) || (index >= size));
        return (T) values[index];
    }

    private void checkIndex(boolean b) {
        if (b) throw new NoSuchElementException();
    }

    @Override
    public T set(int index, T element) {
        checkIndex(index < 0);
        T oldValues = (T) values[index];
        values[index] = element;
        return oldValues;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < size - 1; i++) {
            result.append(values[i]).append(", ");
        }
        return result.append(values[size - 1]).append(']').toString();
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
        Arrays.sort((T[]) values, 0, size, c);
    }

    private class DIYarrayListIterator implements ListIterator<T> {

        private int nextElement;
        private int lastReturned;

        DIYarrayListIterator(int index) {
            nextElement = index;
        }

        @Override
        public boolean hasNext() {
            return nextElement != size;
        }

        @Override
        public T next() {
            int i = nextElement;
            checkIndex(i >= size);
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
            checkIndex(i < 0);
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
            if (nextElement < 0)
                throw new IllegalStateException();
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
