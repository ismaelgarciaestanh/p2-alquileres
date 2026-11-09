package pit2027;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests unitarios de Vehiculo y sus subclases (Turismo, Motocicleta, VehiculoIndustrial).
 */
public class VehiculoTest {

    @Test
    @DisplayName("Un vehículo nuevo está disponible y se puede cambiar su disponibilidad")
    void disponibilidad() {
        Vehiculo v = new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 450.0);

        assertTrue(v.isDisponible());
        v.setDisponible(false);
        assertFalse(v.isDisponible());
    }

    @Test
    @DisplayName("getTipo devuelve el nombre de la subclase")
    void tipos() {
        assertEquals("Turismo", new Turismo("A", Categoria.NORMAL, 2020, 5, 450.0).getTipo());
        assertEquals("Motocicleta", new Motocicleta("B", Categoria.NORMAL, 2020, 200).getTipo());
        assertEquals("VehiculoIndustrial", new VehiculoIndustrial("C", Categoria.NORMAL, 2020, 1000, 2).getTipo());
    }

    @Test
    @DisplayName("Precio base del turismo según categoría")
    void precioBaseTurismo() {
        assertEquals(50, new Turismo("A", Categoria.NORMAL, 2020, 5, 450.0).getPrecioBase());
        assertEquals(80, new Turismo("A", Categoria.ESPECIAL, 2020, 5, 450.0).getPrecioBase());
        assertEquals(100, new Turismo("A", Categoria.SUPERIOR, 2020, 5, 450.0).getPrecioBase());
    }

    @Test
    @DisplayName("Precio base de la motocicleta según categoría")
    void precioBaseMotocicleta() {
        assertEquals(30, new Motocicleta("B", Categoria.NORMAL, 2020, 200).getPrecioBase());
        assertEquals(50, new Motocicleta("B", Categoria.ESPECIAL, 2020, 200).getPrecioBase());
        assertEquals(70, new Motocicleta("B", Categoria.SUPERIOR, 2020, 200).getPrecioBase());
    }

    @Test
    @DisplayName("Precio base del vehículo industrial según categoría")
    void precioBaseIndustrial() {
        assertEquals(80, new VehiculoIndustrial("C", Categoria.NORMAL, 2020, 1000, 2).getPrecioBase());
        assertEquals(120, new VehiculoIndustrial("C", Categoria.ESPECIAL, 2020, 1000, 2).getPrecioBase());
        assertEquals(160, new VehiculoIndustrial("C", Categoria.SUPERIOR, 2020, 1000, 2).getPrecioBase());
    }

    @Test
    @DisplayName("Los getters específicos de cada subclase funcionan")
    void gettersEspecificos() {
        Turismo t = new Turismo("A", Categoria.NORMAL, 2020, 5, 450.0);
        assertEquals(5, t.getPlazas());
        assertEquals(450.0, t.getCapacidadMaletero(), 0.001);

        Motocicleta m = new Motocicleta("B", Categoria.NORMAL, 2021, 200);
        assertEquals(200, m.getCilindrada());

        VehiculoIndustrial c = new VehiculoIndustrial("C", Categoria.NORMAL, 2019, 1400, 2);
        assertEquals(1400, c.getCapacidadCarga());
        assertEquals(2, c.getNumeroEjes());
    }

    @Test
    @DisplayName("toString serializa a JSON incluyendo el tipo y el precio base")
    void toStringJson() {
        Vehiculo v = new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 450.0);

        JSONObject json = new JSONObject(v.toString());

        assertEquals("Turismo", json.getString("tipo"));
        assertEquals("1234ABC", json.getString("matricula"));
        assertEquals("NORMAL", json.getString("categoria"));
        assertEquals(50, json.getInt("precioBase"));
        assertTrue(json.getBoolean("disponible"));
    }
}
