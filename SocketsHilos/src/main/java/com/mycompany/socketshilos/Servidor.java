
package com.mycompany.socketshilos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        
        
        
        try {
            ServerSocket server = new ServerSocket(5000);//crea un servidor en el puerto 5000
            Socket sc;


            System.out.println("Servidor iniciado, esperando conexiones...");

            while(true){

                sc = server.accept();//espera a que un cliente se conecte
                System.out.println("cliente conectado....");

                DataInputStream in = new DataInputStream(sc.getInputStream());//recibe datos
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());// manda datos
    

                ServidorHilo hilo = new ServidorHilo(in, out);//crea un nuevo hilo para atender al cliente
                hilo.start();//inicia el hilo para atender al cliente
                
               
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}
