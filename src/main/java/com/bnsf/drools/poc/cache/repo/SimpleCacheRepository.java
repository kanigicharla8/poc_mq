/**
 * 
 */
package com.bnsf.drools.poc.cache.repo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Uses Google Guava Cache
 * 
 * @author rakesh
 * @param <T>
 *
 */
public abstract class SimpleCacheRepository<T> implements CacheRepository<T>{
	//uses guava cache
	Cache<String, T> cache = CacheBuilder.newBuilder().build();
	
	/**
	 * @return
	 */
	protected Cache<String, T> getCache() {
		return cache;
	}
	
	/**
	 * @see com.bnsf.drools.poc.cache.repo.CacheRepository#get(java.lang.String)
	 */
	public T get(final String key) {
		return cache.getIfPresent(key);
	}

	/**
	 * @see com.bnsf.drools.poc.cache.repo.CacheRepository#save(java.lang.String, java.lang.Object)
	 */
	public void save(final String key, final T model) {
		cache.put(key, model);
	}
	
	/**
	 * @see com.bnsf.drools.poc.cache.repo.CacheRepository#get(java.util.Set)
	 */
	public List<T> get(Set<String> keys) {
		throw new UnsupportedOperationException();
	}


	/**
	 * @see com.bnsf.drools.poc.cache.repo.CacheRepository#save(java.util.Map)
	 */
	public void save(Map<String, T> model) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.bnsf.drools.poc.cache.repo.CacheRepository#delete(java.lang.String)
	 */
	public void delete(String key) {
		cache.invalidate(key);
	}
}
