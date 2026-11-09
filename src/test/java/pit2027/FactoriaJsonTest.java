package pit2027;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests de serialización (toString) y deserialización (factorías) de clientes,
 * vehículos y alquileres. La idea de casi todos es: serializar, deserializar
 * y comprobar que se recupera lo mismo.
 */
public class FactoriaJsonTest {

    // ---------- Clientes ----------

    @Test
    @DisplayName("Cliente: serializar y deserializar recupera los mismos datos")
    void clienteIdaYVuelta() {
        Cliente original = new Cliente("Clara Videncia", "001");

        Cliente copia = FactoriaClientes.getCliente(original.toString());

        assertEquals("Clara Videncia", copia.getNombre());
        assertEquals("001", copia.getDni());
    }

    @Test
    @DisplayName("Cliente: un JSON sin el campo dni lanza excepción")
    void clienteJsonIncompleto() {
        assertThrows(JSONException.class, () -> FactoriaClientes.getCliente("{\"nombre\":\"Ana\"}"));
    }

    // ---------- Vehículos ----------

    @Test
    @DisplayName("Factoría: crea un Turismo con todos sus atributos")
    void turismoIdaYVuelta() {
        Vehiculo original = new Turismo("1234ABC", Categoria.ESPECIAL, 2026, 7, 650.0);

        Vehiculo copia = FactoriaVehiculos.getVehiculo(original.toString());

        assertTrue(copia instanceof Turismo);
        Turismo t = (Turismo) copia;
        assertEquals("1234ABC", t.getMatricula());
        assertEquals(Categoria.ESPECIAL, t.getCategoria());
        assertEquals(2026, t.getAnnio());
        assertEquals(7, t.getPlazas());
        assertEquals(650.0, t.getCapacidadMaletero(), 0.001);
        assertEquals("Turismo", t.getTipo());
    }

    @Test
    @DisplayName("Factoría: crea una Motocicleta con todos sus atributos")
    void motocicletaIdaYVuelta() {
        Vehiculo original = new Motocicleta("MNA2021", Categoria.NORMAL, 2021, 200);

        Vehiculo copia = FactoriaVehiculos.getVehiculo(original.toString());

        assertTrue(copia instanceof Motocicleta);
        assertEquals(200, ((Motocicleta) copia).getCilindrada());
        assertEquals("MNA2021", copia.getMatricula());
    }

    @Test
    @DisplayName("Factoría: crea un VehiculoIndustrial con todos sus atributos")
    void industrialIdaYVuelta() {
        Vehiculo original = new VehiculoIndustrial("CSA2025", Categoria.SUPERIOR, 2025, 2400, 3);

        Vehiculo copia = FactoriaVehiculos.getVehiculo(original.toString());

        assertTrue(copia instanceof VehiculoIndustrial);
        VehiculoIndustrial c = (VehiculoIndustrial) copia;
        assertEquals(2400, c.getCapacidadCarga());
        assertEquals(3, c.getNumeroEjes());
        assertEquals(Categoria.SUPERIOR, c.getCategoria());
    }

    @Test
    @DisplayName("Factoría: se conserva la disponibilidad del vehículo")
    void conservaDisponibilidad() {
        Vehiculo original = new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 450.0);
        original.setDisponible(false);

        Vehiculo copia = FactoriaVehiculos.getVehiculo(original.toString());

        assertFalse(copia.isDisponible());
    }

    @Test
    @DisplayName("Factoría: el vehículo reconstruido tiene el mismo precio base")
    void conservaPrecioBase() {
        Vehiculo original = new Motocicleta("M", Categoria.SUPERIOR, 2021, 500);

        assertEquals(original.getPrecioBase(), FactoriaVehiculos.getVehiculo(original.toString()).getPrecioBase());
    }

    @Test
    @DisplayName("Factoría: un tipo desconocido lanza IllegalArgumentException")
    void tipoDesconocido() {
        String json = "{\"tipo\":\"Bicicleta\",\"matricula\":\"X\",\"categoria\":\"NORMAL\",\"annio\":2020,\"disponible\":true}";

        assertThrows(IllegalArgumentException.class, () -> FactoriaVehiculos.getVehiculo(json));
    }

    @Test
    @DisplayName("Factoría: un JSON sin campo tipo lanza JSONException")
    void sinTipo() {
        assertThrows(JSONException.class, () -> FactoriaVehiculos.getVehiculo("{\"matricula\":\"X\"}"));
    }

    @Test
    @DisplayName("Factoría: un texto que no es JSON lanza JSONException")
    void textoNoJson() {
        assertThrows(JSONException.class, () -> FactoriaVehiculos.getVehiculo("esto no es json"));
    }

    // ---------- Alquileres ----------

    @Test
    @DisplayName("Alquiler: serializar y deserializar recupera cliente, vehículo, fechas y política")
    void alquilerIdaYVuelta() {
        Vehiculo vehiculo = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        Cliente cliente = new Cliente("Juan Pérez", "123456789");
        LocalDate inicio = LocalDate.of(2026, 11, 2);
        Alquiler original = new Alquiler(vehiculo, cliente, inicio, inicio.plusDays(5), PoliticasDePrecios.Oferton);

        Alquiler copia = FactoriaAlquileres.getAlquiler(original.toString());

        assertEquals("Juan Pérez", copia.getCliente().getNombre());
        assertEquals("ABC123", copia.getVehiculo().getMatricula());
        assertEquals("Turismo", copia.getVehiculo().getTipo());
        assertEquals(inicio, copia.getFechaInicio());
        assertEquals(inicio.plusDays(5), copia.getFechaFin());
        assertEquals(PoliticasDePrecios.Oferton, copia.getPolitica());
        assertEquals(original.calcularPrecio(), copia.calcularPrecio());
    }

    @Test
    @DisplayName("Alquiler: si el JSON no trae política se usa la Normal")
    void alquilerSinPolitica() {
        String json = "{\"cliente\":{\"nombre\":\"Ana\",\"dni\":\"005\"},"
                + "\"vehiculo\":{\"tipo\":\"Motocicleta\",\"matricula\":\"M1\",\"categoria\":\"NORMAL\","
                + "\"annio\":2021,\"disponible\":true,\"cilindrada\":200},"
                + "\"fechaInicio\":\"2026-11-02\",\"fechaFin\":\"2026-11-03\"}";

        Alquiler alquiler = FactoriaAlquileres.getAlquiler(json);

        assertEquals(PoliticasDePrecios.Normal, alquiler.getPolitica());
        assertEquals(60, alquiler.calcularPrecio()); // 2 días x 30 euros
    }

    @Test
    @DisplayName("Alquiler: una política desconocida lanza excepción")
    void alquilerPoliticaDesconocida() {
        String json = "{\"cliente\":{\"nombre\":\"Ana\",\"dni\":\"005\"},"
                + "\"vehiculo\":{\"tipo\":\"Motocicleta\",\"matricula\":\"M1\",\"categoria\":\"NORMAL\","
                + "\"annio\":2021,\"disponible\":true,\"cilindrada\":200},"
                + "\"fechaInicio\":\"2026-11-02\",\"fechaFin\":\"2026-11-03\",\"politica\":\"Inventada\"}";

        assertThrows(IllegalArgumentException.class, () -> FactoriaAlquileres.getAlquiler(json));
    }
}
