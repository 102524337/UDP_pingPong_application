import java.io.*;
import java.util.*;
import java.net.*;

class PingServer
{   
    /*
    *@return: number: int
    *This method is used for generating a random number
    *
    */
    public static int randomNum()
    {
        Random ranNum = new Random();
        return ranNum.nextInt(10);
    }

    /*
    *
    *This is a main method for UDP sending poing messages to the client
    *
    */
    public static void main(String[] args) 
    {   
        System.out.println("Author: Junhyek Hyun");
        try 
        {
            DatagramSocket serverSocket = new DatagramSocket(2000); //server udp socket 
            System.out.println("** Server Started, waiting for a client ... ...");

            byte[] dataReceived = new byte[2000];//buffer
            byte[] sendData = new byte[2000];// buffer to send data to client

            DatagramPacket packetReceived = null; //packet to receive 

            while(true)
            {
                packetReceived = new DatagramPacket(dataReceived, dataReceived.length);
                serverSocket.receive(packetReceived);

                //String clientMsg = new String(packetReceived.getData());

                //if ping message is received, send pong message to the client - receive(1)
                if(dataReceived.length >0)
                {
                    String pong = "pong"; //pong message
                    sendData = pong.getBytes();
                    InetAddress ipAddr = packetReceived.getAddress();//get an ip address of client 
                    int port = packetReceived.getPort(); //get a port number of client
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,ipAddr, port);

                    if(randomNum() == 1 || randomNum() == 7)
                    {
                        System.out.println("!! pong message is randomly dropped! Continue running the server... ...");
                    }else
                    {
                        serverSocket.send(sendPacket); //sent (2)
                        System.out.println("** incoming ping: pong sent");
                    }
                }
            }

        } catch (Exception e) 
        {
            e.printStackTrace();
        }        
    }

}