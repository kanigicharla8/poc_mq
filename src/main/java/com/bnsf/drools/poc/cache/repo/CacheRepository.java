package com.bnsf.drools.poc.cache.repo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * Base interface for all CRUD operations
 *
 * Created by Rakesh Komulwad on 6/4/2014.
 */
public interface CacheRepository<T> {
    T get(String key);

    List<T> get(Set<String> keys);

    void save(String key, T model);

    void save(Map<String, T> model);

    void delete(String key);
}
