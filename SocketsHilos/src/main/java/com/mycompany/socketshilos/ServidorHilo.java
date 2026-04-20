/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.socketshilos;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.xml.crypto.Data;

public class ServidorHilo extends Thread {
    
        private DataInputStream in;
        private DataOutputStream out;
        
       
       
       public ServidorHilo(DataInputStream in, DataOutputStream out) {
           this.in = in;
           this.out = out;
       }


        @Override
        public void run(){
            
            try {
                String ecuacion = in.readUTF();//recibe algo tipo "4+5*6"
                
                int resultado = calcular(ecuacion);//procesa la cuenta
                
                out.writeUTF("Resultado: "+ resultado);
  
            } catch (IOException ex) {
                System.getLogger(ServidorHilo.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }

        }
        
      public int calcular(String ecuacion) {

    String[] numeros = ecuacion.split("[+\\-*/]");
    String[] operadores = ecuacion.split("[0-9]+");

    int[] nums = new int[numeros.length];

    // convierto a int
    for (int i = 0; i < numeros.length; i++) {
        nums[i] = Integer.parseInt(numeros[i]);
    }

    //resolver * primero bien
    for (int i = 1; i < operadores.length; i++) {

        if (operadores[i].equals("*")) {

            nums[i] = nums[i - 1] * nums[i]; // multiplico

            nums[i - 1] = 0; // lo anulo para no sumarlo después

            // convierto el operador en "+" para no romper el segundo paso
            operadores[i] = "+";
        
        }else if(operadores[i].equals("/")){
         
            if(nums[i] == 0){
                throw new ArithmeticException("No se puede dividir por 0");
            }
            
            nums[i] = nums[i-1] / nums[i];
            nums[i - 1] = 0;
            
            operadores[i] = "+";
            
        }
    }

    // suma y resta
    int resultado = nums[0]; // lo que hace es al numero en la posicion 0 lo guarda en resultado para despues ir sumando o restando con los numeros en las otras posiciones 

    for (int i = 1; i < nums.length; i++) {

        if (operadores[i].equals("+")) {
            resultado += nums[i];
        }

        else if (operadores[i].equals("-")) {
            resultado -= nums[i];
        }
    }

    return resultado;
}
}

        

        
        
    

