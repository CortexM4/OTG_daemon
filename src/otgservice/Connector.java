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
import otgservice.Interface.*;

/**
 *
 * @author crtx
 */
public class Connector implements IObserver {

    private String[] cmd_search = {
        "/bin/sh",
        "-c",
        "iwlist wlan0 scan | grep ESSID"
    };
    private IObservable connector;

    public Connector() {
        connector = new Serial(this);
    }

    public void Close_listener() {
        connector.close();
    }

    @Override
    public void update(String data) {
        String command = data.substring(0, 4);
        switch (command) {
            case "GETE": {
                Runtime runtime = Runtime.getRuntime();
                try {
                    Process ls = runtime.exec(cmd_search);
                    InputStream in = ls.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line.trim().substring(6).replaceAll("\"", ""));
                    }
                }
                catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                finally {
                    break;
                }
            }
            default:
                break;
        }
    }

}
