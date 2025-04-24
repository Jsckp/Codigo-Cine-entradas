import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.InputMismatchException;

class Sala implements Comparable<Sala> {
    private int id;
    private int capacidad;
    private int entradasVendidas;

    public Sala(int id, int capacidad) {
        this.id = id;
        this.capacidad = capacidad;
        this.entradasVendidas = 0;
    }

    public int getId() {
        return id;
    }

    public int getDisponibilidad() {
        return capacidad - entradasVendidas;
    }

    public boolean venderEntradas(int cantidad) {
        if (cantidad <= 0 || cantidad > 4) {
            System.out.println("Error: Cantidad de entradas no válida. Debe ser entre 1 y 4.");
            return false;
        }

        if (entradasVendidas + cantidad > capacidad) {
            System.out.println("Error: No hay suficientes asientos disponibles en la sala " + id);
            return false;
        }

        entradasVendidas += cantidad;
        System.out.println("Vendidas " + cantidad + " entradas para la sala " + id);
        return true;
    }

    @Override
    public int compareTo(Sala otra) {
        // Ordenar por disponibilidad (mayor disponibilidad primero)
        return Integer.compare(otra.getDisponibilidad(), this.getDisponibilidad());
    }

    @Override
    public String toString() {
        return "Sala " + id + ": " + entradasVendidas + " vendidas, " + getDisponibilidad() + " disponibles";
    }
}

public class Cinee {
    private PriorityQueue<Sala> salas;
    private Scanner scanner;

    public Cinee() {
        salas = new PriorityQueue<>();
        scanner = new Scanner(System.in);
        // Crear 3 salas con capacidad de 25 asientos cada una
        salas.add(new Sala(1, 25));
        salas.add(new Sala(2, 25));
        salas.add(new Sala(3, 25));
    }

    public Sala buscarSala(int id) {
        for (Sala sala : salas) {
            if (sala.getId() == id) {
                return sala;
            }
        }
        return null;
    }

    public void venderEntradas() {
        System.out.println("\n--- Venta de entradas ---");
        mostrarEstadoSalas();

        try {
            System.out.print("Seleccione la sala (1-3): ");
            int idSala = scanner.nextInt();

            Sala salaSeleccionada = buscarSala(idSala);
            if (salaSeleccionada == null) {
                System.out.println("Error: Sala no válida.");
                return;
            }

            System.out.print("Ingrese la cantidad de entradas a comprar (1-4): ");
            int cantidad = scanner.nextInt();

            if (cantidad <= 0 || cantidad > 4) {
                System.out.println("Error: La cantidad de entradas debe ser entre 1 y 4.");
                return;
            }

            // Quitamos la sala por un momento para actualizar su prioridad
            salas.remove(salaSeleccionada);

            if (salaSeleccionada.venderEntradas(cantidad)) {
                // Volvemos a añadir la sala para que se reordene en la cola
                salas.add(salaSeleccionada);
            } else {
                // Si no se vendio, la volvemos a agregar como estaba
                salas.add(salaSeleccionada);
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar un número válido.");
            scanner.next(); // Limpiamos el buffer
        }
    }

    public void mostrarEstadoSalas() {
        System.out.println("\n--- Estado de las salas (ordenadas por disponibilidad) ---");
        // Creamos una copia para no cambiar la cola original
        PriorityQueue<Sala> copia = new PriorityQueue<>(salas);
        while (!copia.isEmpty()) {
            System.out.println(copia.poll());
        }
    }

    public void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Vender entradas");
            System.out.println("2. Mostrar estado de salas");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        venderEntradas();
                        break;
                    case 2:
                        mostrarEstadoSalas();
                        break;
                    case 3:
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número válido.");
                scanner.next();
                opcion = 0;
            }

        } while (opcion != 3);
        scanner.close();
    }

    public static void main(String[] args) {
        Cinee cine = new Cinee();
        cine.mostrarMenu();
    }
}


