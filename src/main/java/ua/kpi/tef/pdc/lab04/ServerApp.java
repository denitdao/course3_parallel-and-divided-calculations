package ua.kpi.tef.pdc.lab04;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//server process
public class ServerApp {

    public static void main(String[] args){
        ServerSocket sSocket = null;
        Socket cSocket = null;
        BufferedReader bfReader = null;
        DataOutputStream output = null;

        try {
            sSocket = new ServerSocket(50001);
            //Wait for a client connection
            cSocket = sSocket.accept();
            bfReader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
            output = new DataOutputStream(cSocket.getOutputStream());

            //Go into infinite loop unless client ask to quit
            while(true){
                String clientMessage = bfReader.readLine();
                System.out.println("Message from Client: " + clientMessage);
                if (clientMessage.equals("quit")) break;

                output.writeBytes("Hey Client, it's Server!\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sSocket.close();
                cSocket.close();
                bfReader.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
