package ua.kpi.tef.pdc.lab04;

import java.io.*;
import java.net.Socket;

//client process
public class ClientApp {

    public static void main(String[] args) {
        Socket sSocket = null;
        BufferedReader bfReader = null;
        DataOutputStream output = null;
        try{
            //Binding socket to 50001 port number on localhost
            sSocket = new Socket("localhost",50001);
            output = new DataOutputStream(sSocket.getOutputStream());
            bfReader = new BufferedReader(new InputStreamReader(sSocket.getInputStream()));

            output.writeBytes("Hello Server\n");

            System.out.println("Message from Server: " + bfReader.readLine());

            output.writeBytes("quit");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                sSocket.close();
                bfReader.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
