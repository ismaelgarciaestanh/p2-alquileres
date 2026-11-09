package pit2027;

import java.time.LocalDate;

/**
 * MÉTODO FACTORÍA para alquileres.
 * El alquiler contiene un cliente y un vehículo; su constructor desde JSON
 * usa a su vez las factorías de clientes y de vehículos.
 */
public class FactoriaAlquileres {

    /**
     * Crea un alquiler a partir de su representación JSON.
     * @param jsonString JSON del alquiler
     * @return el alquiler
     */
    public static Alquiler getAlquiler(String jsonString) {
        return new Alquiler(jsonString);
    }

    /**
     * Ejemplo de uso: serializar un alquiler y reconstruirlo.
     */
    public static void main(String[] args) {
        Vehiculo vehiculo = new Motocicleta("5678DEF", Categoria.SUPERIOR, 2022, 500);
        Cliente cliente = new Cliente("Ana Lítica", "005");
        LocalDate hoy = LocalDate.now();
        Alquiler a1 = new Alquiler(vehiculo, cliente, hoy, hoy.plusDays(4), PoliticasDePrecios.Oferton);

        String serializado = a1.toString();
        System.out.println("Serializado: " + serializado);

        Alquiler a2 = FactoriaAlquileres.getAlquiler(serializado);
        System.out.println("Deserializado: " + a2);
        System.out.println("Precio del alquiler reconstruido: " + a2.calcularPrecio() + " euros");
    }
}
