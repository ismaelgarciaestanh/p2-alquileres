package pit2027;

import java.time.LocalDate;

public class GestorAlquileres {
    
    private int contadorAlquileres = 0;
    private Alquiler[] alquileres = new Alquiler[1000];

    // Para simplificar, vamos a definir un array de clientes y 
    // otro de vehículos dentro del gestor.
    // En una aplicación real estarían en una base de datos.
    private Cliente[] clientes = new Cliente[10];
    private Vehiculo[] vehiculos = new Vehiculo[100];


    public GestorAlquileres() {
        inicializarClientes();
        inicializarVehiculos();
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // INICIALIZACIÓN DE CLIENTES Y VEHÍCULOS DE EJEMPLO

    // Definimos métodos privados para inicializar clientes y vehículos de ejemplo
    private void inicializarClientes() {
        clientes[0] = new Cliente("Clara Videncia", "001");
        clientes[1] = new Cliente("Aitor Menta", "002");
        clientes[2] = new Cliente("Lola Mento", "003");
        clientes[3] = new Cliente("Armando Guerra", "004");
        clientes[4] = new Cliente("Ana Lítica", "005");
        clientes[5] = new Cliente("Carmelo Cotón", "006");
    }

    private void inicializarVehiculos() {
        vehiculos[0] = new Turismo("TNA2020", Categoria.NORMAL, 2020, 5, 450.0);
        vehiculos[1] = new Motocicleta("MNA2021", Categoria.NORMAL, 2021, 200);
        vehiculos[2] = new VehiculoIndustrial("CNA2019", Categoria.NORMAL, 2019, 1400, 2);
        vehiculos[3] = new Turismo("TSA2024", Categoria.SUPERIOR, 2024, 5, 550.0);
        vehiculos[4] = new Motocicleta("MSA2021", Categoria.SUPERIOR, 2021, 500);
        vehiculos[5] = new VehiculoIndustrial("CSA2025", Categoria.SUPERIOR, 2025, 2400, 3);
        vehiculos[6] = new Turismo("TEA2026", Categoria.ESPECIAL, 2026, 7, 650.0);
        vehiculos[7] = new Motocicleta("MEA2026", Categoria.ESPECIAL, 2026, 1000);
        vehiculos[8] = new VehiculoIndustrial("CEA2025", Categoria.ESPECIAL, 2025, 4400, 5);

    }

    /////////////////////////////////////////////////////////////////////////////////////
    // MÉTODOS PARA GESTIONAR CLIENTES

    /**
     * Devuelve un array con todos los clientes del gestor (sin huecos vacíos).
     * @return array de clientes
     */
    public Cliente[] obtenerClientes() {
        int contador = 0;
        for (Cliente cliente : clientes) {
            if (cliente != null) {
                contador++;
            }
        }

        Cliente[] resultado = new Cliente[contador];
        int posicion = 0;
        for (Cliente cliente : clientes) {
            if (cliente != null) {
                resultado[posicion++] = cliente;
            }
        }
        return resultado;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // MÉTODOS PARA GESTIONAR VEHÍCULOS

    /**
     * Devuelve un array con todos los vehículos del gestor (sin huecos vacíos).
     * @return array de vehículos
     */
    public Vehiculo[] obtenerVehiculos() {
        int contador = 0;
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo != null) {
                contador++;
            }
        }

        Vehiculo[] resultado = new Vehiculo[contador];
        int posicion = 0;
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo != null) {
                resultado[posicion++] = vehiculo;
            }
        }
        return resultado;
    }

     public Vehiculo[] obtenerVehiculos(String tipo) {
        
        Vehiculo[] vehiculosFiltrados = new Vehiculo[vehiculos.length];
        int contador = 0;

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo != null) {
                if (vehiculo.getTipo().equals(tipo)) {
                    vehiculosFiltrados[contador++] = vehiculo;
                }
            }
        }

        // Ajustar el tamaño del array para que solo contenga los vehículos encontrados
        Vehiculo[] resultado = new Vehiculo[contador];
        System.arraycopy(vehiculosFiltrados, 0, resultado, 0, contador);
        return resultado;
    }   

    /**
     * Devuelve un array con los vehículos del tipo y categoría especificados.
     * @param tipo
     * @param categoria
     * @return lista de vehículos del tipo y categoría especificados
     */
    public Vehiculo[] obtenerVehiculos(String tipo, Categoria categoria) {

        Vehiculo[] vehiculosFiltrados = new Vehiculo[vehiculos.length];
        int contador = 0;

        for (Vehiculo vehiculo : vehiculos) {
            // Se guarda el vehículo solo si coinciden el tipo Y la categoría
            if (vehiculo != null
                    && vehiculo.getTipo().equals(tipo)
                    && vehiculo.getCategoria() == categoria) {
                vehiculosFiltrados[contador++] = vehiculo;
            }
        }

        // Ajustamos el tamaño del array igual que en obtenerVehiculos(tipo)
        Vehiculo[] resultado = new Vehiculo[contador];
        System.arraycopy(vehiculosFiltrados, 0, resultado, 0, contador);
        return resultado;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // MÉTODOS PARA GESTIONAR ALQUILERES

    /**
     * Agrega un nuevo alquiler al array de alquileres.
     * @param alquiler
     */
    public void agregarAlquiler(Alquiler alquiler) {
        if (contadorAlquileres < alquileres.length) {
            alquileres[contadorAlquileres] = alquiler;
            contadorAlquileres++;
        } else {
            System.out.println("No se pueden agregar más alquileres. Capacidad máxima alcanzada.");
        }
    }

    /**
     * Devuelve un array con todos los alquileres registrados en el gestor.
     * @return
     */
    public Alquiler[] obtenerAlquileres() {
        return alquileres; 
    }

    /**
     * Devuelve los alquileres (histórico) de un cliente.
     * Dos clientes son el mismo si tienen el mismo DNI.
     * @param cliente cliente del que se quieren los alquileres
     * @return array con sus alquileres (vacío si no tiene ninguno o si el cliente es null)
     */
    public Alquiler[] obtenerAlquileres(Cliente cliente) {
        Alquiler[] encontrados = new Alquiler[contadorAlquileres];
        int contador = 0;

        if (cliente != null) {
            // Solo recorremos hasta contadorAlquileres: el resto del array está vacío (null)
            for (int i = 0; i < contadorAlquileres; i++) {
                if (alquileres[i].getCliente().getDni().equals(cliente.getDni())) {
                    encontrados[contador++] = alquileres[i];
                }
            }
        }

        Alquiler[] resultado = new Alquiler[contador];
        System.arraycopy(encontrados, 0, resultado, 0, contador);
        return resultado;
    }

    /**
     * Devuelve los alquileres de un cliente que todavía no ha entregado.
     * Un alquiler está pendiente si su vehículo no está disponible y no hay
     * después otro alquiler más reciente del mismo vehículo.
     * @param cliente cliente a consultar
     * @return array con los alquileres pendientes de entrega
     */
    public Alquiler[] obtenerAlquileresPendientes(Cliente cliente) {
        Alquiler[] delCliente = obtenerAlquileres(cliente);
        Alquiler[] pendientes = new Alquiler[delCliente.length];
        int contador = 0;

        for (Alquiler alquiler : delCliente) {
            if (!alquiler.getVehiculo().isDisponible() && esUltimoAlquilerDelVehiculo(alquiler)) {
                pendientes[contador++] = alquiler;
            }
        }

        Alquiler[] resultado = new Alquiler[contador];
        System.arraycopy(pendientes, 0, resultado, 0, contador);
        return resultado;
    }

    /**
     * Comprueba que no hay ningún alquiler posterior del mismo vehículo
     * (la matrícula es única para cada vehículo).
     */
    private boolean esUltimoAlquilerDelVehiculo(Alquiler alquiler) {
        boolean encontrado = false;
        boolean esUltimo = true;

        for (int i = 0; i < contadorAlquileres; i++) {
            if (alquileres[i] == alquiler) {
                encontrado = true;
            } else if (encontrado
                    && alquileres[i].getVehiculo().getMatricula().equals(alquiler.getVehiculo().getMatricula())) {
                esUltimo = false;
            }
        }
        return esUltimo;
    }


    /**
     * Informa si un cliente es cliente frecuente en función del número de alquileres previos.
     * @param cliente cliente a evaluar
     * @param umbral número mínimo de alquileres previos que se consideran para determinar si es cliente frecuente
     * @return true si el cliente tiene al menos "umbral" alquileres registrados
     */
    boolean esClienteFrecuente(Cliente cliente, int umbral) {
        // Sin "private" para que el test del mismo paquete pueda probarlo
        return obtenerAlquileres(cliente).length >= umbral;
    }


    /////////////////////////////////////////////////////////////////////////////////////
    // INTERFAZ DE USUARIO (CONSOLA) Y LÓGICA DE NEGOCIO

    // La interfaz de usuario y la lógica de negocio suelen implementarse por separado.
    // Aquí se integran ambas por simplicidad, pero en una aplicación real hay que separarlas.


    public void menuPrincipal(){
        
        boolean salir = false;
        
        while(!salir){

            // Se da oportunidad de salir inmediatamente de la app.
            System.out.println("Gestíon de Alquileres");
            System.out.println("¿Desea salir de la app (S/N)?");

            char s = KeyBoard.readChar();
            if (s == 's' || s == 'S'){
                System.out.println("Bye ...");
                return;
            }

            // Primero se identifica al cliente:
            Cliente cliente = identificarCliente();

            // Ahora el cliente puede elegir una opción o salir de la aplicación.
            if (cliente != null) {
                System.out.println("Sr " + cliente.getNombre() + " Introduzca una opción");
                System.out.println("(1) seleccionar coche para alquilar");
                System.out.println("(2) entregar un coche alquilado");
                System.out.println("(3) listar su histórico de alquileres");

                int opcion = KeyBoard.readInt();
                System.out.println("opción escogida: " + opcion);

                switch (opcion) {
                    case 1:
                        Vehiculo v = seleccionarVehiculo(cliente);
                        if (v != null && v.isDisponible()){
                            // para simplificar la IU se supone que el alquiler comienza "ahora"
                            // y el usuario especifica duración en días.
                            System.out.println("¿cuántos días desea alquilar el vehículo?");
                            int dias = KeyBoard.readInt();
                            if (dias < 1) {
                                System.out.println("El número de días debe ser al menos 1");
                                break;
                            }
                            LocalDate inicio = LocalDate.now();
                            // El día de inicio también cuenta: 3 días = hoy + 2 días más
                            LocalDate fin = inicio.plusDays(dias - 1);

                            // Se determina la política de precios aplicable:
                            // cliente frecuente si tiene 3 o más alquileres previos, y normal en caso contrario.
                            // Se comprueba ANTES de registrar el alquiler para contar solo los anteriores.
                            boolean usuarioFrecuente = this.esClienteFrecuente(cliente, 3);
                            PoliticasDePrecios politica;
                            if (usuarioFrecuente){
                                politica = PoliticasDePrecios.ClienteFrecuente;
                                System.out.println("Alquiler realizado. Tarifa Usuario frecuente.");
                            }
                            else{
                                politica = PoliticasDePrecios.Normal;
                                System.out.println("Alquiler realizado. Tarifa Normal.");
                            }

                            // Creamos y registramos el alquiler y marcamos el vehículo como ocupado
                            Alquiler q = new Alquiler(v, cliente, inicio, fin, politica);
                            this.agregarAlquiler(q);
                            v.setDisponible(false);

                            // El propio alquiler calcula el precio con su estrategia
                            System.out.println("Precio total: " + q.calcularPrecio() + " euros");
                        }
                        else{
                            System.out.println("Debe seleccionar un vehículo disponible");
                        }
                        break;
                    case 2: 
                        entregarVehiculo(cliente);
                        break;              
                    case 3:
                        Alquiler[] historico = obtenerAlquileres(cliente);
                        if (historico.length == 0) {
                            System.out.println("No tiene alquileres registrados");
                        }
                        for (Alquiler q : historico){
                            System.out.println(q);
                        }
                        break;
                    default:
                        break;
                }
            }
            else{
                System.out.println("Debe identificarse correctamente o salir de la app");
            }
        }
    }

    /**
     * Opción "entregar un coche alquilado": el cliente elige uno de sus alquileres
     * pendientes y el vehículo vuelve a estar disponible.
     */
    private void entregarVehiculo(Cliente cliente) {
        Alquiler[] pendientes = obtenerAlquileresPendientes(cliente);

        if (pendientes.length == 0) {
            System.out.println("No tiene ningún vehículo pendiente de entregar");
            return;
        }

        System.out.println("Vehículos pendientes de entrega:");
        for (int i = 0; i < pendientes.length; i++) {
            Vehiculo vehiculo = pendientes[i].getVehiculo();
            System.out.println(i + " ---> " + vehiculo.getTipo() + " " + vehiculo.getMatricula());
        }

        System.out.println("Introduzca el número del vehículo que entrega (-1 para cancelar):");
        int seleccion = KeyBoard.readInt();
        if (seleccion < 0 || seleccion >= pendientes.length) {
            System.out.println("No se ha entregado ningún vehículo");
            return;
        }

        pendientes[seleccion].getVehiculo().setDisponible(true);
        System.out.println("Vehículo entregado. Gracias, Sr/Sra " + cliente.getNombre());
    }

    public Cliente identificarCliente(){

        boolean seleccionado = false;
        Cliente cliente = null;

        while (!seleccionado) {
        
            System.out.println("Bienvenido al Gestor de Alquileres");
            for (int i = 0; i < clientes.length; i++) {
                if (clientes[i] != null) {
                    System.out.println(i + " ---> " + clientes[i].getNombre() + " - " + clientes[i].getDni());
                }
            }
            System.out.println("Identífiquese como cliente con su número de cliente (-1 para salir):");
            int numeroCliente = KeyBoard.readInt();
            if (numeroCliente == -1) {
                return null;
            }
            else if (numeroCliente < 0 || numeroCliente >= clientes.length || clientes[numeroCliente] == null) {
                System.out.println("identificarCliente:Número de cliente inválido.");
                return null;
            }
            else{
                seleccionado = true;
                cliente = clientes[numeroCliente];
            }
        }
        return cliente;
    }

    public Vehiculo seleccionarVehiculo(Cliente cliente){
        boolean seleccionado = false;
        Vehiculo vehiculo = null;

        while (!seleccionado) {
        
            System.out.println("Estimado Sr./Sra. " + cliente.getNombre());
            System.out.println("Introduzca el número correspondiente al tipo de vehículo que desea alquilar:");
            System.out.println("1. Turismo, 2. Motocicleta, 3. Vehículo Industrial (0 para cancelar): ");

            int tipoVehiculo = KeyBoard.readInt();
            String tipo = "";
            Vehiculo [] elegibles;

            switch (tipoVehiculo) {
                case 0:
                    return null; // el cliente cancela la selección
                case 1:
                    System.out.println("Ha seleccionado Turismo.");
                    tipo = "Turismo";
                    break;
                case 2:
                    System.out.println("Ha seleccionado Motocicleta.");
                    tipo = "Motocicleta";
                    break;
                case 3:
                    System.out.println("Ha seleccionado Vehículo Industrial.");
                    tipo = "VehiculoIndustrial"; // igual que devuelve getTipo() (sin tilde)
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione un tipo de vehículo válido.");
                    continue; // Volver a pedir la selección
            }

            System.out.println("Estimado Sr./Sra. " + cliente.getNombre() + "\n seleccione categoría.");
            System.out.println("Introduzca el número correspondiente a la categoría que desea alquilar:");
            System.out.println("1. Normal, 2. Superior, 3. Especial, 4.Cualquiera: ");

            int categoriaVehiculo = KeyBoard.readInt();

            switch (categoriaVehiculo) {
                case 1:
                    System.out.println("Ha seleccionado Categoría Normal.");
                    elegibles = obtenerVehiculos(tipo, Categoria.NORMAL);
                    break;
                case 2:
                    System.out.println("Ha seleccionado Categoría Superior.");
                    elegibles = obtenerVehiculos(tipo, Categoria.SUPERIOR);
                    break;
                case 3:
                    System.out.println("Ha seleccionado Categoría Especial.");
                    elegibles = obtenerVehiculos(tipo, Categoria.ESPECIAL);
                    break;
                case 4:
                    System.out.println("Ha seleccionado cualquier categoría.");
                    elegibles = obtenerVehiculos(tipo);
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione una categoría válida.");
                    continue; // Volver a pedir la selección
            }

            if (elegibles.length == 0) {
                System.out.println("No hay vehículos de ese tipo y categoría.");
                continue; // Volver a pedir la selección
            }

            System.out.println("Coches elegibles");

            for(int i = 0; i < elegibles.length; i++){
                System.out.println(i + " ---> " + elegibles[i]);
            }

            System.out.println("Introduzca número coche (sólo si está disponible)");
            int seleccion = KeyBoard.readInt();
            if (seleccion < 0 || seleccion >= elegibles.length) {
                System.out.println("Número de coche inválido.");
                continue; // Volver a pedir la selección
            }
            vehiculo = elegibles[seleccion];
            seleccionado = true;
        }
        return vehiculo;
    }

    public void listarAlquileres (){
        System.out.println("Listado de alquileres");
        for (Alquiler alq: alquileres){
            if (alq != null) {
                System.out.println(alq);
            }
        }
        System.out.println();
    }

    public void listarVehiculos(){
        System.out.println("Listado de vehículos");
        for (Vehiculo veh: vehiculos){
            if (veh != null) {
                System.out.println(veh);
            }
        }
        System.out.println();
    }


    public static void main(String [] args){
        GestorAlquileres app = new GestorAlquileres();
        app.menuPrincipal();
    }
}
