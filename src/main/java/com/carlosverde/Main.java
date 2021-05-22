package com.carlosverde;


import com.carlosverde.ClientTCP.TCPClient;
import com.carlosverde.ServerUDP.UDPServer;
import com.carlosverde.Utilities.MD5;
import net.iharder.Base64;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

//    private static Socket socket;
//    private static String hostname = "";
//    private static int port = -1;
//    private static String command = "";
//    private static Scanner scanner = new Scanner(System.in);
//    private static String extra = "";
//    private static String msg = null;
//
//    public static void main(String[] args) {
//        try
//        {
//            getServerInfo(false);
//            createSocket();
//            System.out.println("\nConexion establecida.... (escriba \"exit\" para salir)\n");
////            manageCommands();
//
//            String username = "";
//            while (username.trim().equals("")) {
//                System.out.print("Cual es su usuario: ");
//                username = scanner.nextLine();
//            }
//
//            doEverything(username);
//
//            socket.close();
//            System.out.println("\nConexion cerrada....\n");
//        }
////        catch (ConnectException ex){
////            ex.printStackTrace();
////            System.out.println(ex.getMessage());
////        } catch (UnknownHostException ex) {
////            ex.printStackTrace();
////            System.out.println(ex.getMessage());
////        } catch (IOException ex) {
////            ex.printStackTrace();
////            System.out.println(ex.getMessage());
////        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//            System.out.println(ex.getMessage());
//        }
//
//    }
//
//    private static void createSocket() throws IOException {
//        socket = new Socket(hostname, port);
//    }
//
//    private static void getServerInfo(boolean getInfo){
//
//        if (getInfo) {
//            while (hostname.trim().equals("")) {
//                System.out.print("Servidor IP: ");
//                hostname = scanner.nextLine().trim().toLowerCase();
//            }
//
//            while (port <= 0) {
//                try {
//                    System.out.print("Servidor PORT: ");
//                    port = Integer.parseInt(scanner.nextLine());
//                } catch (Exception e) {
//                    System.out.println("Debes colocar un puerto valido.");
//                }
//            }
//        }
//        else {
//            hostname = "localhost";
//            port = 19876;
//        }
//    }
//
//    private static void sendMsg(String msg) throws IOException {
//        OutputStream output = socket.getOutputStream();
//        PrintWriter writer = new PrintWriter(output, true);
//        writer.println(msg);
//    }
//
//    private static String recvMsg() throws IOException {
//        InputStream input = socket.getInputStream();
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//        String line = null;
//        while (line == null) {
//            try {
//                line = reader.readLine();
//            }
//            catch (Exception e){
//                line = "";
//            }
//        }
//        return line;
//    }
//
//    private static void manageCommands() throws IOException {
//        while (true) {
//            while (command.trim().equals("")) {
//                System.out.print("Comando a enviar: ");
//                command = scanner.nextLine();
//            }
//            if (command.equals("exit")) break;
//            sendMsg(command);
//            command = "";
//            manageResponse(recvMsg());
//            System.out.println("Respuesta: " + extra);
//            switch (extra) {
//                case "invalid command":
////                    createSocket();
//                    return;
//                default:
//                    break;
//            }
//        }
//    }
//
//    private static void doEverything(String username) throws IOException {
//
//        command = "helloiam "+username;
//        sendMsg(command);
//        if (!manageResponse(recvMsg())) return;
//
//        command = "msglen";
//        sendMsg(command);
//        if (!manageResponse(recvMsg())) return;
//
//        int tama = Integer.parseInt(extra);
////        System.out.println(String.format("El mensaje tendra una longitud de %s caracteres.",extra));
//
//        int udp_port = ThreadLocalRandom.current().nextInt(12345, 55001);;
//        UDPServer udpServer = new UDPServer(udp_port,Integer.parseInt(extra));
//        udpServer.start();
//
//        command = "givememsg "+udp_port;
//        sendMsg(command);
//        if (!manageResponse(recvMsg())) return;
//        msg = udpServer.getReceived();
//        while ( msg.equals("") || new String(Base64.decode(msg), "UTF-8").length() != tama){
//            msg = udpServer.getReceived().trim();
////            System.out.println("tamaño: " + Base64.base64Decode(msg).length());
//        }
//        msg = new String(Base64.decode(msg), "UTF-8");
////        System.out.println(String.format("Ha recibido el mensaje: \n%s",msg));
//
////        System.out.println("consultando el checksum...");
//        command = "chkmsg "+ MD5.getHash(msg);
//        sendMsg(command);
//        if (!manageResponse(recvMsg())) return;
//
////        System.out.println("checksum aprobado...");
//
//        command = "bye";
//        sendMsg(command);
//        if (!manageResponse(recvMsg())) return;
//
//        udpServer.stopServer();
//        System.out.println(String.format("%s... Su mensaje es:\n%s",extra,msg));
//    }
//
//    private static boolean manageResponse(String response){
//        boolean flag = false;
//        if (response.startsWith("ok")){
//            extra = response.substring(2).trim();
//            flag = true;
//        }else if (response.startsWith("error")){
//            extra = response.substring(5).trim();
//            System.out.println("[ERROR] " + extra);
//        }
//        return flag;
//    }

    public static void main(String[] args) {


        String hostname = System.getProperty("server", "localhost");
        String tcp_port = System.getProperty("tcp_p", "19876");
        String udp_port = System.getProperty("udp_p", null);
        String username = System.getProperty("user", "usuario_2");

        if (username != null){
            try {

                TCPClient tcpClient = new TCPClient();

                int tcp = Integer.parseInt(tcp_port);

                if (udp_port != null) {
                    int udp = Integer.parseInt(udp_port);
                    tcpClient.setUdp_port(udp);
                }

                tcpClient.setHostname(hostname);
                tcpClient.setTcp_port(tcp);
                tcpClient.setUsername(username);

                tcpClient.start();
            }
            catch (NumberFormatException e){
                System.out.println("Debes introducir un puerto valido \"-Dtcp_p=19876\"");
            }
        }
        else
        {
            System.out.println("Debes introducir un usuario valido \"-Duser=user\"");
        }

    }

}
