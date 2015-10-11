package com.bnsf.drools.poc.configuration;

/**
 * Created by rakesh on 9/21/15.
 */
public class ApplicationConfiguration {

    private JMSConfiguration jms;


    public JMSConfiguration getJms() {
        return jms;
    }

    public void setJms(JMSConfiguration jms) {
        this.jms = jms;
    }

    private class JMSConfiguration{
        private String brokerURL;
        private String userName;
        private String password;

        public String getBrokerURL() {
            return brokerURL;
        }

        public void setBrokerURL(String brokerURL) {
            this.brokerURL = brokerURL;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
