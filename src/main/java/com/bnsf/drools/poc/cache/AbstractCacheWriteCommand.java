package com.bnsf.drools.poc.cache;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bnsf.drools.poc.cache.keygen.KeyGenerator;
import com.bnsf.drools.poc.cache.repo.CacheRepository;
import com.bnsf.drools.poc.cache.util.RequestContext;
import com.bnsf.drools.poc.cache.util.ServiceCacheConfiguration;
import com.google.common.base.Stopwatch;
import com.netflix.hystrix.HystrixCommand;

/**
 * Generic write-to-cache implementation designed to be subclassed
 *
 * Created by Rakesh Komulwad on 6/12/2014.
 */
public abstract class AbstractCacheWriteCommand<R> extends HystrixCommand<List<R>> {
    private static final Logger log = LoggerFactory.getLogger(AbstractCacheWriteCommand.class);

    List<R> dataToWriteInCache = null;

    Object searchRequest = null;

  /**
   *
   * @param setter
   * @param dataToWriteInCache - Data to be written in the cache
   * @param searchRequest - Search Request which triggered the service call and eventual write to the cache
   */
    protected AbstractCacheWriteCommand(Setter setter,final List<R> dataToWriteInCache, final Object searchRequest){
        super(setter);
        this.dataToWriteInCache = dataToWriteInCache;
        this.searchRequest = searchRequest;
    }


    protected abstract RequestContext getRequestContext();
    protected abstract KeyGenerator getKeyGenerator();
    protected abstract CacheRepository getCacheRepository();
    protected abstract ServiceCacheConfiguration getServiceConfiguration();

    protected List<R> getDataToWriteInCache() {
        return dataToWriteInCache;
    }

    protected Object getSearchRequest() {
        return searchRequest;
    }

    @Override protected List<R> run() throws Exception {

        ServiceCacheConfiguration config = getServiceConfiguration();

        if(config.getUseCache() == false)
            return dataToWriteInCache;

        Stopwatch sw = Stopwatch.createStarted();
        final String gtid = getRequestContext().getGtid();
        String key = null;
        try{
            for (R r : dataToWriteInCache) {
                //save the data in the cache
                key = getKeyToWriteToCache(r, searchRequest);
                getCacheRepository().save(key , r);
            }
        }
        catch(Throwable t){
            log.error("{} failed to write to cache ",gtid, t);
            throw new Exception(t);
        }

        log.info("{} {}", gtid, sw);
        return dataToWriteInCache;
    }

  private String getKeyToWriteToCache(R r, Object searchRequest) {
    return getKeyGenerator().extract(r, this.searchRequest);
  }

    /*
    @Override
    protected List<R> getFallback() {
        final String gtid = getRequestContext().getGtid();
        log.debug("{} Write to cache failed", gtid);
        return dataToWriteInCache; // Turn into silent failure
    }
    */
}
