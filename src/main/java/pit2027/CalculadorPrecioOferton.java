package pit2027;

import java.time.LocalDate;

/**
 * Política ofertón: 30% de descuento sobre el precio base y, si el alquiler
 * es de más de 7 días, un 10% de descuento adicional sobre el total.
 */
public class CalculadorPrecioOferton implements ICalculadorPrecio {

    @Override
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo) {
        long dias = contarDias(fechaInicio, fechaFin);

        // 30% de descuento: se paga el 70% del precio base cada día
        double total = vehiculo.getPrecioBase() * 0.7 * dias;

        // Más de 7 días: 10% de descuento extra sobre el total
        if (dias > 7) {
            total = total * 0.9;
        }
        return (int) Math.round(total);
    }
}
