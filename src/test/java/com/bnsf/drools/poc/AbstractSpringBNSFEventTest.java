package com.bnsf.drools.poc;

import com.bnsf.drools.poc.cache.repo.CacheRepository;
import com.bnsf.drools.poc.configuration.ApplicationConfiguration;
import com.bnsf.drools.poc.drools.DroolsSessionManager;
import com.bnsf.drools.poc.model.LocomotiveInventory;
import com.bnsf.drools.poc.model.Train;
import com.bnsf.drools.poc.drools.util.TrackingAgendaEventListener;

import org.junit.Assert;
import org.junit.Before;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Work in progress
 */
public class AbstractSpringBNSFEventTest {
    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    protected TrackingAgendaEventListener trackingAgendaEventListener = new TrackingAgendaEventListener();

    protected static final String TRAIN_ID = "Train-ABC";

    protected KieSession session;
    protected EntryPoint gpsHarvestStream;

    protected CacheRepository<LocomotiveInventory> locomotiveInventoryCacheRepository;
    protected CacheRepository<Train> trainCache;

    protected ApplicationConfiguration configuration = new ApplicationConfiguration();

    @Before
    public void init(){
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        DroolsSessionManager sessionManager = (DroolsSessionManager) context.getBean("sessionManager");

        session = sessionManager.getSessionProducer().getSession();
        Assert.assertNotNull(session);

        locomotiveInventoryCacheRepository = (CacheRepository<LocomotiveInventory>) context.getBean("locomotiveInventoryCacheRepository");
        trainCache = (CacheRepository<Train>) context.getBean("trainCacheRepository");
    }

    protected KieSession createDefaultSession() {
        return session;
    }

    protected void assertRuleFired(final String ruleName){
        assertTrue("Expecting "+ruleName+" to be fired",trackingAgendaEventListener.isRuleFired(ruleName));
    }

    protected void assertRuleNotFired(final String ruleName) {
        assertFalse("Expecting " + ruleName + " to be fired", trackingAgendaEventListener.isRuleFired(ruleName));
    }
}
