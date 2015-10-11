package com.bnsf.drools.poc.integration;

import com.bnsf.drools.poc.cache.repo.CacheRepository;
import com.bnsf.drools.poc.model.LocomotiveInventory;
import com.bnsf.drools.poc.model.Train;

import org.apache.camel.ProducerTemplate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GPSIntegrationTest {

    protected static final String TRAIN_ID = "Train-ABC";

    public static final String GPS_INBOUND_QUEUE = "activemq:queue:queue.event.gpsupdate.in";

    private ApplicationContext context = null;
    private ProducerTemplate camelTemplate = null;

    private CacheRepository<LocomotiveInventory> locomotiveInventoryCacheRepository;
    private CacheRepository<Train> trainCache;

    @Before
    public void setup(){
        context = new ClassPathXmlApplicationContext("fuse/fuse-config.xml");
        camelTemplate = context.getBean("camelTemplate", ProducerTemplate.class);

        locomotiveInventoryCacheRepository = (CacheRepository<LocomotiveInventory>) context.getBean("locomotiveInventoryCacheRepository");
        trainCache = (CacheRepository<Train>) context.getBean("trainCacheRepository");

        loadData();
    }

    private void loadData() {
        //populate the inventory
        LocomotiveInventory locomotiveInventory = new LocomotiveInventory();
        locomotiveInventory.setLocomotiveId("1234");
        locomotiveInventory.setTrainId(TRAIN_ID);
        locomotiveInventoryCacheRepository.save(locomotiveInventory.getId(), locomotiveInventory);

        //populate the trains
        Train train = new Train();
        train.setTrainId(TRAIN_ID);
        trainCache.save(train.getId(), train);
    }

    @Test
    public void test_GPSEvent_Insert(){
        //send GPS Message
        String sampleGPSEvent = "2015-09-16T14:03:00.000+0000;UTC|1234|44.968046|-94.420307";
        camelTemplate.sendBody(GPS_INBOUND_QUEUE, sampleGPSEvent);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //expect response on outbound queue
        //TODO add assertions


    }
}
