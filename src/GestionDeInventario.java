import java.util.Scanner;


public class GestionDeInventario {
    public static void main(String[] args) {
        /**Declaración de objetos*/
        Inventario inventario = new Inventario();
        Scanner entry = new Scanner(System.in);
        /**Declaración de variables*/
        int opcion;
        /**Desarrollo de la aplicación*/
        do {
            System.out.println("\n\t=== SIMULADOR DE GESTION DE INVENTARIO ===\n");
            inventario.mostrarMenu();
            opcion = inventario.ingresoEnteros(7, "Ingrese una opción: ");
            switch (opcion){
                case 1:
                    inventario.ingresarDatos();
                    break;
                case 2:
                    inventario.mostrarInfo();
                    break;
                case 3:
                    inventario.eliminarProducto();
                    break;
                case 4:
                    inventario.editarProducto();
                    break;
                case 5:
                    inventario.venderProductos();
                    break;
                default:
                    break;
            }
        } while(opcion != 6);
        System.out.println("\n\tSALIENDO DEL SISTEMA, HASTA LA PROXIMA");

        entry.close();
    }
}