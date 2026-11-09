package pit2027;

import java.time.LocalDate;

/**
 * Política normal: precio base x número de días de alquiler.
 */
public class CalculadorPrecioNormal implements ICalculadorPrecio {

    @Override
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo) {
        long dias = contarDias(fechaInicio, fechaFin);
        return (int) (vehiculo.getPrecioBase() * dias);
    }
}
