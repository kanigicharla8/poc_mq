package com.bnsf.drools.poc.drools;

import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rakesh on 9/23/15.
 */
public class DroolsSessionManager implements InitializingBean, DisposableBean {

    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    private DroolsSessionProducer sessionProducer;
    private DroolsSessionRunnable sessionRunnable;

    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    public KieSession getSession(){
        return sessionProducer.getSession();
    }

    /**
     * starts the session
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        executorService.submit(new DroolsSessionRunnable(sessionProducer.getSession()));
    }

    /**
     * performs clean up
     * @throws Exception
     */
    public void destroy() throws Exception {
        //stop the session
        sessionProducer.getSession().halt();
    }

    /**
     * Runs the session in fireUntilHalt mode
     */
    private static class DroolsSessionRunnable implements Runnable{

       private final transient Logger logger = LoggerFactory.getLogger(getClass());

       KieSession session;

       public DroolsSessionRunnable(KieSession session){
           this.session = session;
       }

       public void run() {
           logger.info("Session Started");
           try{
               //start the session
               session.fireUntilHalt();
           }catch (Throwable t){
               logger.error("Error in running the session ",t);
           }
           logger.info("Session Halted");
       }
   }

    public DroolsSessionProducer getSessionProducer() {
        return sessionProducer;
    }

    public void setSessionProducer(DroolsSessionProducer sessionProducer) {
        this.sessionProducer = sessionProducer;
    }

}
