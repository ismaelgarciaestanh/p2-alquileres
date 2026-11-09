package pit2027;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.json.JSONObject;

/**
 * Modela un alquiler de un vehículo por parte de un cliente, con fechas de inicio y fin.
 * Almacena los alquileres realizados en un array estático y proporciona métodos para calcular el precio del alquiler.
 * Alquiler
 */
public class Alquiler {
    
    private Vehiculo vehiculo;
    private Cliente cliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    // Política de precios elegida para este alquiler
    private PoliticasDePrecios politica;
    // ESTRATEGIA: objeto que sabe calcular el precio según la política.
    // No tiene getter para que no aparezca en el JSON.
    private ICalculadorPrecio calculador;

    private static Alquiler[] alquileres = new Alquiler[1000];

    public static final int PRECIO_BASE = 50;
    private static int contadorAlquileres = 0;

    /**
     * Crea un alquiler con la política de precios normal.
     */
    public Alquiler(Vehiculo vehiculo, Cliente cliente, LocalDate fechaInicio, LocalDate fechaFin) {
        this(vehiculo, cliente, fechaInicio, fechaFin, PoliticasDePrecios.Normal);
    }

    /**
     * Crea un alquiler indicando la política de precios que se le aplica.
     */
    public Alquiler(Vehiculo vehiculo, Cliente cliente, LocalDate fechaInicio, LocalDate fechaFin,
            PoliticasDePrecios politica) {
        this.vehiculo = vehiculo;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.politica = politica;
        this.calculador = crearCalculador(politica);
    }

    /**
     * Elige la estrategia (calculador de precio) que corresponde a una política.
     * Es privado: solo lo usa el propio Alquiler.
     */
    private ICalculadorPrecio crearCalculador(PoliticasDePrecios politica) {
        switch (politica) {
            case TemporadaAlta:
                return new CalculadorPrecioTemporadaAlta();
            case Oferton:
                return new CalculadorPrecioOferton();
            case ClienteFrecuente:
                return new CalculadorPrecioClienteFrecuente();
            case Normal:
            default:
                return new CalculadorPrecioNormal();
        }
    }

    public static Alquiler[] obtenerAlquileres() {
        return alquileres; 
    }

    public static void agregarAlquiler(Alquiler alquiler) {
        if (contadorAlquileres < alquileres.length) {
            alquileres[contadorAlquileres] = alquiler;
            contadorAlquileres++;
        } else {
            System.out.println("No se pueden agregar más alquileres. Capacidad máxima alcanzada.");
        }
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public PoliticasDePrecios getPolitica() {
        return politica;
    }

    /**
     * Método antiguo: precio con la tarifa fija PRECIO_BASE (no usa patrones).
     * Se mantiene para no romper el código anterior.
     */
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin) {
        // +1 para contar también el día de inicio
        int dias = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
        return dias * PRECIO_BASE;
    }

    /**
     * Calcula el precio de un alquiler con la política indicada (patrón Estrategia).
     * Elige la estrategia y le delega el cálculo.
     * @param fechaInicio primer día
     * @param fechaFin último día
     * @param politica política de precios a aplicar
     * @param vehiculo vehículo alquilado
     * @return precio total
     */
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin,
            PoliticasDePrecios politica, Vehiculo vehiculo) {
        ICalculadorPrecio estrategia = crearCalculador(politica);
        return estrategia.calcularPrecioAlquiler(fechaInicio, fechaFin, vehiculo);
    }

    /**
     * Calcula el precio de ESTE alquiler con su propia política y sus propias fechas.
     * (No se llama getPrecio para que no salga en el JSON.)
     * @return precio total del alquiler
     */
    public int calcularPrecio() {
        return calculador.calcularPrecioAlquiler(fechaInicio, fechaFin, vehiculo);
    }

    @Override 
    public String toString() {
        return new JSONObject(this).toString();
    }

    /**
     * Ejemplo de uso: el mismo alquiler de 9 días con las cuatro políticas de precios.
     */
    public static void main(String[] args) {
        Vehiculo vehiculo = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        Cliente cliente = new Cliente("Juan Pérez", "123456789");
        LocalDate hoy = LocalDate.now();
        LocalDate fin = hoy.plusDays(8); // 9 días contando el de inicio

        for (PoliticasDePrecios politica : PoliticasDePrecios.values()) {
            Alquiler alquiler = new Alquiler(vehiculo, cliente, hoy, fin, politica);
            System.out.println(alquiler);
            System.out.println("Política " + politica + " -> precio: " + alquiler.calcularPrecio() + " euros");
            System.out.println("-----------------------------------------------");
        }
    }
}
