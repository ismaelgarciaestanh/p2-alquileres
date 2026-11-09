package pit2027;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests de la factoría de políticas de precios.
 */
public class FactoriaCalculadoresPrecioTest {

    @Test
    @DisplayName("Cada política devuelve su calculador")
    void calculadorPorPolitica() {
        assertTrue(FactoriaCalculadoresPrecio.getCalculador(PoliticasDePrecios.Normal) instanceof CalculadorPrecioNormal);
        assertTrue(FactoriaCalculadoresPrecio.getCalculador(PoliticasDePrecios.TemporadaAlta) instanceof CalculadorPrecioTemporadaAlta);
        assertTrue(FactoriaCalculadoresPrecio.getCalculador(PoliticasDePrecios.Oferton) instanceof CalculadorPrecioOferton);
        assertTrue(FactoriaCalculadoresPrecio.getCalculador(PoliticasDePrecios.ClienteFrecuente) instanceof CalculadorPrecioClienteFrecuente);
    }

    @Test
    @DisplayName("Se puede crear el calculador a partir del nombre de la política")
    void calculadorPorNombre() {
        assertTrue(FactoriaCalculadoresPrecio.getCalculador("Normal") instanceof CalculadorPrecioNormal);
        assertTrue(FactoriaCalculadoresPrecio.getCalculador("TemporadaAlta") instanceof CalculadorPrecioTemporadaAlta);
        assertTrue(FactoriaCalculadoresPrecio.getCalculador("Oferton") instanceof CalculadorPrecioOferton);
        assertTrue(FactoriaCalculadoresPrecio.getCalculador("ClienteFrecuente") instanceof CalculadorPrecioClienteFrecuente);
    }

    @Test
    @DisplayName("Un nombre de política desconocido lanza IllegalArgumentException")
    void nombreDesconocido() {
        assertThrows(IllegalArgumentException.class, () -> FactoriaCalculadoresPrecio.getCalculador("Inventada"));
    }

    @Test
    @DisplayName("El nombre es sensible a mayúsculas (oferton no es Oferton)")
    void nombreSensibleAMayusculas() {
        assertThrows(IllegalArgumentException.class, () -> FactoriaCalculadoresPrecio.getCalculador("oferton"));
    }

    @Test
    @DisplayName("Política null o nombre null lanzan IllegalArgumentException")
    void valoresNull() {
        assertThrows(IllegalArgumentException.class, () -> FactoriaCalculadoresPrecio.getCalculador((PoliticasDePrecios) null));
        assertThrows(IllegalArgumentException.class, () -> FactoriaCalculadoresPrecio.getCalculador((String) null));
    }

    @Test
    @DisplayName("El calculador creado por la factoría calcula bien el precio")
    void calculaPrecio() {
        Vehiculo turismo = new Turismo("A", Categoria.NORMAL, 2020, 5, 400.0); // base 50
        LocalDate inicio = LocalDate.of(2026, 11, 2);

        ICalculadorPrecio c = FactoriaCalculadoresPrecio.getCalculador("TemporadaAlta");

        assertEquals(300, c.calcularPrecioAlquiler(inicio, inicio.plusDays(2), turismo)); // 3 días x 100
    }

    @Test
    @DisplayName("Alquiler usa la factoría: el precio coincide con el de cada estrategia")
    void alquilerUsaLaFactoria() {
        Vehiculo turismo = new Turismo("A", Categoria.NORMAL, 2020, 5, 400.0);
        Cliente cliente = new Cliente("Ana", "005");
        LocalDate inicio = LocalDate.of(2026, 11, 2);
        LocalDate fin = inicio.plusDays(9); // 10 días

        for (PoliticasDePrecios politica : PoliticasDePrecios.values()) {
            Alquiler alquiler = new Alquiler(turismo, cliente, inicio, fin, politica);
            int esperado = FactoriaCalculadoresPrecio.getCalculador(politica).calcularPrecioAlquiler(inicio, fin, turismo);
            assertEquals(esperado, alquiler.calcularPrecio());
        }
    }
}
