package pit2027;

import java.time.LocalDate;

/**
 * Política cliente frecuente: 20% de descuento sobre el precio base los tres
 * primeros días y 40% de descuento a partir del cuarto día.
 */
public class CalculadorPrecioClienteFrecuente implements ICalculadorPrecio {

    @Override
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo) {
        long dias = contarDias(fechaInicio, fechaFin);

        // Separamos los días en "los tres primeros" y "el resto"
        long diasPrimeros = Math.min(dias, 3);
        long diasResto = dias - diasPrimeros;

        double total = vehiculo.getPrecioBase() * 0.8 * diasPrimeros
                     + vehiculo.getPrecioBase() * 0.6 * diasResto;
        return (int) Math.round(total);
    }
}
