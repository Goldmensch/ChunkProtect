package de.goldmensch.chunkprotect.storage.dao;

import java.util.Optional;

public interface Dao<K, V> {
    void write(V v);

    void delete(K k);

    Optional<V> read(K k);
}
