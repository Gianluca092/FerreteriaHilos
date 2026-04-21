package programacionavanzada;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        String host = "localhost"; // Dirección del servidor
        int puerto = 5003;         // Puerto del servidor

        try {
            // Se conecta al servidor
            Socket socket = new Socket(host, puerto);

            // Flujo de entrada (leer lo que envía el servidor)
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            // Flujo de salida (enviar datos al servidor)
            PrintWriter salida = new PrintWriter(
                    socket.getOutputStream(), true
            );

            // Permite leer lo que escribe el usuario
            Scanner teclado = new Scanner(System.in);

            // Hilo separado para escuchar mensajes del servidor en tiempo real
            Thread recibir = new Thread(() -> {
                try {
                    String mensajeServidor;

                    // Lee continuamente lo que envía el servidor
                    while ((mensajeServidor = entrada.readLine()) != null) {
                        System.out.println(mensajeServidor);
                    }
                } catch (Exception e) {
                    System.out.println("Conexión cerrada");
                }
            });

            recibir.start();

            String mensaje;

            // Bucle para enviar mensajes al servidor
            while (true) {

                mensaje = teclado.nextLine();
                salida.println(mensaje);

                // Si el usuario escribe SALIR, termina
                if (mensaje.equalsIgnoreCase("SALIR")) {
                    break;
                }
            }

            socket.close();
            teclado.close();

        } catch (Exception e) {
            System.out.println("Error de conexión");
        }
    }
}