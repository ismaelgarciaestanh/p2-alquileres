package pit2027;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests de las cuatro estrategias de cálculo de precio.
 * Se usa un turismo de categoría NORMAL, cuyo precio base es 50 euros al día.
 */
public class CalculadorPrecioTest {

    private Vehiculo turismo;
    private LocalDate inicio;

    @BeforeEach
    void preparar() {
        turismo = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        inicio = LocalDate.of(2026, 11, 2);
    }

    // Método auxiliar: fecha fin para un alquiler de "dias" días
    private LocalDate fin(int dias) {
        return inicio.plusDays(dias - 1);
    }

    // ---------- Normal ----------

    @Test
    @DisplayName("Normal: precio base x días")
    void normal() {
        ICalculadorPrecio c = new CalculadorPrecioNormal();

        assertEquals(50, c.calcularPrecioAlquiler(inicio, fin(1), turismo));
        assertEquals(150, c.calcularPrecioAlquiler(inicio, fin(3), turismo));
        assertEquals(500, c.calcularPrecioAlquiler(inicio, fin(10), turismo));
    }

    // ---------- Temporada alta ----------

    @Test
    @DisplayName("Temporada alta: el precio base diario se duplica")
    void temporadaAlta() {
        ICalculadorPrecio c = new CalculadorPrecioTemporadaAlta();

        assertEquals(100, c.calcularPrecioAlquiler(inicio, fin(1), turismo));
        assertEquals(300, c.calcularPrecioAlquiler(inicio, fin(3), turismo));
    }

    // ---------- Ofertón ----------

    @Test
    @DisplayName("Ofertón: 30% de descuento hasta 7 días")
    void ofertonHastaSieteDias() {
        ICalculadorPrecio c = new CalculadorPrecioOferton();

        assertEquals(35, c.calcularPrecioAlquiler(inicio, fin(1), turismo));
        assertEquals(245, c.calcularPrecioAlquiler(inicio, fin(7), turismo));
    }

    @Test
    @DisplayName("Ofertón: con más de 7 días se aplica además un 10% al total")
    void ofertonMasDeSieteDias() {
        ICalculadorPrecio c = new CalculadorPrecioOferton();

        // 8 días: 8 x 35 = 280 -> menos 10% = 252
        assertEquals(252, c.calcularPrecioAlquiler(inicio, fin(8), turismo));
        // 10 días: 10 x 35 = 350 -> menos 10% = 315
        assertEquals(315, c.calcularPrecioAlquiler(inicio, fin(10), turismo));
    }

    // ---------- Cliente frecuente ----------

    @Test
    @DisplayName("Cliente frecuente: 20% de descuento los tres primeros días")
    void frecuenteTresPrimerosDias() {
        ICalculadorPrecio c = new CalculadorPrecioClienteFrecuente();

        assertEquals(40, c.calcularPrecioAlquiler(inicio, fin(1), turismo));
        assertEquals(120, c.calcularPrecioAlquiler(inicio, fin(3), turismo));
    }

    @Test
    @DisplayName("Cliente frecuente: 40% de descuento a partir del cuarto día")
    void frecuenteDesdeCuartoDia() {
        ICalculadorPrecio c = new CalculadorPrecioClienteFrecuente();

        // 4 días: 3 x 40 + 1 x 30 = 150
        assertEquals(150, c.calcularPrecioAlquiler(inicio, fin(4), turismo));
        // 10 días: 3 x 40 + 7 x 30 = 330
        assertEquals(330, c.calcularPrecioAlquiler(inicio, fin(10), turismo));
    }

    // ---------- Otros precios base ----------

    @Test
    @DisplayName("El precio depende del precio base del vehículo (motocicleta SUPERIOR = 70)")
    void usaPrecioBaseDelVehiculo() {
        Vehiculo moto = new Motocicleta("M1", Categoria.SUPERIOR, 2021, 500);

        assertEquals(210, new CalculadorPrecioNormal().calcularPrecioAlquiler(inicio, fin(3), moto));
    }

    // ---------- Errores ----------

    @Test
    @DisplayName("Si la fecha fin es anterior a la de inicio se lanza una excepción")
    void fechasIncorrectas() {
        ICalculadorPrecio c = new CalculadorPrecioNormal();

        assertThrows(IllegalArgumentException.class,
                () -> c.calcularPrecioAlquiler(inicio, inicio.minusDays(1), turismo));
    }

    @Test
    @DisplayName("Si alguna fecha es null se lanza una excepción")
    void fechaNull() {
        ICalculadorPrecio c = new CalculadorPrecioNormal();

        assertThrows(IllegalArgumentException.class, () -> c.calcularPrecioAlquiler(null, inicio, turismo));
    }
}
