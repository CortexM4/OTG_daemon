/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otgservice;

import USB_OTG.Serial;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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
    private final String file = "/etc/network/interfaces";

    public Connector() {
        connector = new Serial(this);
    }

    public void Close_listener() {
        connector.close();
    }

    @Override
    public void update(String data) {

        /* Получение списка доступных сетей */
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

        /* Запись конфигурации в /etc/network/interfaces */
        if (data.regionMatches(0, "WLAN", 0, 4)) {
            data = data.replaceAll("\n", "");                       // Хер знает откуда там перенос строки, но ИНОГДА появляется
            int index_net = data.indexOf(" ", 0);
            int index_pwd = data.indexOf(" ", index_net + 1);
            String essid = data.substring(index_net + 1, index_pwd);
            String pwd = data.substring(index_pwd + 1);

            System.out.println("Network param: " + essid + " " + pwd);

            /* Чтение файла конфигурации сети и внесение новых данных */
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file)));
                String tmp = null; // буфер чтения
                StringBuilder ost = new StringBuilder();
                while ((tmp = br.readLine()) != null) {
                    int i = tmp.indexOf("wpa-ssid");
                    if (tmp.contains("wpa-ssid")) {
                        ost.append("\twpa-ssid \"").append(essid).append("\"\n");
                    }
                    else if (tmp.contains("wpa-psk")) {
                        ost.append("\twpa-psk \"").append(pwd).append("\"\n");
                    }
                    else {
                        ost.append(tmp).append("\n");
                    }
                }
                br.close();
                /* Запись измененных данных */
                FileWriter fw = new FileWriter(new File(file));
                fw.write(ost.toString());
                fw.close();
            }
            catch (IOException ex) {
                Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /* Перезапуск сети */
        if (data.regionMatches(0, "TRYC", 0, 4)) {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process process = runtime.exec("/etc/init.d/networking restart");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            catch (IOException ex) {
                Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
