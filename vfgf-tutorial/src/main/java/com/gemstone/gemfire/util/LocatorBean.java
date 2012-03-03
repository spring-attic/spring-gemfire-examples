package com.gemstone.gemfire.util;

import java.io.File;
import java.net.InetAddress;
import java.util.Properties;

import com.gemstone.gemfire.distributed.Locator;
import com.gemstone.gemfire.distributed.internal.InternalLocator;
import com.gemstone.gemfire.i18n.LogWriterI18n;

public class LocatorBean {
    
    private boolean throwOnBindFailure = false;
    private InetAddress bind;
    private int port1 = 10334;
    private int port2 = 10335;
    public int getPort1() {
        return port1;
    }

    public void setPort1(int port1) {
        this.port1 = port1;
    }

    public int getPort2() {
        return port2;
    }

    public void setPort2(int port2) {
        this.port2 = port2;
    }

    private Locator locator;
    private File log;
    private File state;
    private boolean peerLocator = true;
    private boolean serverLocator = true;
    private String hostnameForClients;
    private String locators;
    
    public String getLocators() {
        return locators;
    }

    public void setLocators(String locators) {
        this.locators = locators;
    }

    public void startLocator() {
        try {
            startLocator(port1, true); 
        } catch (Exception x) {
            startLocator(port2, throwOnBindFailure);
        }
    }
    
    public void startLocator(int port, boolean throwOnFail) {
        try {
            Properties props = new Properties();
            props.setProperty("locators", locators);
            props.setProperty("mcast-port", "0");
            locator = InternalLocator.startLocator(port, log, state, (LogWriterI18n)null, (LogWriterI18n)null, bind, true, props, peerLocator, serverLocator, hostnameForClients);
            //locator = Locator.startLocatorAndDS(port, log, props);
            System.out.println("Started locator bind="+bind+" port="+port+" locators="+locators);
        }catch (Exception x) {
            if (throwOnFail) {
                throw new RuntimeException(x);
            } else {
                System.err.println("Locator start failure for port["+port+"]: ("+x.getMessage()+")");
            }
        }
    }
    
    public void stopLocator() {
        if (locator!=null) {
            locator.stop();
            locator=null;
        }
    }

    public boolean isThrowOnBindFailure() {
        return throwOnBindFailure;
    }

    public void setThrowOnBindFailure(boolean throwOnBindFailure) {
        this.throwOnBindFailure = throwOnBindFailure;
    }

    public InetAddress getBind() {
        return bind;
    }

    public void setBind(InetAddress bind) {
        this.bind = bind;
    }

    public int getPort() {
        return port1;
    }

    public void setPort(int port) {
        this.port1 = port;
    }

    public Locator getLocator() {
        return locator;
    }

    public void setLocator(Locator locator) {
        this.locator = locator;
    }

    public File getLog() {
        return log;
    }

    public void setLog(File log) {
        this.log = log;
    }

    public File getState() {
        return state;
    }

    public void setState(File state) {
        this.state = state;
    }

    public boolean isPeerLocator() {
        return peerLocator;
    }

    public void setPeerLocator(boolean peerLocator) {
        this.peerLocator = peerLocator;
    }

    public boolean isServerLocator() {
        return serverLocator;
    }

    public void setServerLocator(boolean serverLocator) {
        this.serverLocator = serverLocator;
    }

    public String getHostnameForClients() {
        return hostnameForClients;
    }

    public void setHostnameForClients(String hostnameForClients) {
        this.hostnameForClients = hostnameForClients;
    }
    
    
    
    
}
