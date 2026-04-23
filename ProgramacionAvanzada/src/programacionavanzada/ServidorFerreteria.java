package programacionavanzada;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServidorFerreteria {

    public static HashMap<String, Integer> stock = new HashMap<>();
    public static HashMap<String, Double> precios = new HashMap<>();

    public static void main(String[] args) {

        int puerto = 5004;

        stock.put("martillo", 10);
        stock.put("clavos", 50);
        stock.put("destornillador", 15);
        stock.put("taladro", 5);

        precios.put("martillo", 2000.0);
        precios.put("clavos", 100.0);
        precios.put("destornillador", 1500.0);
        precios.put("taladro", 15000.0);

        try {
            ServerSocket servidor = new ServerSocket(puerto);
            System.out.println("Servidor Ferreteria iniciado...");

            while (true) {
                Socket socket = servidor.accept();
                System.out.println("Cliente conectado");

                new ClienteHiloFerreteria(socket).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}