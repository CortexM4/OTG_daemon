/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package USB_OTG;

import java.util.logging.Level;
import java.util.logging.Logger;
import otgservice.Interface.*;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author crtx
 */
public class Serial implements IObservable, SerialPortEventListener {

    private static final Logger log = Logger.getLogger(Serial.class.getName());
    SerialPort serialPort;
    private IObserver obs;

    public Serial(IObserver obs) {
        serialPort = new SerialPort("/dev/ttyDev");
        try {
            this.obs = obs;
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN
                    | SerialPort.FLOWCONTROL_RTSCTS_OUT);

            serialPort.addEventListener(this, SerialPort.MASK_RXCHAR);
            
        }
        catch (SerialPortException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void close() {
        try {
            serialPort.closePort();
        }
        catch (SerialPortException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() > 0) {
            try {
                if (serialPort.getInputBufferBytesCount() > 0) {
                    notifyObserver(serialPort.readString());
                }
            }
            catch (SerialPortException ex) {
                log.log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void notifyObserver(String data) {
        obs.update(data);
    }

    @Override
    public void sendData(String data) {
        try {
            serialPort.writeString(data);
        }
        catch (SerialPortException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

}
