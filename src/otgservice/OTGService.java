/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otgservice;

/**
 *
 * @author crtx
 */
public class OTGService {

    public static void main(String[] args) {

        System.out.println("Listener created");
        Connector listener = new Connector();
        

        try {
            Thread.sleep(20000);
        }
        catch (InterruptedException ex) {
            System.out.println("Err " + ex.getMessage());
        }
        finally {
            System.out.println("Listener closed");
            listener.Close_listener();
        }

    }

}
