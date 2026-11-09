package pit2027;

import java.time.LocalDate;

/**
 * Política temporada alta: se duplica el precio base diario.
 */
public class CalculadorPrecioTemporadaAlta implements ICalculadorPrecio {

    @Override
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo) {
        long dias = contarDias(fechaInicio, fechaFin);
        return (int) (2 * vehiculo.getPrecioBase() * dias);
    }
}
