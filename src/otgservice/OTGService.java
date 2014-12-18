/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otgservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author crtx
 */
public class OTGService {

    private static final Logger log = Logger.getLogger(OTGService.class.getName());
    static private boolean shutdownFlag = false;

    public static void main(String[] args) {

        log.info("Start USB-OTG Service");
        registerShutdownHook();
        
        
        Connector listener = new Connector();
        try {
            while (false == shutdownFlag) {
                Thread.sleep(1000);
            }
        }
        catch (InterruptedException ex) {
            log.log(Level.SEVERE, null, ex);
        }
        finally {
            listener.Close_listener();
            log.info("Stop USB-OTG Service");
        }

    }

    static public void setShutdownFlag() {shutdownFlag = true;}
    
    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(
                new Thread() {
                    public void run() {
                        OTGService.setShutdownFlag();
                    }
                }
        );
    }
      
}
