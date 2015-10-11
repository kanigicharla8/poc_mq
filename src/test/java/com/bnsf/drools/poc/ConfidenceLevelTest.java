package com.bnsf.drools.poc;

import com.bnsf.drools.poc.events.AEIEvent;
import com.bnsf.drools.poc.events.GPSLocoEvent;
import com.bnsf.drools.poc.events.internal.ConfidenceLevelPercentage;
import com.bnsf.drools.poc.events.internal.MissingGPSLocoEvent;
import com.bnsf.drools.poc.model.LocomotiveInventory;
import com.bnsf.drools.poc.model.Train;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.runtime.rule.EntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rakesh on 9/16/15.
 */
public class ConfidenceLevelTest extends AbstractBNSFEventTest{

    private static final String LOCOMOTIVE_ID = "1234";

    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    private EntryPoint aeiHarvestStream;

    @Before
    public void setup(){
        //load cache
        loadData();

        this.session = createDefaultSession();
        this.gpsHarvestStream = this.session.getEntryPoint("GPS Harvest Stream");
        this.aeiHarvestStream = this.session.getEntryPoint("AEI Harvest Stream");
    }

    @After
    public void cleanup(){
        this.session.dispose();
        this.trackingAgendaEventListener.reset();
    }

    private void loadData() {
        //populate the inventory
        LocomotiveInventory locomotiveInventory = new LocomotiveInventory();
        locomotiveInventory.setLocomotiveId(LOCOMOTIVE_ID);
        locomotiveInventory.setTrainId(TRAIN_ID);
        locomotiveInventoryCacheRepository.save(locomotiveInventory.getId(), locomotiveInventory);

        //populate the trains
        Train train = new Train();
        train.setTrainId(TRAIN_ID);
        trainCache.save(train.getId(), train);
    }

    /**
     * Test: If a subsequent GPS event is not received in 30m then a MissingGPSLocoEvent event is expected
     */
    @Test
    public void testRule_GPSLocoEvent_did_not_receive_a_subsequent_GPSLocoEvent(){
        String ruleName="GPSLocoEvent did not receive a subsequent GPSLocoEvent";

        captureFactsAndEventsInserted();

        GPSLocoEvent gpsLocoEvent = new GPSLocoEvent();
        gpsLocoEvent.setLocomotiveId(LOCOMOTIVE_ID);
        gpsLocoEvent.setLatitude(100);
        gpsLocoEvent.setLongitude(200);

        this.gpsHarvestStream.insert(gpsLocoEvent);
        this.session.getAgenda().getAgendaGroup( "evaluation" ).setFocus();
        int rulesFired = fireAllRules(ruleName);

        assertEquals("No rules should be fired",0,rulesFired);

        //train should not have any changes
        Train train = trainCache.get(TRAIN_ID);
        assertNotNull("Train should not be null", train);
        assertEquals("Latitude should not change", 0.0, train.getLatitude(), 0.0f);
        assertEquals("Longitude should not change", 0.0, train.getLongitude(), 0.0f);
        assertEquals("Confidence level not have been computer", 0.0, train.getConfidenceLevel(), 0.0f);

        //advance the clock by 30 min
        advanceClock(30, TimeUnit.MINUTES);

        this.trackingAgendaEventListener.reset();
        rulesFired = fireAllRules(ruleName);

        assertEquals("1 rule should be fired",1,rulesFired);
        assertRuleFired("GPSLocoEvent did not receive a subsequent GPSLocoEvent");

        // 2 objects GPSLocoEvent and MissingGPSLocoEvent should have been inserted into the working memory
        assertNumberOfObjectsInsertedIntoMemory(2);

        Iterator<ObjectInsertedEvent> events = getFactsAndEventsInsertedInMemory();

        GPSLocoEvent prevGPSEvent = (GPSLocoEvent) events.next().getObject();
        Assert.assertThat(prevGPSEvent, is(equalTo(gpsLocoEvent)));

        // MissingGPSLocoEvent fact should have been inserted by the rule
        MissingGPSLocoEvent missingGPSLocoEvent = (MissingGPSLocoEvent) events.next().getObject();
        Assert.assertThat(missingGPSLocoEvent.getGpsLocoEvent() , is(equalTo(prevGPSEvent)));

    }
    /**
     * Test for missing GPSLocoEvents
     * @throws InterruptedException
     */
    @Test
    public void testGPSLocoEvent_Handle_MissingGPSLocoEvent() throws InterruptedException{
        GPSLocoEvent gpsLocoEvent = new GPSLocoEvent();
        gpsLocoEvent.setLocomotiveId(LOCOMOTIVE_ID);
        gpsLocoEvent.setLatitude(100);
        gpsLocoEvent.setLongitude(200);

        this.gpsHarvestStream.insert(gpsLocoEvent);
        this.session.getAgenda().getAgendaGroup( "evaluation" ).setFocus();
        fireAllRules();

        //train should have the latest GPS co-ordinate set
        Train train = trainCache.get(TRAIN_ID);
        assertNotNull("Train should not be null", train);
        assertEquals("Latitude should be same", gpsLocoEvent.getLatitude(), train.getLatitude(), 0.0f);
        assertEquals("Longitude should be same", gpsLocoEvent.getLongitude(), train.getLongitude(), 0.0f);
        assertEquals("Confidence level not increased", ConfidenceLevelPercentage.GPSLocoEvent.getPositivePercentageLevel(), train.getConfidenceLevel(), 0.0f);

        //sleep for 2 seconds
        Thread.currentThread().sleep(1000);
        this.trackingAgendaEventListener.reset();
        fireAllRules();
        assertRuleFired("GPSLocoEvent did not receive a subsequent GPSLocoEvent");
        assertRuleFired("Handle MissingGPSLocoEvent");


        train = trainCache.get(TRAIN_ID);
        //confidence level should have reduced from 10 to 5
        assertEquals("Confidence level not reduced", 5.0,train.getConfidenceLevel(),0.0f);
        assertRuleFired("Handle Changes to ConfidenceLevels");
    }


    @Test
    public void test_AEIEvent_After_GPSLocoEvent() throws InterruptedException {
        GPSLocoEvent gpsLocoEvent = new GPSLocoEvent();
        gpsLocoEvent.setLocomotiveId(LOCOMOTIVE_ID);
        gpsLocoEvent.setLatitude(100);
        gpsLocoEvent.setLongitude(200);

        this.gpsHarvestStream.insert(gpsLocoEvent);
        this.session.getAgenda().getAgendaGroup( "evaluation" ).setFocus();
        fireAllRules();

        AEIEvent aeiEvent = new AEIEvent();
        aeiEvent.setLocomotiveId(LOCOMOTIVE_ID);
        aeiEvent.setAEIReaderId("Reader-123");

        this.trackingAgendaEventListener.reset();

        //sleep for 500ms
        Thread.currentThread().sleep(500);
        //insert AEIEvent
        this.aeiHarvestStream.insert(aeiEvent);
        //fire rules
        fireAllRules();

        assertRuleFired("GPSLocoEvent received a subsequent AEIEvent");

        Train train = trainCache.get(TRAIN_ID);

        //confidence should have changed from 10 to 15 - train did receive a subsequent AEI event
        assertEquals("Confidence level not increased", 15.0, train.getConfidenceLevel(), 0.0f);
        assertRuleFired("Handle Changes to ConfidenceLevels");
    }

    @Test
    public void test_Missing_AEIEvent_After_GPSLocoEvent() throws InterruptedException {
        GPSLocoEvent gpsLocoEvent = new GPSLocoEvent();
        gpsLocoEvent.setLocomotiveId(LOCOMOTIVE_ID);
        gpsLocoEvent.setLatitude(100);
        gpsLocoEvent.setLongitude(200);

        this.gpsHarvestStream.insert(gpsLocoEvent);
        this.session.getAgenda().getAgendaGroup( "evaluation" ).setFocus();
        fireAllRules();

        //sleep for 2 seconds
        Thread.currentThread().sleep(2000);
        this.trackingAgendaEventListener.reset();
        fireAllRules();
        assertRuleFired("GPSLocoEvent did not receive a subsequent AEIEvent");

        Train train = trainCache.get(TRAIN_ID);
        //confidence level should have reduced from 8 to 3
        //confidence changed from 10 to 2 - train did not receive a subsequent AEI event
        //confidence changed from 8 to 3 - train did not receive a subsequent GPSLoco event
        assertEquals("Confidence level not reduced", 3.0, train.getConfidenceLevel(), 0.0f);
        assertRuleFired("Handle Changes to ConfidenceLevels");
    }
}
