package programacionavanzada;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {

    // Lista global donde se guardan todos los clientes conectados.
    // Es static para que pueda ser accedida desde otras clases como ClienteHilo.
    public static ArrayList<ClienteHilo> clientes = new ArrayList<>();

    public static void main(String[] args) {

        int puerto = 5003; // Puerto donde el servidor va a escuchar conexiones

        try {
            // Se crea el servidor en el puerto indicado
            ServerSocket servidor = new ServerSocket(puerto);
            System.out.println("Servidor iniciado...");

            // Bucle infinito para aceptar múltiples clientes
            while (true) {

                // El servidor queda bloqueado acá hasta que un cliente se conecta
                Socket socket = servidor.accept();
                System.out.println("Cliente conectado");

                // Por cada cliente se crea un hilo independiente
                ClienteHilo hilo = new ClienteHilo(socket);

                // Se guarda el cliente en la lista global
                clientes.add(hilo);

                // Se inicia el hilo, lo que ejecuta el método run()
                hilo.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método que evalúa una expresión matemática en forma de String
    public static double evaluar(String expr) {

        // Primero se resuelven multiplicaciones y divisiones
        // Se repite mientras existan esos operadores en la expresión
        while (expr.contains("*") || expr.contains("/")) {
            expr = resolverOperacion(expr, '*', '/');
        }

        // Luego se resuelven sumas y restas
        while (expr.contains("+") || expr.lastIndexOf('-') > 0) {
            expr = resolverOperacion(expr, '+', '-');
        }

        // Cuando ya no quedan operaciones, se convierte el resultado a número
        return Double.parseDouble(expr);
    }

    // Método auxiliar que busca y resuelve una operación dentro de la expresión
    public static String resolverOperacion(String expr, char op1, char op2) {

        // Recorre la expresión carácter por carácter
        for (int i = 0; i < expr.length(); i++) {

            char c = expr.charAt(i);

            // Si encuentra alguno de los operadores indicados
            if (c == op1 || c == op2) {

                int inicio = i - 1;

                // Busca hacia la izquierda para obtener el número completo
                while (inicio >= 0 &&
                        (Character.isDigit(expr.charAt(inicio)) || expr.charAt(inicio) == '.')) {
                    inicio--;
                }

                int fin = i + 1;

                // Busca hacia la derecha para obtener el número completo
                while (fin < expr.length() &&
                        (Character.isDigit(expr.charAt(fin)) || expr.charAt(fin) == '.')) {
                    fin++;
                }

                // Extrae los números como String y los convierte a double
                double num1 = Double.parseDouble(expr.substring(inicio + 1, i));
                double num2 = Double.parseDouble(expr.substring(i + 1, fin));

                double res = 0;

                // Realiza la operación correspondiente
                if (c == '*') res = num1 * num2;
                if (c == '/') res = num1 / num2;
                if (c == '+') res = num1 + num2;
                if (c == '-') res = num1 - num2;

                // Reemplaza la operación completa en el String original
                return expr.substring(0, inicio + 1) + res + expr.substring(fin);
            }
        }

        return expr;
    }
}