package com.carlosverde.ClientTCP;


import com.carlosverde.ServerUDP.UDPServer;
import com.carlosverde.Utilities.MD5;
import net.iharder.Base64;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class TCPClient extends Thread{

    private Socket socket;
    private String hostname = "";
    private String username = "";
    private int tcp_port = -1;
    private int udp_port = -1;
    private int intentos = 5;
    private String command = "";
    private Scanner scanner = new Scanner(System.in);
    private String extra = "";
    private String msg = null;

    public TCPClient() {
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTcp_port(int tcp_port) {
        this.tcp_port = tcp_port;
    }

    public void setUdp_port(int udp_port) {
        this.udp_port = udp_port;
    }

    private void createSocket() throws IOException {
        socket = new Socket(hostname, tcp_port);
    }

    private void getServerInfo(boolean getInfo){

        if (getInfo) {
            while (hostname.trim().equals("")) {
                System.out.print("Servidor IP: ");
                hostname = scanner.nextLine().trim().toLowerCase();
            }

            while (tcp_port <= 0) {
                try {
                    System.out.print("Servidor PORT: ");
                    tcp_port = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println("Debes colocar un puerto valido.");
                }
            }
        }
        else {
            hostname = "localhost";
            tcp_port = 19876;
        }
    }

    private void sendMsg(String msg) throws IOException {
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println(msg);
    }

    private String recvMsg() throws IOException {
        InputStream input = socket.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = null;
        while (line == null) {
            try {
                line = reader.readLine();
            }
            catch (Exception e){
                line = "";
            }
        }
        return line;
    }

    private void manageCommands() throws IOException {
        while (true) {
            while (command.trim().equals("")) {
                System.out.print("Comando a enviar: ");
                command = scanner.nextLine();
            }
            if (command.equals("exit")) break;
            sendMsg(command);
            command = "";
            manageResponse(recvMsg());
            System.out.println("Respuesta: " + extra);
            switch (extra) {
                case "invalid command":
//                    createSocket();
                    return;
                default:
                    break;
            }
        }
    }

    private void doEverything(String username) throws IOException {

        command = "helloiam "+username;
        sendMsg(command);
        if (!manageResponse(recvMsg())) return;

        command = "msglen";
        sendMsg(command);
        if (!manageResponse(recvMsg())) return;

        int tama = Integer.parseInt(extra);
//        System.out.println(String.format("El mensaje tendra una longitud de %s caracteres.",extra));

        if (udp_port <= 0)
            udp_port = ThreadLocalRandom.current().nextInt(12345, 55001);

        UDPServer udpServer = new UDPServer(udp_port,Integer.parseInt(extra));
        udpServer.start();

        System.out.println("Solicitando mensaje... ");
        while (intentos > 0) {
            command = "givememsg "+udp_port;
            sendMsg(command);
//            if (!manageResponse(recvMsg())) return;

            msg = udpServer.getReceived();
//            System.out.println("["+msg+"]");
            if (!msg.equals("") && new String(Base64.decode(msg.trim()), "UTF-8").length() == tama) {
//                msg = udpServer.getReceived().trim();
                break;
            }
            else {
                try {
                    Thread.sleep(2500);
                    intentos--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Lo siento, no se recibio el mensaje o incompleto. Quedan "+intentos+" intentos...");
                System.out.println();
            }
        }

        if (intentos == 0){
            System.out.println("Lo siento, tiempo de espera del mensaje excedido...");

        }
        else {

            msg = new String(Base64.decode(msg.trim()), "UTF-8");
//        System.out.println(String.format("Ha recibido el mensaje: \n%s",msg));

//        System.out.println("consultando el checksum...");
            command = "chkmsg " + MD5.getHash(msg);
            sendMsg(command);
            if (!manageResponse(recvMsg())) return;

//        System.out.println("checksum aprobado...");

            command = "bye";
            sendMsg(command);
            if (!manageResponse(recvMsg())) return;

            udpServer.stopServer();
            System.out.println(String.format("\nSu mensaje es:\n\n%s", msg));
        }
    }

    private boolean manageResponse(String response){
        boolean flag = false;
        if (response.startsWith("ok")){
            extra = response.substring(2).trim();
            flag = true;
        }else if (response.startsWith("error")){
            extra = response.substring(5).trim();
            System.out.println("[ERROR] " + extra);
        }
        return flag;
    }

    public void run(){
        try
        {
//            getServerInfo(false);
            createSocket();
//            System.out.println("\nConexion establecida.... (escriba \"exit\" para salir)\n");
//            manageCommands();

//            String username = "";
//            while (username.trim().equals("")) {
//                System.out.print("Cual es su usuario: ");
//                username = scanner.nextLine();
//            }

            doEverything(username);

            socket.close();
//            System.out.println("\nConexion cerrada....\n");
        }
//        catch (ConnectException ex){
//            ex.printStackTrace();
//            System.out.println(ex.getMessage());
//        } catch (UnknownHostException ex) {
//            ex.printStackTrace();
//            System.out.println(ex.getMessage());
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            System.out.println(ex.getMessage());
//        }
        catch (Exception ex) {
//            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }

}
