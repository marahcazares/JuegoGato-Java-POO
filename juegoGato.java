import java.util.*;

// Clase Jugador
class Jugador {
    private String nombre;
    private int juegosGanados;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.juegosGanados = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void ganarJuego() {
        juegosGanados++;
    }
}

// Clase Historial
class Historial {
    public List<String> resultados = new ArrayList<>();

    public void agregarResultado(String resultado) {
        resultados.add(resultado);
    }

    public void mostrarHistorial() {
        System.out.println("\nHistorial de juegos:");
        for (String r : resultados) {
            System.out.println(r);
        }
    }
}

// Clase Administrador
class Administrador {
    public void entregarBoletos(Jugador jugador) {
        System.out.println("🎟️ Se entregan boletos al ganador: " + jugador.getNombre());
    }

    public void entregarBoletoPerdedor(Jugador jugador) {
        if (jugador != null) {
            System.out.println("🎟️ Se entrega boleto de consolación a: " + jugador.getNombre());
        }
    }
}

// Clase principal JuegoGato
class JuegoGato {
    private Jugador jugador1;
    private Jugador jugador2;
    private String[][] tablero;
    private Historial historial;
    private Administrador admin;

    public JuegoGato(Jugador jugador1, Jugador jugador2, Historial historial, Administrador admin) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.historial = historial;
        this.admin = admin;
        this.tablero = new String[3][3];
        inicializarTablero();
    }

    private void inicializarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = "-";
            }
        }
    }

    public void jugar() {
        Scanner scanner = new Scanner(System.in);
        boolean turnoJugador1 = true;
        boolean hayGanador = false;
        int movimientos = 0;
        Jugador jugadorGanador = null;
        Jugador jugadorPerdedor = null;

        while (!hayGanador && movimientos < 9) {
            mostrarTablero();
            Jugador jugadorActual = turnoJugador1 ? jugador1 : jugador2;

            int fila, columna;
            System.out.println(jugadorActual.getNombre() + ", ingresa fila (0-2): ");
            fila = scanner.nextInt();
            System.out.println("Ingresa columna (0-2): ");
            columna = scanner.nextInt();

            if (fila < 0 || fila > 2 || columna < 0 || columna > 2) {
                System.out.println("Movimiento inválido.");
                continue;
            }

            if (!tablero[fila][columna].equals("-")) {
                System.out.println("Casilla ocupada.");
                continue;
            }

            tablero[fila][columna] = turnoJugador1 ? "X" : "O";
            movimientos++;

            if (verificarGanador()) {
                mostrarTablero();
                System.out.println(jugadorActual.getNombre() + " ganó!");
                jugadorActual.ganarJuego();
                jugadorGanador = jugadorActual;
                jugadorPerdedor = turnoJugador1 ? jugador2 : jugador1;

                historial.agregarResultado(jugadorGanador.getNombre() + " ganó.");

                admin.entregarBoletos(jugadorGanador);
                hayGanador = true;
            }

            turnoJugador1 = !turnoJugador1;
        }

        if (!hayGanador) {
            mostrarTablero();
            System.out.println("Empate.");
            historial.agregarResultado("Empate.");
        }

        if (jugadorPerdedor != null) {
            admin.entregarBoletoPerdedor(jugadorPerdedor);
        }
    }

    private boolean verificarGanador() {
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0].equals(tablero[i][1]) &&
                tablero[i][1].equals(tablero[i][2]) &&
                !tablero[i][0].equals("-")) return true;
        }

        for (int i = 0; i < 3; i++) {
            if (tablero[0][i].equals(tablero[1][i]) &&
                tablero[1][i].equals(tablero[2][i]) &&
                !tablero[0][i].equals("-")) return true;
        }

        if (tablero[0][0].equals(tablero[1][1]) &&
            tablero[1][1].equals(tablero[2][2]) &&
            !tablero[0][0].equals("-")) return true;

        if (tablero[0][2].equals(tablero[1][1]) &&
            tablero[1][1].equals(tablero[2][0]) &&
            !tablero[0][2].equals("-")) return true;

        return false;
    }

    public void mostrarTablero() {
        System.out.println("\nTablero:");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
    }
}

// MAIN para ejecutar
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nombre del jugador 1: ");
        Jugador j1 = new Jugador(scanner.nextLine());

        System.out.print("Nombre del jugador 2: ");
        Jugador j2 = new Jugador(scanner.nextLine());

        Historial historial = new Historial();
        Administrador admin = new Administrador();

        JuegoGato juego = new JuegoGato(j1, j2, historial, admin);
        juego.jugar();

        historial.mostrarHistorial();
    }
}