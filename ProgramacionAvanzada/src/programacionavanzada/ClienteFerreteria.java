package programacionavanzada;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteFerreteria {

    public static void main(String[] args) {

        String host = "localhost";
        int puerto = 5004;

        try {
            Socket socket = new Socket(host, puerto);

            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter salida = new PrintWriter(
                    socket.getOutputStream(), true
            );

            Scanner teclado = new Scanner(System.in);

            Thread recibir = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = entrada.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (Exception e) {
                    System.out.println("Conexión cerrada");
                }
            });

            recibir.start();

            String mensaje;

            while (true) {
                mensaje = teclado.nextLine();
                salida.println(mensaje);

                if (mensaje.equalsIgnoreCase("3")) {
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