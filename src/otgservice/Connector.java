/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otgservice;

import USB_OTG.Serial;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import otgservice.Interface.*;

/**
 *
 * @author crtx
 */
public class Connector implements IObserver {

    private IObservable connector;

    public Connector() {
        connector = new Serial(this);
    }

    public void Close_listener() {
        connector.close();
    }

    @Override
    public void update(String data) {
        if (data.regionMatches(0, "GETE", 0, 4)) {

            String[] cmd_search = {
                "/bin/sh",
                "-c",
                "iwlist wlan0 scan | grep ESSID"
            };

            Runtime runtime = Runtime.getRuntime();
            try {
                Process ls = runtime.exec(cmd_search);
                InputStream in = ls.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    String essid;
                    essid = line.trim().substring(6).replaceAll("\"", "");
                    connector.sendData(essid);
                }
            }
            catch (IOException ex) {
                Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (data.regionMatches(0, "WLAN", 0, 4)) {
            int index_net = data.indexOf(" ", 0);
            int index_pwd = data.indexOf(" ", index_net + 1);
            String essid = data.substring(index_net + 1, index_pwd);
            String pwd = data.substring(index_pwd + 1);

            System.out.println("Trying restart network: "+essid+" "+pwd);
            String[] cmd_wlan = {
                "/bin/sh",
                "-c",
                "sed -i -e 's/wpa-ssid .*/wpa-ssid \"" + essid + "\"/' -e 's/wpa-psk .*/wpa-psk \"" + pwd + "\"/'  "
                    + "/etc/network/interfaces"
            };

            Runtime runtime = Runtime.getRuntime();
            try {
                Process ls = runtime.exec(cmd_wlan);
                //ls = runtime.exec("/etc/init.d/networking restart");
                //InputStream in = ls.getInputStream();

                //BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                //String line;
                //while ((line = reader.readLine()) != null) {
                //    System.out.println(line);
                //}
                //ls = runtime.exec("/etc/init.d/networking restart");
            }
            catch (IOException ex) {
                Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
