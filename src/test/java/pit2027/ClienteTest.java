package pit2027;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests unitarios de la clase Cliente.
 */
public class ClienteTest {

    @Test
    @DisplayName("Los getters devuelven los datos con los que se creó el cliente")
    void gettersDevuelvenLosDatos() {
        Cliente cliente = new Cliente("Juan Pérez", "12345678A");

        assertEquals("Juan Pérez", cliente.getNombre());
        assertEquals("12345678A", cliente.getDni());
    }

    @Test
    @DisplayName("toString devuelve un JSON con nombre y dni")
    void toStringDevuelveJson() {
        Cliente cliente = new Cliente("Juan Pérez", "12345678A");

        JSONObject json = new JSONObject(cliente.toString());

        assertEquals("Juan Pérez", json.getString("nombre"));
        assertEquals("12345678A", json.getString("dni"));
    }
}
