package pit2027;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests del GestorAlquileres. Solo se prueban los métodos de lógica;
 * los menús de consola (que leen del teclado) no se prueban aquí.
 */
public class GestorAlquileresTest {

    private GestorAlquileres gestor;
    private Cliente clara;   // cliente 001
    private Cliente aitor;   // cliente 002
    private LocalDate hoy;

    // El gestor trae 6 clientes y 9 vehículos de ejemplo
    @BeforeEach
    void preparar() {
        gestor = new GestorAlquileres();
        clara = gestor.obtenerClientes()[0];
        aitor = gestor.obtenerClientes()[1];
        hoy = LocalDate.of(2026, 11, 2);
    }

    // Método auxiliar: registra un alquiler de 3 días del vehículo número "i" para un cliente
    private Alquiler alquilar(Cliente cliente, int i) {
        Vehiculo vehiculo = gestor.obtenerVehiculos()[i];
        Alquiler alquiler = new Alquiler(vehiculo, cliente, hoy, hoy.plusDays(2));
        gestor.agregarAlquiler(alquiler);
        vehiculo.setDisponible(false);
        return alquiler;
    }

    // ---------- Datos iniciales ----------

    @Test
    @DisplayName("El gestor se crea con 6 clientes y 9 vehículos de ejemplo")
    void datosIniciales() {
        assertEquals(6, gestor.obtenerClientes().length);
        assertEquals(9, gestor.obtenerVehiculos().length);
    }

    // ---------- obtenerVehiculos(tipo) y obtenerVehiculos(tipo, categoria) ----------

    @Test
    @DisplayName("obtenerVehiculos(tipo) devuelve 3 vehículos de cada tipo")
    void vehiculosPorTipo() {
        assertEquals(3, gestor.obtenerVehiculos("Turismo").length);
        assertEquals(3, gestor.obtenerVehiculos("Motocicleta").length);
        assertEquals(3, gestor.obtenerVehiculos("VehiculoIndustrial").length);
    }

    @Test
    @DisplayName("obtenerVehiculos(tipo, categoria) filtra por tipo y por categoría")
    void vehiculosPorTipoYCategoria() {
        Vehiculo[] resultado = gestor.obtenerVehiculos("Turismo", Categoria.NORMAL);

        assertEquals(1, resultado.length);
        assertEquals("TNA2020", resultado[0].getMatricula());

        resultado = gestor.obtenerVehiculos("Motocicleta", Categoria.SUPERIOR);
        assertEquals(1, resultado.length);
        assertEquals("MSA2021", resultado[0].getMatricula());

        resultado = gestor.obtenerVehiculos("VehiculoIndustrial", Categoria.ESPECIAL);
        assertEquals(1, resultado.length);
        assertEquals("CEA2025", resultado[0].getMatricula());
    }

    @Test
    @DisplayName("obtenerVehiculos(tipo, categoria) con un tipo que no existe devuelve un array vacío")
    void vehiculosTipoInexistente() {
        assertEquals(0, gestor.obtenerVehiculos("Bicicleta", Categoria.NORMAL).length);
    }

    // ---------- obtenerAlquileres(cliente) ----------

    @Test
    @DisplayName("Un cliente sin alquileres tiene un histórico vacío")
    void historicoVacio() {
        assertEquals(0, gestor.obtenerAlquileres(clara).length);
    }

    @Test
    @DisplayName("obtenerAlquileres(cliente) devuelve solo los alquileres de ese cliente")
    void historicoDeUnCliente() {
        alquilar(clara, 0);
        alquilar(aitor, 1);
        alquilar(clara, 2);

        Alquiler[] deClara = gestor.obtenerAlquileres(clara);
        Alquiler[] deAitor = gestor.obtenerAlquileres(aitor);

        assertEquals(2, deClara.length);
        assertEquals(1, deAitor.length);
        assertEquals("002", deAitor[0].getCliente().getDni());
    }

    @Test
    @DisplayName("obtenerAlquileres con cliente null devuelve un array vacío")
    void historicoClienteNull() {
        alquilar(clara, 0);

        assertEquals(0, gestor.obtenerAlquileres(null).length);
    }

    // ---------- esClienteFrecuente ----------

    @Test
    @DisplayName("Es cliente frecuente si tiene al menos 'umbral' alquileres")
    void clienteFrecuente() {
        assertFalse(gestor.esClienteFrecuente(clara, 3));

        alquilar(clara, 0);
        alquilar(clara, 1);
        assertFalse(gestor.esClienteFrecuente(clara, 3));

        alquilar(clara, 2);
        assertTrue(gestor.esClienteFrecuente(clara, 3));
        // los alquileres de otro cliente no cuentan
        assertFalse(gestor.esClienteFrecuente(aitor, 3));
    }

    // ---------- alquileres pendientes de entrega ----------

    @Test
    @DisplayName("Los alquileres con vehículo sin entregar están pendientes")
    void alquileresPendientes() {
        Alquiler a1 = alquilar(clara, 0);
        alquilar(clara, 1);

        assertEquals(2, gestor.obtenerAlquileresPendientes(clara).length);

        // al entregar el primer vehículo ya no está pendiente
        a1.getVehiculo().setDisponible(true);
        Alquiler[] pendientes = gestor.obtenerAlquileresPendientes(clara);
        assertEquals(1, pendientes.length);
        assertEquals("MNA2021", pendientes[0].getVehiculo().getMatricula());
    }

    @Test
    @DisplayName("Si un vehículo se alquila otra vez, solo el último alquiler está pendiente")
    void soloElUltimoAlquilerEstaPendiente() {
        Alquiler primero = alquilar(clara, 0);
        primero.getVehiculo().setDisponible(true); // lo entrega
        alquilar(clara, 0);                         // lo vuelve a alquilar

        assertEquals(2, gestor.obtenerAlquileres(clara).length);
        assertEquals(1, gestor.obtenerAlquileresPendientes(clara).length);
    }
}
