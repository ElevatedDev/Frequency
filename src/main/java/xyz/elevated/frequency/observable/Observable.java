package xyz.elevated.frequency.observable;

import java.util.HashSet;
import java.util.Set;

/*
* I don't like how Java's looks and I can't be arsed to make my own so I am using Toons.
 */
public final class Observable<T> {

    private final Set<ChangeObserver<T>> observers = new HashSet<>();
    private T value;

    public Observable(final T initValue) {
        this.value = initValue;
    }

    public T get() {
        return value;
    }

    public void set(final T value) {
        final T oldValue = this.value;

        this.value = value;

        observers.forEach(it -> it.handle(oldValue, value));
    }


    public ChangeObserver<T> observe(final ChangeObserver<T> onChange) {
        observers.add(onChange);
        return onChange;
    }

    public void unobserve(final ChangeObserver<T> onChange) {
        observers.remove(onChange);
    }

    @FunctionalInterface
    public interface ChangeObserver<T> {
        void handle(final T from, final T to);
    }
}
