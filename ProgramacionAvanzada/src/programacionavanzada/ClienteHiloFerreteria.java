package programacionavanzada;

import java.io.*;
import java.net.*;
import java.util.Map;

public class ClienteHiloFerreteria extends Thread {

    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private String nombre;

    public ClienteHiloFerreteria(Socket socket) throws IOException {
        this.socket = socket;

        entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        salida = new PrintWriter(
                socket.getOutputStream(), true
        );
    }

    @Override
    public void run() {
        try {
            salida.println("Ingrese su nombre:");
            nombre = entrada.readLine();

            salida.println("Bienvenido a la ferreteria " + nombre);

            String opcion;

            while (true) {

                menu();
                opcion = entrada.readLine();

                System.out.println("[" + nombre + "]: " + opcion);

                if (opcion.equals("3")) {
                    salida.println("Gracias por visitar la ferreteria");
                    break;
                }

                procesar(opcion);
            }

            socket.close();

        } catch (Exception e) {
            System.out.println(nombre + " desconectado");
        }
    }

    private void menu() {
        salida.println("""
        === MENU FERRETERIA ===
        1 - Ver stock
        2 - Comprar producto
        3 - Salir
        """);
    }

    private void procesar(String opcion) {

        if (opcion.equals("1")) {

            String lista = "Stock disponible:\n";

            for (Map.Entry<String, Integer> p : ServidorFerreteria.stock.entrySet()) {
                lista += p.getKey() + " - $" + ServidorFerreteria.precios.get(p.getKey()) + " - stock: " + p.getValue() + "\n";
            }

            salida.println(lista);
        }

        else if (opcion.equals("2")) {

            try {
                salida.println("Ingrese producto:");
                String producto = entrada.readLine();

                salida.println("Ingrese cantidad:");
                int cantidad = Integer.parseInt(entrada.readLine());

                if (!ServidorFerreteria.stock.containsKey(producto)) {
                    salida.println("Producto no existe");
                    return;
                }

                int stockActual = ServidorFerreteria.stock.get(producto);
                double precio = ServidorFerreteria.precios.get(producto);

                if (stockActual < cantidad) {
                    salida.println("Stock insuficiente");
                    return;
                }

                double total = precio * cantidad;

                salida.println("Total a pagar: $" + total);
                salida.println("Desea confirmar la compra? (si/no)");

                String respuesta = entrada.readLine();

                if (respuesta.equalsIgnoreCase("si")) {
                    ServidorFerreteria.stock.put(producto, stockActual - cantidad);
                    salida.println("Compra realizada con exito");
                } else {
                    salida.println("Compra cancelada");
                }

            } catch (Exception e) {
                salida.println("Error en la compra");
            }
        }

        else {
            salida.println("Opcion no valida");
        }
    }
}