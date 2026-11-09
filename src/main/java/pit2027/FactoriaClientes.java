package pit2027;

/**
 * MÉTODO FACTORÍA para clientes.
 * Solo hay una clase de cliente, pero así todas las clases se crean desde JSON de la misma forma.
 */
public class FactoriaClientes {

    /**
     * Crea un cliente a partir de su representación JSON.
     * @param jsonString JSON del cliente
     * @return el cliente
     */
    public static Cliente getCliente(String jsonString) {
        return new Cliente(jsonString);
    }

    /**
     * Ejemplo de uso: serializar un cliente y reconstruirlo.
     */
    public static void main(String[] args) {
        Cliente c1 = new Cliente("Juan Pérez", "12345678A");
        String serializado = c1.toString();
        System.out.println("Serializado: " + serializado);

        Cliente c2 = FactoriaClientes.getCliente(serializado);
        System.out.println("Deserializado: " + c2);
    }
}
