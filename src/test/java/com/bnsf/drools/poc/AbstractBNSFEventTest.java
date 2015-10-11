package com.bnsf.drools.poc;

import com.bnsf.drools.poc.cache.repo.CacheRepository;
import com.bnsf.drools.poc.cache.repo.SimpleLocomotiveInventoryCacheRepository;
import com.bnsf.drools.poc.cache.repo.SimpleTrainCacheRepository;
import com.bnsf.drools.poc.channel.ConfidenceLevelChangeEventChannel;
import com.bnsf.drools.poc.channel.LocomotiveInventoryMissingInCacheEventEventChannel;
import com.bnsf.drools.poc.channel.TrainMissingInCacheEventChannel;
import com.bnsf.drools.poc.configuration.ApplicationConfiguration;
import com.bnsf.drools.poc.drools.util.TrackingAgendaEventListener;
import com.bnsf.drools.poc.model.LocomotiveInventory;
import com.bnsf.drools.poc.model.Train;

import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.time.SessionPseudoClock;
import org.junit.Before;
import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.runtime.rule.EntryPoint;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by rakesh on 9/16/15.
 */
public abstract class AbstractBNSFEventTest {
    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    protected TrackingAgendaEventListener trackingAgendaEventListener = new TrackingAgendaEventListener();

    protected static final String TRAIN_ID = "Train-ABC";

    protected KieSession session;
    protected SessionPseudoClock clock = null;
    protected EntryPoint gpsHarvestStream;

    protected CacheRepository<LocomotiveInventory> locomotiveInventoryCacheRepository = new SimpleLocomotiveInventoryCacheRepository();
    protected CacheRepository<Train> trainCache = new SimpleTrainCacheRepository();

    protected ApplicationConfiguration configuration = new ApplicationConfiguration();
    protected DebugRuleRuntimeEventListener drel = null;

    protected ArgumentCaptor<ObjectInsertedEvent> argumentCaptor;

    @Before
    public void setupTest(){
        drel = mock( DebugRuleRuntimeEventListener.class );
    }

    protected KieSession createDefaultSession() {
        session = createNewSession();
        return session;
    }

    protected KieSession createNewSession(){
        //configure the pseudo clock
        KieSessionConfiguration config = KieServices.Factory.get().newKieSessionConfiguration();
        config.setOption( ClockTypeOption.get("pseudo") );

        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.getKieClasspathContainer();

        KieSession kie_session = kc.newKieSession("BNSF_KS", config);
        //get handle to the clock
        clock = kie_session.getSessionClock();

        //global variables
        kie_session.setGlobal("locomotiveInventoryCacheRepository", locomotiveInventoryCacheRepository);
        kie_session.setGlobal("trainCache", trainCache);
        kie_session.setGlobal("logger", logger);

        kie_session.registerChannel("missing_data_loco_inv", new LocomotiveInventoryMissingInCacheEventEventChannel());
        kie_session.registerChannel("missing_data_train", new TrainMissingInCacheEventChannel());
        kie_session.registerChannel("confidence_level_change_channel", new ConfidenceLevelChangeEventChannel());

        //debug listeners
        kie_session.addEventListener(trackingAgendaEventListener);
        kie_session.addEventListener(drel);

        //kie_session.addEventListener( new DebugAgendaEventListener() );
        //kie_session.addEventListener( new DebugRuleRuntimeEventListener() );
        return kie_session;
    }

    /*
      Utility methods
     */

    /**
     *  calls session.fireAllRules()
     *
     * @return number of rules fired
     */
    protected int fireAllRules(){
        return session.fireAllRules();
    }

    /**
     *  calls session.fireAllRules(with RuleNameEqualsAgendaFilter)
     *
     * @param ruleToConsider
     * @return number of rules fired
     */
    protected int fireAllRules(String ruleToConsider){
        return session.fireAllRules(new RuleNameEqualsAgendaFilter(ruleToConsider));
    }

    protected void advanceClock(long amount, TimeUnit unit ){
        clock.advanceTime(amount, unit);
    }

    /**
     * Captures all the Facts and Events inserted into the working memory
     * <p/>Make sure assertNumberOfObjectsInsertedIntoMemory is called before calling getFactsAndEventsInsertedInMemory
     */
    protected void captureFactsAndEventsInserted(){
        argumentCaptor = ArgumentCaptor.forClass(ObjectInsertedEvent.class);
    }

    /**
     * Verifies the numberExpected facts/events are inserted into the working memory
     * @param numberExpected
     */
    protected void assertNumberOfObjectsInsertedIntoMemory(int numberExpected){
        verify( drel, times(numberExpected) ).objectInserted(argumentCaptor.capture());
    }

    /**
     *
     * @return Facts/Events inserted into the working memory
     */
    protected Iterator<ObjectInsertedEvent> getFactsAndEventsInsertedInMemory(){
        return argumentCaptor.getAllValues().iterator();
    }

    protected void assertRuleFired(final String ruleName){
        assertTrue("Expecting "+ruleName+" to be fired",trackingAgendaEventListener.isRuleFired(ruleName));
    }

    protected void assertRuleNotFired(final String ruleName) {
        assertFalse("Expecting " + ruleName + " to be fired", trackingAgendaEventListener.isRuleFired(ruleName));
    }
}
