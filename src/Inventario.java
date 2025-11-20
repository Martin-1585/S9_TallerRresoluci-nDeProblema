import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Inventario extends Producto{
    /**Declaración de variables*/
    private int maxSpace = 30;
    private double maxBudget = 2000;
    private int maxTime = 3;
    private String name;
    private int amount;
    private int year;
    private int month;
    private int day;
    private double pvp;
    private double cost;
    private double maxCost = 0;
    /**Declaración de objetos*/
    private List<Producto> productos= new ArrayList<>();
    Scanner entry = new Scanner(System.in);
    /**Métodos propios del desarrollador*/
    public void mostrarMenu(){
        System.out.println("\t-------MENU DE ACCIONES INVENTARIO-------");
        System.out.println("1. Ingresar productos");
        System.out.println("2. Desplegar inventario");
        System.out.println("3. Eliminar producto");
        System.out.println("4. Editar producto");
        System.out.println("5. Vender producto");
        System.out.println("6. Salir del programa");
    }

    public Boolean allowString(String name, String warning){
        boolean allow = true;
        if (name == null || name.isEmpty()){
            allow = false;

        }
        for(char check : name.toCharArray()){
            if (!Character.isAlphabetic(check)){
                allow = false;
            }
        }
        if (!allow){
            System.out.println(warning);
        }
        return allow;
    }

    public String validName(String message){
        boolean allow = false;
        String name;
        do {
            System.out.println(message);
            name = entry.nextLine();
            allow = allowString(name, "No se puede ingresar cadenas vacías o no caracteres");
        } while(!allow);
        return name;
    }

    public Boolean allowYear(int restockYear, String warning){
        LocalDate todayDate = LocalDate.now();
        int year = todayDate.getYear();
        boolean allow;
        if (restockYear != year){
            System.out.println(warning);
            allow = false;
        } else {
            allow = true;
        }
        return allow;
    }

    public Integer validYear(String message){
        boolean allow = false;
        int value;
        do {
            System.out.print(message);
            value = entry.nextInt();
            allow = allowYear(value, "Año no valido");
        }while(!allow);
        return value;
    }

    public Boolean valorPermitido(int value, int max, String warning){
        boolean allow;
        if (value <= 0 || value > max){
            System.out.println(warning);
            allow = false;
        } else {
            allow = true;
        }
        return allow;
    }

    public Integer valoresSinReduccion(int max, String message){
        boolean allow = false;
        int value;
        do {
            System.out.print(message);
            value = entry.nextInt();
            allow = valorPermitido(value, max, "Valor no permitido");
        } while(!allow);
        return value;
    }

    public Integer ingresoEnteros(int max, String message){
        boolean allow = false;
        int value;
        do {
            System.out.print(message);
            value = entry.nextInt();
            allow = valorPermitido(value, max, "Valor no permitido/Sobrepasa el total");
        } while(!allow);
        return value;
    }

    public Boolean costoPermitido(double value, double max, String warning){
        boolean allow;
        if (value <= 0 || value > max){
            System.out.println(warning);
            allow = false;
        } else {
            allow = true;
        }
        return allow;
    }

    public Double ingresoCosto(double max, String message){
        boolean allow = false;
        double value;
        do {
            System.out.print(message);
            value = entry.nextDouble();
            allow = costoPermitido(value, max, "Valor no permitido/Sobrepasa el total");
        } while(!allow);
        return value;
    }

    public void ingresarDatos(){
        entry.nextLine();
        System.out.println("=======INGRESO DE PRODUCTO=======");
        name = validName("Ingrese el nombre del producto");
        amount = ingresoEnteros(maxSpace, "Ingrese la cantidad de productos: ");
        year = validYear("Ingrese el año para la fecha de reabastecimiento: ");
        month = valoresSinReduccion(12, "Ingrese el mes para la fecha de reabastecimiento: ");
        day = valoresSinReduccion(31, "Ingrese el día de la fecha para el reabastecimiento: ");
        pvp = ingresoCosto(maxBudget, "Ingrese el precio de venta al publico del producto: ");
        cost = (amount*pvp);

        maxSpace-=amount;

        if (maxSpace <= 0){
            maxSpace = 0;
            System.out.println("\t-> !HA USADO TODO EL ESPACIO DEL INVENTARIO¡ <-");
            System.out.println("\t-> !NO SE HA AGREGADO EL ULTIMO PRODUCTO QUE INGRESO¡ <-\n");
            return;
        }

        maxCost+=cost;

        maxBudget-=maxCost;

        if (maxBudget <= 0){
            maxBudget = 0;
            System.out.println("Ha gastado todo el presupuesto");
            return;
        }
        Producto nuevoproducto = new Producto(name,amount,year,month,day,pvp,cost);

        productos.add(nuevoproducto);
        System.out.println("\tSe ha agregado de manera exitosa el producto: " + name + " con ID: " + nuevoproducto.getId());

    }

    public void mostrarInfo(){
        System.out.println("\t***********************************************");
        System.out.println("\t   DESPLIEGUE DEL INVENTARIO DE LA EMPRESA       ");
        System.out.println("\t***********************************************  ");
        if (productos.isEmpty()){
            System.out.println("No hay productos registrados");
            return;
        }

        System.out.println("LOS PRODUCTOS REGISTRADOS SON: ");
        System.out.println("======================================================================");
        for(Producto p: productos){
            p.mostrarInfo();
            System.out.println("======================================================================");
        }
        System.out.println("\tEl costo total de todos los productos es de:  $" + maxCost + " de $" + maxBudget);
    }

    public void eliminarProducto(){
        boolean found = false;
        int idErase;
        if (productos.isEmpty()){
            System.out.println("\tNO HAY PRODUCTOS A ELIMINAR");
            return;
        }

        idErase = valoresSinReduccion(500, "Ingrese el ID del producto a eliminar: ");

        for (int i = 0; i < productos.size(); i++){
            if (productos.get(i).getId() == idErase){
                productos.remove(i);
                found = true;
                System.out.println("\tEL producto " + name + "con ID: " + idErase + " ha sido eliminado de manera correcta");
                maxSpace+=amount;
                System.out.println("Ahora se tiene espacio para: " + maxSpace + " unidades");
                break;
            }
        }

        if (!found){
            System.out.println("\t=== EL ID ingresado no existe en el inventario para eliminar ===\n");
        }
    }

    public void editarProducto(){
        boolean found = false;
        int idEdit;
        String nuevoName;
        int nuevoAmount;
        int nuevoYear;
        int nuevoMonth;
        int nuevoDay;
        double nuevoPvp;
        double nuevoCost;

        if (productos.isEmpty()){
            System.out.println("\t NO HAY PRODUCTOS A EDITAR");
            return;
        }

        idEdit = valoresSinReduccion(500, "Ingrese el ID del producto que desea cambiar: ");
        for (int i = 0; i < productos.size(); i++){
            if (productos.get(i).getId() == idEdit){
                found = true;
                entry.nextLine();
                nuevoName = validName("Ingrese el nuevo nombre del producto: ");
                nuevoAmount = ingresoEnteros(maxSpace, "Ingrese la nueva cantidad: ");
                nuevoYear = validYear("Ingrese el nuevo año para la fecha de reabastecimiento: ");
                nuevoMonth = valoresSinReduccion(12, "Ingrese el nuevo mes para la fecha de reabastecimiento: ");
                nuevoDay = valoresSinReduccion(31, "Ingrese el nuevo día para la fecha de reabastecimiento: ");
                nuevoPvp = ingresoCosto(maxBudget, "Ingrese el nuevo precio de venta al público: $ ");
                nuevoCost = (nuevoAmount * nuevoPvp);

                Producto editProduct = new Producto(nuevoName, nuevoAmount, nuevoYear, nuevoMonth, nuevoDay, nuevoPvp, nuevoCost);
                productos.set(i, editProduct);
                break;
            }
        }

        if (!found){
            System.out.println("\t=== EL ID ingresado no existe en el inventario para editar ===\n");
        }

    }


}
