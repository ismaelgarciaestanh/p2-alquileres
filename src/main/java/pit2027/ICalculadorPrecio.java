package pit2027;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Interfaz de la ESTRATEGIA del patrón Estrategia.
 * Cada política de precios (normal, temporada alta, ofertón, cliente frecuente)
 * es una clase que implementa esta interfaz y calcula el precio a su manera.
 * El contexto (la clase Alquiler) solo conoce esta interfaz, no las clases concretas.
 */
public interface ICalculadorPrecio {

    /**
     * Calcula el precio total de un alquiler.
     * @param fechaInicio primer día del alquiler
     * @param fechaFin último día del alquiler
     * @param vehiculo vehículo alquilado (de él se obtiene el precio base diario)
     * @return precio total del alquiler (entero)
     */
    int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo);

    /**
     * Cuenta los días del alquiler. Se cuenta también el día de inicio
     * (de hoy a hoy es 1 día; de hoy a dentro de 3 días son 4 días).
     * Es un método "default": lo heredan todas las estrategias y así no repetimos código.
     * @throws IllegalArgumentException si alguna fecha es null o si la fecha fin es anterior a la de inicio
     */
    default long contarDias(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser null");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio");
        }
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
    }
}
