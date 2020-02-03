package ru.otus.cache;

import org.springframework.stereotype.Component;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.*;

@Component
public class MyCache<K, V> implements HwCache<K, V>, HwListener<K, V> {

    private Map<K, V> cache = new WeakHashMap<>();
    private List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();
    private ReferenceQueue<HwListener<K, V>> listenerReferenceQueue = new ReferenceQueue<>();

    @Override
    public void put(K key, V value) {
        cleanUp();
        cache.put(key, value);
        notify(key, value, "Entity is cached");
        notifyAllListeners(key, value, "Entity is cached");
    }

    @Override
    public void remove(K key) {
        cleanUp();
        V oldValue = cache.remove(key);
        notify(key, oldValue, "Entity is removed");
        notifyAllListeners(key, oldValue, "Entity is removed");
    }

    @Override
    public Optional<V> get(K key) {
        cleanUp();
        V value = cache.get(key);
        if (value != null) {
            notify(key, value, "Entity is loaded");
            notifyAllListeners(key, value, "Entity is loaded");
        }
        return Optional.ofNullable(value);
    }

    @Override
    public void addListener(HwListener listener) {
        listeners.add(new WeakReference<HwListener<K, V>>(listener, listenerReferenceQueue));
    }

    @Override
    public void removeListener(HwListener listener) {
        if (listener != null) listeners.remove(listener);
    }

    private void cleanUp() {
        WeakReference listenerWeakReference = (WeakReference) listenerReferenceQueue.poll();
        while (listenerWeakReference != null) {
            listeners.remove(listenerWeakReference);
            listenerWeakReference = (WeakReference) listenerReferenceQueue.poll();
        }
    }

    private void notifyAllListeners(K key, V value, String entity) {
        listeners.forEach(weakReferenceListener -> {
            HwListener<K, V> listener = weakReferenceListener.get();
            if (listener != null) {
                listener.notify(key, value, entity);
            }
        });
    }
}
