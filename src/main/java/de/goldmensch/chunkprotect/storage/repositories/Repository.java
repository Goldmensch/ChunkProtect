package de.goldmensch.chunkprotect.storage.repositories;

import java.util.Optional;

public interface Repository<K, V> {
    V create(V v);

    void update(V v);

    void delete(K k);

    Optional<V> read(K k);
}
