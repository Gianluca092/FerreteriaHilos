package programacionavanzada;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;

public class ClienteHilo extends Thread {

    private Socket socket; // Conexión con el cliente
    private BufferedReader entrada; // Para leer lo que envía el cliente
    private PrintWriter salida;     // Para enviar datos al cliente
    private String nombre;          // Nombre del usuario

    public ClienteHilo(Socket socket) throws IOException {
        this.socket = socket;

        // Se inicializan los flujos de entrada y salida
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
            // Se le pide el nombre al cliente
            salida.println("Ingrese su nombre:");
            nombre = entrada.readLine();

            // Se valida que el nombre no esté repetido
            nombre = validarNombre(nombre);

            salida.println("Bienvenido " + nombre);

            // Se muestra el menú de comandos disponibles
            menu();

            String mensaje;

            // Bucle principal que escucha mensajes del cliente
            while ((mensaje = entrada.readLine()) != null) {

                // Se muestra en la consola del servidor (log)
                System.out.println("[" + nombre + "]: " + mensaje);

                // Si el cliente quiere salir, se termina el hilo
                if (mensaje.equalsIgnoreCase("SALIR")) {
                    salida.println("Conexión finalizada");
                    break;
                }

                // Se procesa el mensaje recibido
                procesar(mensaje);
            }

            // Se cierra el socket cuando termina la conexión
            socket.close();

        } catch (Exception e) {
            System.out.println(nombre + " desconectado");
        }
    }

    // Método que asegura que no haya dos usuarios con el mismo nombre
    private String validarNombre(String nombre) {
        String original = nombre;
        int i = 1;

        boolean existe;

        do {
            existe = false;

            // Recorre todos los clientes conectados
            for (ClienteHilo c : Servidor.clientes) {

                // Si encuentra uno con el mismo nombre, agrega un número
                if (c != this && c.nombre != null && c.nombre.equals(nombre)) {
                    nombre = original + i;
                    i++;
                    existe = true;
                    break;
                }
            }

        } while (existe);

        return nombre;
    }

    // Envía al cliente los comandos disponibles
    private void menu() {
        salida.println("""
        === MENU ===
        /hora
        /list
        /all mensaje
        /msg usuario mensaje
        RESOLVER 5+3*2
        """);
    }

    // Método que interpreta y ejecuta los comandos del cliente
    private void procesar(String mensaje) {

        // Devuelve fecha y hora actual
        if (mensaje.equalsIgnoreCase("/hora")) {
            salida.println("Hora: " + LocalDateTime.now());
        }

        // Lista todos los clientes conectados
        else if (mensaje.equalsIgnoreCase("/list")) {
            String lista = "Conectados: ";
            for (ClienteHilo c : Servidor.clientes) {
                lista += c.nombre + " ";
            }
            salida.println(lista);
        }

        // Envía un mensaje a todos los clientes
        else if (mensaje.startsWith("/all ")) {
            String msg = mensaje.substring(5);

            for (ClienteHilo c : Servidor.clientes) {
                c.salida.println(nombre + ": " + msg);
            }
        }

        // Envía un mensaje privado a un cliente específico
        else if (mensaje.startsWith("/msg ")) {
            String[] partes = mensaje.split(" ", 3);

            // Verifica formato correcto
            if (partes.length < 3) {
                salida.println("Formato incorrecto");
                return;
            }

            String destino = partes[1];
            String msg = partes[2];

            boolean encontrado = false;

            // Busca el cliente destino
            for (ClienteHilo c : Servidor.clientes) {
                if (c.nombre.equals(destino)) {
                    c.salida.println("(Privado) " + nombre + ": " + msg);
                    encontrado = true;
                    break;
                }
            }

            // Si no existe, avisa al emisor
            if (!encontrado) {
                salida.println("Usuario no existe");
            }
        }

        // Resuelve una operación matemática
        else if (mensaje.startsWith("RESOLVER ")) {
            try {
                String operacion = mensaje.substring(9);
                double resultado = Servidor.evaluar(operacion);
                salida.println("Resultado: " + resultado);
            } catch (Exception e) {
                salida.println("Error en la operación");
            }
        }

        // Si no coincide con ningún comando
        else {
            salida.println("No entiendo el comando");
        }
    }
}