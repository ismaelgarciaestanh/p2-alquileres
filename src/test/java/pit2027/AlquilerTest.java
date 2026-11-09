package pit2027;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests unitarios de la clase Alquiler.
 */
public class AlquilerTest {

    private Vehiculo vehiculo;
    private Cliente cliente;
    private LocalDate inicio;

    // Se ejecuta antes de cada test: así todos parten de los mismos datos
    @BeforeEach
    void preparar() {
        vehiculo = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        cliente = new Cliente("Juan Pérez", "123456789");
        inicio = LocalDate.of(2026, 11, 2);
    }

    @Test
    @DisplayName("Los getters devuelven los datos del alquiler")
    void getters() {
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, inicio.plusDays(3));

        assertSame(vehiculo, alquiler.getVehiculo());
        assertSame(cliente, alquiler.getCliente());
        assertEquals(inicio, alquiler.getFechaInicio());
        assertEquals(inicio.plusDays(3), alquiler.getFechaFin());
    }

    @Test
    @DisplayName("Un alquiler de un solo día cuesta un día de precio base")
    void precioUnDia() {
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, inicio);

        assertEquals(Alquiler.PRECIO_BASE, alquiler.calcularPrecioAlquiler(inicio, inicio));
    }

    @Test
    @DisplayName("Un alquiler de inicio + 3 días cuenta 4 días (incluye el día de inicio)")
    void precioVariosDias() {
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, inicio.plusDays(3));

        assertEquals(4 * Alquiler.PRECIO_BASE, alquiler.calcularPrecioAlquiler(inicio, inicio.plusDays(3)));
    }

    @Test
    @DisplayName("toString serializa el alquiler a JSON con cliente, vehículo y fechas")
    void toStringJson() {
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, inicio.plusDays(3));

        JSONObject json = new JSONObject(alquiler.toString());

        assertEquals("2026-11-02", json.getString("fechaInicio"));
        assertEquals("2026-11-05", json.getString("fechaFin"));
        assertEquals("Juan Pérez", json.getJSONObject("cliente").getString("nombre"));
        assertEquals("ABC123", json.getJSONObject("vehiculo").getString("matricula"));
    }

    @Test
    @DisplayName("agregarAlquiler guarda el alquiler en el array estático")
    void agregarAlquiler() {
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, inicio.plusDays(1));

        Alquiler.agregarAlquiler(alquiler);

        boolean encontrado = false;
        for (Alquiler a : Alquiler.obtenerAlquileres()) {
            if (a == alquiler) {
                encontrado = true;
            }
        }
        assertTrue(encontrado);
    }

    // ---------- Patrón Estrategia en Alquiler ----------

    @Test
    @DisplayName("Por defecto el alquiler usa la política Normal")
    void politicaPorDefecto() {
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, inicio.plusDays(3));

        assertEquals(PoliticasDePrecios.Normal, alquiler.getPolitica());
        // 4 días x 50 euros (turismo NORMAL)
        assertEquals(200, alquiler.calcularPrecio());
    }

    @Test
    @DisplayName("calcularPrecio usa la política con la que se creó el alquiler")
    void calcularPrecioSegunPolitica() {
        LocalDate fin = inicio.plusDays(3); // 4 días

        assertEquals(200, new Alquiler(vehiculo, cliente, inicio, fin, PoliticasDePrecios.Normal).calcularPrecio());
        assertEquals(400, new Alquiler(vehiculo, cliente, inicio, fin, PoliticasDePrecios.TemporadaAlta).calcularPrecio());
        assertEquals(140, new Alquiler(vehiculo, cliente, inicio, fin, PoliticasDePrecios.Oferton).calcularPrecio());
        assertEquals(150, new Alquiler(vehiculo, cliente, inicio, fin, PoliticasDePrecios.ClienteFrecuente).calcularPrecio());
    }

    @Test
    @DisplayName("calcularPrecioAlquiler con política elige la estrategia indicada")
    void calcularPrecioAlquilerConPolitica() {
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, inicio.plusDays(3));

        assertEquals(400, alquiler.calcularPrecioAlquiler(inicio, inicio.plusDays(3),
                PoliticasDePrecios.TemporadaAlta, vehiculo));
    }

    @Test
    @DisplayName("La política aparece en el JSON del alquiler")
    void politicaEnJson() {
        Alquiler alquiler = new Alquiler(vehiculo, cliente, inicio, inicio.plusDays(3), PoliticasDePrecios.Oferton);

        assertEquals("Oferton", new JSONObject(alquiler.toString()).getString("politica"));
    }
}
