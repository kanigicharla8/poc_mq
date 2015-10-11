package com.bnsf.drools.poc.drools.util;

import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the rules executed
 *
 * Created by rakesh on 9/10/15.
 */
public class TrackingAgendaEventListener extends DefaultAgendaEventListener {
    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    List<String> rulesFiredList = new ArrayList<String>();

    @Override
    public void afterMatchFired(AfterMatchFiredEvent event) {
        String ruleName = event.getMatch().getRule().getName();
        logger.info("Rule fired {}",ruleName);
        rulesFiredList.add(ruleName);
    }

    public boolean isRuleFired(String ruleName) {
        for (String firedRuleName : rulesFiredList) {
            if (firedRuleName.equals(ruleName)) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        rulesFiredList.clear();
    }

    public List<String> getRulesFiredList() {
        return rulesFiredList;
    }
}
