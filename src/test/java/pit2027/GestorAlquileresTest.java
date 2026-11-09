package pit2027;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
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
    private Path fichero; // fichero temporal para los tests de guardar y cargar

    // El gestor trae 6 clientes y 9 vehículos de ejemplo
    @BeforeEach
    void preparar() {
        gestor = new GestorAlquileres();
        clara = gestor.obtenerClientes()[0];
        aitor = gestor.obtenerClientes()[1];
        hoy = LocalDate.of(2026, 11, 2);
    }

    // Se ejecuta después de cada test: borra el fichero temporal si se ha creado
    @AfterEach
    void limpiar() throws IOException {
        if (fichero != null) {
            Files.deleteIfExists(fichero);
        }
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

    // ---------- Guardar y cargar datos en un fichero JSON ----------

    @Test
    @DisplayName("Guardar y cargar recupera clientes, vehículos y alquileres")
    void guardarYCargar() throws IOException {
        fichero = Files.createTempFile("alquileres", ".json");
        alquilar(clara, 0);
        alquilar(aitor, 4);
        assertTrue(gestor.guardarDatos(fichero.toString()));

        GestorAlquileres otro = new GestorAlquileres();
        assertTrue(otro.cargarDatos(fichero.toString()));

        assertEquals(6, otro.obtenerClientes().length);
        assertEquals(9, otro.obtenerVehiculos().length);
        assertEquals(1, otro.obtenerAlquileres(otro.obtenerClientes()[0]).length);
        assertEquals(1, otro.obtenerAlquileres(otro.obtenerClientes()[1]).length);
    }

    @Test
    @DisplayName("Al cargar se conservan los datos del alquiler (fechas, política y precio)")
    void cargarConservaElAlquiler() throws IOException {
        fichero = Files.createTempFile("alquileres", ".json");
        Vehiculo vehiculo = gestor.obtenerVehiculos()[3]; // turismo SUPERIOR (base 100)
        gestor.agregarAlquiler(new Alquiler(vehiculo, clara, hoy, hoy.plusDays(9), PoliticasDePrecios.Oferton));
        gestor.guardarDatos(fichero.toString());

        GestorAlquileres otro = new GestorAlquileres();
        otro.cargarDatos(fichero.toString());

        Alquiler cargado = otro.obtenerAlquileres(otro.obtenerClientes()[0])[0];
        assertEquals(hoy, cargado.getFechaInicio());
        assertEquals(hoy.plusDays(9), cargado.getFechaFin());
        assertEquals(PoliticasDePrecios.Oferton, cargado.getPolitica());
        assertEquals(630, cargado.calcularPrecio()); // 10 días x 70 euros, -10%
    }

    @Test
    @DisplayName("Al cargar, el alquiler apunta a los mismos objetos que las listas del gestor")
    void cargarEnlazaLosObjetos() throws IOException {
        fichero = Files.createTempFile("alquileres", ".json");
        alquilar(clara, 0);
        gestor.guardarDatos(fichero.toString());

        GestorAlquileres otro = new GestorAlquileres();
        otro.cargarDatos(fichero.toString());

        Alquiler cargado = otro.obtenerAlquileres(otro.obtenerClientes()[0])[0];
        assertSame(otro.obtenerClientes()[0], cargado.getCliente());
        assertSame(otro.obtenerVehiculos()[0], cargado.getVehiculo());
        assertFalse(cargado.getVehiculo().isDisponible()); // el vehículo seguía sin entregar
        assertEquals(1, otro.obtenerAlquileresPendientes(otro.obtenerClientes()[0]).length);
    }

    @Test
    @DisplayName("Cargar sustituye los datos actuales (no los duplica)")
    void cargarSustituyeLosDatos() throws IOException {
        fichero = Files.createTempFile("alquileres", ".json");
        gestor.guardarDatos(fichero.toString()); // fichero sin alquileres

        alquilar(clara, 0); // ahora el gestor tiene un alquiler que no está en el fichero
        assertTrue(gestor.cargarDatos(fichero.toString()));

        assertEquals(0, gestor.obtenerAlquileres(gestor.obtenerClientes()[0]).length);
        assertEquals(6, gestor.obtenerClientes().length);
    }

    @Test
    @DisplayName("Cargar un fichero que no existe devuelve false y no cambia los datos")
    void cargarFicheroInexistente() throws IOException {
        alquilar(clara, 0);

        assertFalse(gestor.cargarDatos("este_fichero_no_existe.json"));

        assertEquals(1, gestor.obtenerAlquileres(clara).length);
    }

    @Test
    @DisplayName("Cargar un fichero que no es JSON devuelve false y no cambia los datos")
    void cargarFicheroNoJson() throws IOException {
        fichero = Files.createTempFile("alquileres", ".json");
        Files.writeString(fichero, "esto no es json");
        alquilar(clara, 0);

        assertFalse(gestor.cargarDatos(fichero.toString()));

        assertEquals(1, gestor.obtenerAlquileres(clara).length);
        assertEquals(6, gestor.obtenerClientes().length);
    }

    @Test
    @DisplayName("Cargar un JSON al que le faltan listas devuelve false")
    void cargarJsonIncompleto() throws IOException {
        fichero = Files.createTempFile("alquileres", ".json");
        Files.writeString(fichero, "{\"clientes\":[]}");

        assertFalse(gestor.cargarDatos(fichero.toString()));
    }

    @Test
    @DisplayName("Cargar un alquiler de un cliente que no está en el fichero devuelve false")
    void cargarAlquilerConClienteDesconocido() throws IOException {
        fichero = Files.createTempFile("alquileres", ".json");
        String json = "{\"clientes\":[],\"vehiculos\":[{\"tipo\":\"Motocicleta\",\"matricula\":\"M1\","
                + "\"categoria\":\"NORMAL\",\"annio\":2021,\"disponible\":true,\"cilindrada\":200}],"
                + "\"alquileres\":[{\"cliente\":{\"nombre\":\"Ana\",\"dni\":\"999\"},"
                + "\"vehiculo\":{\"matricula\":\"M1\"},\"fechaInicio\":\"2026-11-02\",\"fechaFin\":\"2026-11-03\"}]}";
        Files.writeString(fichero, json);

        assertFalse(gestor.cargarDatos(fichero.toString()));
        assertEquals(6, gestor.obtenerClientes().length); // no se ha tocado nada
    }

    @Test
    @DisplayName("Guardar en una ruta que no existe devuelve false")
    void guardarEnRutaInvalida() {
        assertFalse(gestor.guardarDatos("carpeta_que_no_existe/datos.json"));
    }

    @Test
    @DisplayName("El fichero guardado es texto JSON con las tres listas")
    void ficheroTieneLasTresListas() throws IOException {
        fichero = Files.createTempFile("alquileres", ".json");
        gestor.guardarDatos(fichero.toString());

        String texto = Files.readString(fichero);

        assertTrue(texto.contains("\"clientes\""));
        assertTrue(texto.contains("\"vehiculos\""));
        assertTrue(texto.contains("\"alquileres\""));
        assertTrue(texto.contains("Clara Videncia"));
    }
}
