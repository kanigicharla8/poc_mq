package com.bnsf.drools.poc.drools;

import com.bnsf.drools.poc.cache.repo.CacheRepository;
import com.bnsf.drools.poc.channel.ConfidenceLevelChangeEventChannel;
import com.bnsf.drools.poc.channel.LocomotiveInventoryMissingInCacheEventEventChannel;
import com.bnsf.drools.poc.channel.TrainMissingInCacheEventChannel;
import com.bnsf.drools.poc.drools.util.TrackingAgendaEventListener;
import com.bnsf.drools.poc.model.LocomotiveInventory;
import com.bnsf.drools.poc.model.Train;

import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by rakesh on 9/23/15.
 */
public class DroolsSessionProducer implements InitializingBean{

    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    private KieSession session;

    private CacheRepository<LocomotiveInventory> locomotiveInventoryCacheRepository;
    private CacheRepository<Train> trainCacheRepository;
    private TrackingAgendaEventListener trackingAgendaEventListener;

    private ConfidenceLevelChangeEventChannel confidenceLevelChangeEventChannel;

    public void afterPropertiesSet() throws Exception {
        session = createNewSession();

        //could be replaced with a generic drools logger
        session.setGlobal("logger", logger);
    }

    private KieSession createNewSession(){

        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.getKieClasspathContainer();

        KieSession kie_session = kc.newKieSession("BNSF_KS");
        //global variables
        kie_session.setGlobal("locomotiveInventoryCacheRepository", locomotiveInventoryCacheRepository);
        kie_session.setGlobal("trainCache", trainCacheRepository);
        kie_session.setGlobal("logger", logger);

        kie_session.registerChannel("missing_data_loco_inv", new LocomotiveInventoryMissingInCacheEventEventChannel());
        kie_session.registerChannel("missing_data_train", new TrainMissingInCacheEventChannel());
        kie_session.registerChannel("confidence_level_change_channel", confidenceLevelChangeEventChannel);

        //debug listeners
        kie_session.addEventListener(trackingAgendaEventListener);
        if(false){
            kie_session.addEventListener( new DebugAgendaEventListener() );
            kie_session.addEventListener( new DebugRuleRuntimeEventListener() );
        }
        return kie_session;
    }

    public KieSession getSession() {
        return session;
    }

    public void setSession(KieSession session) {
        this.session = session;
    }

    public CacheRepository<LocomotiveInventory> getLocomotiveInventoryCacheRepository() {
        return locomotiveInventoryCacheRepository;
    }

    public void setLocomotiveInventoryCacheRepository(CacheRepository<LocomotiveInventory> locomotiveInventoryCacheRepository) {
        this.locomotiveInventoryCacheRepository = locomotiveInventoryCacheRepository;
    }

    public CacheRepository<Train> getTrainCacheRepository() {
        return trainCacheRepository;
    }

    public void setTrainCacheRepository(CacheRepository<Train> trainCacheRepository) {
        this.trainCacheRepository = trainCacheRepository;
    }

    public TrackingAgendaEventListener getTrackingAgendaEventListener() {
        return trackingAgendaEventListener;
    }

    public void setTrackingAgendaEventListener(TrackingAgendaEventListener trackingAgendaEventListener) {
        this.trackingAgendaEventListener = trackingAgendaEventListener;
    }

    public ConfidenceLevelChangeEventChannel getConfidenceLevelChangeEventChannel() {
        return confidenceLevelChangeEventChannel;
    }

    public void setConfidenceLevelChangeEventChannel(ConfidenceLevelChangeEventChannel confidenceLevelChangeEventChannel) {
        this.confidenceLevelChangeEventChannel = confidenceLevelChangeEventChannel;
    }
}
