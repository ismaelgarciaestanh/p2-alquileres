package pit2027;

import org.json.JSONObject;

public class VehiculoIndustrial extends Vehiculo {

    private int capacidadCarga;
    private int numeroEjes;

    public VehiculoIndustrial(String matricula, Categoria categoria, int annio,
        int capacidadCarga, int numeroEjes
    ) {
        super(matricula, categoria, annio);
        this.capacidadCarga = capacidadCarga;
        this.numeroEjes = numeroEjes;
    }

    /**
     * Constructor para DESERIALIZAR: crea el objeto a partir de su JSON.
     * @param jsonString texto JSON del vehículo
     */
    public VehiculoIndustrial(String jsonString) {
        super(jsonString);
        JSONObject json = new JSONObject(jsonString);
        this.capacidadCarga = json.getInt("capacidadCarga");
        this.numeroEjes = json.getInt("numeroEjes");
    }

    public int getCapacidadCarga() {
        return capacidadCarga;
    }

    public int getNumeroEjes() {
        return numeroEjes;
    }

    @Override
    public String getTipo() {
        return "VehiculoIndustrial";
    }

    @Override
    public int getPrecioBase() {
        int precioBase = 0;
        switch (getCategoria()) {
            case NORMAL:
                precioBase = 80;
                break;
            case ESPECIAL:
                precioBase = 120;
                break;
            case SUPERIOR:
                precioBase = 160;
                break;
        }
        return precioBase;
    }
}
