package xyz.elevated.frequency.processor.type;

import xyz.elevated.frequency.data.PlayerData;

public interface Processor<T> {
    void process(final PlayerData playerData, final T t);
}
