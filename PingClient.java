import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;
import java.net.*;


class PingClient
{   

    /*
    *
    *@return: true or false depending on user input. 
    *This method is used for getting user value and matching against pre-defined values such as "y", "yes", "Y", "Yes".
    */
    public static boolean isContinue()
    {   
        Scanner scanner = new Scanner(System.in); //user input scanner 
        System.out.println("##!! Do you want to exit the program? y/n !!##");
        String isExit = scanner.nextLine();

        if(isExit.equals("y") || isExit.equals("yes") || isExit.equals("Y") || isExit.equals("Yes"))
        {
            return true;
        }else
        {
            return false;
        }

    }
    /*
    *@param: pingTime :long , pongTime: long - ping Time assigned when the packet is sent and pong Time assigned when the packet arrives 
    *@return: difference between pongTime and pingTime : long
    *This method is used for calculation of ping time and pong time to sort RTT value
    */
    public static long calculateRTT(long pingTime, long pongTime)
    {
        return pongTime - pingTime;
    }

    /*
    *
    *This is a main method which includes a logic for sending ping and receive pong from server side using UDP. 
    *
    */
    public static void main(String[] args) 
    {   
        System.out.println("Author: Junhyek Hyun");
        Scanner scn = new Scanner(System.in); //user input scanner 
        DatagramSocket clientSocket = null;

        try 
        {    
            clientSocket = new DatagramSocket();//socket creation
            DatagramPacket clientPacket;
            InetAddress localIp = InetAddress.getLocalHost(); // ip address

            long pingTime; //ping time when sending
            long pongTime; // pong time 
            long rtt = 0; // RTT
            int rttSeqNum = 1; //Serial number for Rtt of ping 

            int sequenceNum = 1; //sequence number

            byte sendBuffer[] = null;
            clientSocket.setSoTimeout(1000);//timeout for one second 

            while(true)
            {   
                String userInput = ""; //assigns scanner value of user input
                String ping = ""; // assigns ping string

                //user input
                if(sequenceNum <= 10)
                {
                    System.out.println("** Do you want to ping the server? (y/n)");
                    userInput = scn.nextLine();
                    ping = "ping";
                  
                }else if(sequenceNum == 11)
                {
                    userInput = "y";
                }

                //if user wants to send a ping
                if(userInput.equals("y"))
                {                       
                    //print rtt
                    if(sequenceNum>=1 && rtt != 0)
                    {   
                        System.out.println("- RTT for ping" + rttSeqNum +":" +rtt);
                        rttSeqNum++;
                    }
                    if(rttSeqNum >10)
                    {
                        System.out.println("** You have pinged 10 times. System terminating ... ...");
                        break;
                    }

                    //send ping message to server - send(1)
                    sendBuffer = ping.getBytes();
                    clientPacket = new DatagramPacket(sendBuffer, sendBuffer.length,localIp, 2000);
                    pingTime = System.nanoTime();//get ping Time
                    clientSocket.send(clientPacket);//send data
                    System.out.println("** pinging server" + "(ping" + sequenceNum + ")");

                    //receive(2)
                    DatagramPacket rcvPacket = new DatagramPacket(sendBuffer, sendBuffer.length);
                    clientSocket.receive(rcvPacket); //receive data from the server
                    pongTime = System.nanoTime(); //get pong time
                    //String stc = new String(rcvPacket.getData());

                    rtt = calculateRTT(pingTime, pongTime);

                }else //if not exit the program
                {
                    System.out.println("** Exiting ... .. System terminating");
                    break;
                }
                sequenceNum++; //increment sequence number

                //if sequence number is 5, ask if user wants to go out
                if(sequenceNum == 6)
                {
                    boolean iscont = isContinue();
                    if(iscont)
                    {   
                        System.out.println("** Existing the progam... ...");
                        System.exit(0);
                        break;
                    }
                }
            }//loop ends
            
        } catch (SocketException e) 
        {
            e.printStackTrace();
        }catch(IOException e)
        {
            System.out.println("!! Timeout. Pong message might have been dropped. Client Closing... ...");
        }

        //if socket is not closed, close the socket
        if(!clientSocket.isClosed())
        {
           clientSocket.close();        
        }
        System.exit(0);//exit System
    }
}