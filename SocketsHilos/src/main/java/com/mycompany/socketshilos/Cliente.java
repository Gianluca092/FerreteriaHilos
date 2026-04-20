
package com.mycompany.socketshilos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) throws Exception {
        
        Socket sc = new Socket("localHost",5000);// se conecta al server 
        
        DataInputStream in = new DataInputStream(sc.getInputStream());//recibe datos del servidor 
        DataOutputStream out = new DataOutputStream(sc.getOutputStream());//envia datos al servidor
        
        Scanner sn = new Scanner(System.in);
        
        System.out.println("Ingrese un calculo matematico: ");
        String ecuacion = sn.nextLine();//el usuario escribe su ecuacion y este se guarda en ecuacion
        
        out.writeUTF(ecuacion); //manda lo escrito al servidor 
        
        String respuesta = in.readUTF();
        
        System.out.println(respuesta);
        
        
        sc.close();
    }
}

