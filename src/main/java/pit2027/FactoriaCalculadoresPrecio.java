package pit2027;

/**
 * MÉTODO FACTORÍA para las políticas de precios.
 * Dada una política (o su nombre en texto) devuelve el calculador de precio
 * (la estrategia) que le corresponde. Así el resto del código no tiene que
 * conocer las clases concretas CalculadorPrecioNormal, CalculadorPrecioOferton, etc.
 */
public class FactoriaCalculadoresPrecio {

    /**
     * Crea el calculador de precio de una política.
     * @param politica política de precios
     * @return la estrategia correspondiente
     * @throws IllegalArgumentException si la política es null
     */
    public static ICalculadorPrecio getCalculador(PoliticasDePrecios politica) {
        if (politica == null) {
            throw new IllegalArgumentException("La política de precios no puede ser null");
        }
        switch (politica) {
            case Normal:
                return new CalculadorPrecioNormal();
            case TemporadaAlta:
                return new CalculadorPrecioTemporadaAlta();
            case Oferton:
                return new CalculadorPrecioOferton();
            case ClienteFrecuente:
                return new CalculadorPrecioClienteFrecuente();
            default:
                throw new IllegalArgumentException("Política de precios desconocida: " + politica);
        }
    }

    /**
     * Crea el calculador de precio a partir del NOMBRE de la política
     * (por ejemplo "Oferton", tal y como aparece en el JSON).
     * @param nombre nombre de la política
     * @return la estrategia correspondiente
     * @throws IllegalArgumentException si el nombre no corresponde a ninguna política
     */
    public static ICalculadorPrecio getCalculador(String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre de la política no puede ser null");
        }
        try {
            // valueOf busca el valor del enum con ese nombre exacto
            return getCalculador(PoliticasDePrecios.valueOf(nombre));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Política de precios desconocida: " + nombre);
        }
    }

    /**
     * Ejemplo de uso: se crean calculadores por nombre y se usan sobre un mismo alquiler.
     */
    public static void main(String[] args) {
        Vehiculo vehiculo = new Turismo("1234ABC", Categoria.SUPERIOR, 2024, 5, 550.0);
        java.time.LocalDate inicio = java.time.LocalDate.now();
        java.time.LocalDate fin = inicio.plusDays(9); // 10 días

        String[] nombres = { "Normal", "TemporadaAlta", "Oferton", "ClienteFrecuente", "Inventada" };
        for (String nombre : nombres) {
            try {
                ICalculadorPrecio calculador = FactoriaCalculadoresPrecio.getCalculador(nombre);
                int precio = calculador.calcularPrecioAlquiler(inicio, fin, vehiculo);
                System.out.println(nombre + " -> " + calculador.getClass().getSimpleName() + " -> " + precio + " euros");
            } catch (IllegalArgumentException e) {
                System.out.println(nombre + " -> error: " + e.getMessage());
            }
        }
    }
}
