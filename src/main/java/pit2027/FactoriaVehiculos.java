package pit2027;

import org.json.JSONObject;

/**
 * MÉTODO FACTORÍA para vehículos.
 * Recibe el JSON de un vehículo, mira su campo "tipo" y decide qué subclase
 * (Turismo, Motocicleta o VehiculoIndustrial) hay que crear.
 * Así quien la usa no necesita saber qué constructor llamar.
 */
public class FactoriaVehiculos {

    /**
     * Crea un vehículo a partir de su representación JSON.
     * @param jsonString JSON del vehículo (debe incluir el campo "tipo")
     * @return el vehículo (de la subclase que corresponda)
     * @throws IllegalArgumentException si el tipo no es conocido
     */
    public static Vehiculo getVehiculo(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        String clase = json.getString("tipo");
        switch (clase) {
            case "Turismo":
                return new Turismo(jsonString);
            case "Motocicleta":
                return new Motocicleta(jsonString);
            case "VehiculoIndustrial":
                return new VehiculoIndustrial(jsonString);
            default:
                throw new IllegalArgumentException("Tipo de vehículo desconocido: " + clase);
        }
    }

    /**
     * Ejemplo de uso: serializar un vehículo y reconstruirlo.
     */
    public static void main(String[] args) {
        Vehiculo v1 = new Turismo("1234ABC", Categoria.NORMAL, 2020, 5, 450.0);
        String serializado = v1.toString();
        System.out.println("Serializado: " + serializado);

        Vehiculo v2 = FactoriaVehiculos.getVehiculo(serializado);
        System.out.println("Deserializado (" + v2.getTipo() + "): " + v2);
    }
}
