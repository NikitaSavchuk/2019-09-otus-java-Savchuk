import java.util.*;

public class DIYarrayList<T> implements List<T> {

    private final int DEFAULT_SIZE = 25;
    private Object[] values;
    private int size;

    DIYarrayList(int size) {
        this.size = size;
        values = new Object[size];
    }

    DIYarrayList() {
        this.size = 0;
        values = new Object[DEFAULT_SIZE];
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
        try {
            if (size + 1 > values.length)
                values = Arrays.copyOf(values, size + 1);
            values[size] = t;
            size = size + 1;
            return true;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return false;
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
        Arrays.sort((T[]) values, 0, size, c);
    }

    @Override
    public String toString() {
        return "DIYarrayList{" +
                "values=" + Arrays.toString(values) +
                ", size=" + size +
                '}';
    }

    private class DIYarrayListIterator implements ListIterator<T> {

        private int val;
        int last;

        public DIYarrayListIterator(int index) {
            val = index;
        }

        @Override
        public boolean hasNext() {
            return val != size;
        }

        @Override
        public T next() {
            int i = val;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] values = DIYarrayList.this.values;
            val = i + 1;
            return (T) values[last = i];
        }

        @Override
        public boolean hasPrevious() {
            return val != 0;
        }

        @Override
        public T previous() {
            int i = val - 1;
            val = i;
            return (T) DIYarrayList.this.values[last = i];
        }

        @Override
        public int nextIndex() {
            return val;
        }

        @Override
        public int previousIndex() {
            return val - 1;
        }

        @Override
        public void remove() {
            try {
                DIYarrayList.this.remove(last);
                val = last;
                last = -1;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void set(T t) {
            DIYarrayList.this.set(last, t);
        }

        @Override
        public void add(T t) {
            DIYarrayList.this.add(t);
            val += 1;
            last = -1;
        }
    }
}
