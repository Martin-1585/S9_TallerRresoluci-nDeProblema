package udla.molalla.S9ResolucionProblemas.clases;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Inventario extends Producto {
    /**Declaración de variable global*/
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final int MAXYEAR = 2030;
    private static final int MAXTIME = 3;
    /**Declaración de variables*/
    private int maxSpace = 100;
    private double maxBudget = 20000;
    private String name;
    private LocalDate restockDate;
    private double maxCost = 0;
    /**Declaración de objetos y listas*/
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
        boolean allow = true; //
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

    private LocalDate allowDate(String message, String warning){
        String date;
        LocalDate restockDate;
        int year, yearComputer;

        System.out.print(message);
        date = entry.nextLine().trim();
        try{
            restockDate = LocalDate.parse(date,FORMAT);
            year = restockDate.getYear();
            yearComputer = LocalDate.now().getYear();
            if (year < yearComputer || year > MAXYEAR){
                System.out.println("\tAÑO NO VALIDO, INGRESE NUEVAMENTE");
                return allowDate("Ingrese nuevamente la fecha (dd/MM/aaaa): ", "Información no valido, ingrese nuevamente");
            }
            return restockDate;
        } catch (DateTimeParseException e){
            System.out.println(warning);
            return allowDate("Ingrese nuevamente la fecha (dd/MM/aaaa): ", "Información no valido, ingrese nuevamente");
        }
    }

    public LocalDate allowSaleDate(String warning){
        LocalDate saleDate;

        try {
            saleDate = allowDate("Ingrese la fecha en la que se realizo la venta (dd/MM/yyyy): ", "Información no válida, ingrese nuevamente");
            if (saleDate.isEqual(restockDate)){
                System.out.println("\tLAS FECHAS DE VENTA Y REABASTECIMIENTO NO PUEDEN SER IGUALES");
                return allowSaleDate("Información noo valida, ingrese nuevamente");
            } else if (saleDate.isBefore(restockDate)) {
                System.out.println("\tLA FECHA DE VENTA NO PUEDE SER MENOR A LA FECHA DE REABASTECIMIENTO");
                return allowSaleDate("Información noo valida, ingrese nuevamente");
            } else {
                return saleDate;
            }
        }catch (DateTimeParseException e){
            System.out.println(warning);
            return allowSaleDate("Información noo valida, ingrese nuevamente");
        }
    }

    public LocalDate deliveryTime(LocalDate saleDate){
        return saleDate.plusDays(MAXTIME);
    }

    public LocalDate restock(String warning){
        LocalDate todayDate;

        try {
            todayDate = allowDate("Ingrese la fecha de reabastecimiento (dd/MM/aaaa): ", "Información no valida, ingrese nuevamente");
            if (todayDate.isEqual(restockDate)){
                System.out.println("\tLA FECHA INGRESADA COINCIDE CON LA FECHA DE REABASTECIMIENTO");
                return todayDate;
            } else {
                System.out.println("\tLA FECHA INGRESADA NO COINCIDE CON LA FECHA DE REABASTECIMIENTO, INGRESE NUEVAMENTE");
                return restock("\tEL FORMATO DE LA FECHA ES INCORRECTO");
            }
        }catch (DateTimeParseException e){
            System.out.println(warning);
            return restock("\tEL FORMATO DE LA FECHA ES INCORRECTO");
        }
    }


    public Boolean valorPermitido(int value, int max, String warning){
        boolean allow = false;
        if (value <= 0 || value > max){
            System.out.println(warning);
            allow = false;
        } else {
            allow = true;
        }
        return allow;
    }

    public Integer ingresoEnteros(int max, String message){
        boolean allow = false;
        int value = 0;
        do {
            try{
                System.out.print(message);
                value = Integer.parseInt(entry.nextLine());
                allow = valorPermitido(value, max, "Valor no permitido/Sobrepasa el total");
            } catch (NumberFormatException e){
                System.out.println("\tNO SE PUEDE INGRESAR CARACTERES EN ESTE ESPACIO");
            }
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
        double value = 0;
        do {
            try{
                System.out.print(message);
                value = Double.parseDouble(entry.nextLine());
                allow = costoPermitido(value, max, "Valor no permitido/Sobrepasa el total");
            } catch(NumberFormatException e){
                System.out.println("\tNO SE PUEDE INGRESAR CARACTERES EN ESTE ESPACIO");
            }
        } while(!allow);
        return value;
    }

    public void ingresarDatos(){
        if (maxSpace == 0){
            System.out.println("\t-> !HA USADO TODO EL ESPACIO DEL INVENTARIO¡ <-");
            System.out.println("\t-> !DEBE VENDER PRODUCTOS PARA LIBERAR ESPACIO¡ <-\n");
            return;
        }
        System.out.println("\tESPACIO DE ALMACENAMIENTO: " + maxSpace + " UNIDADES");
        System.out.println("\tPRESUPUESTO DEL INVENTARIO: $" + maxBudget + "\n");
        System.out.println("=======INGRESO DE PRODUCTO=======");
        name = validName("Ingrese el nombre del producto");
        int amount = ingresoEnteros(maxSpace, "Ingrese la cantidad de productos: ");
        restockDate = allowDate("Ingrese la fecha de reabastecimiento (dd/MM/aaaa): ", "Información no valido, ingrese nuevamente");
        double cost = ingresoCosto(maxBudget, "Ingrese el costo unitario del producto: $ ");
        double pvp = (cost * 1.10); //Es por el 10% por el costo unitario
        double totalCost = (cost * amount);

        if (totalCost > maxBudget){
            System.out.println("\t-> !HA USADO TODO EL PRESUPUESTO DEL INVENTARIO¡ <-");
            System.out.println("\t-> !NO SE HA AGREGADO EL ULTIMO PRODUCTO QUE INGRESO¡ <-\n");
            return;
        }

        maxSpace-= amount;;
        maxBudget-= totalCost;
        maxCost+= totalCost;

        Producto nuevoproducto = new Producto(name, amount,restockDate, pvp, cost, totalCost);
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
        System.out.println("\tEL COSTO TOTAL DEL INVENTARIO ES DE: $" + maxCost);
        System.out.println("\tEL PRESUPUESTO DEL INVENTARIO: $" + maxBudget);
    }

    public void eliminarProducto(){
        boolean found = false;
        int idErase;
        if (productos.isEmpty()){
            System.out.println("\tNO HAY PRODUCTOS A ELIMINAR");
            return;
        }

        System.out.println("=========================================");
        for (Producto p: productos){
            p.mostrarIds();
            System.out.println("=========================================");
        }

        idErase = ingresoEnteros(500, "Ingrese el ID del producto a eliminar: ");

        for (Producto p: productos){
            if (p.getId() == idErase){
                found = true;
                maxSpace+=p.getAmount();
                maxBudget+=p.getCost();
                maxCost-=p.getCost();
                productos.remove(p);
                System.out.println("\tEL producto " + name + "con ID: " + idErase + " ha sido eliminado de manera correcta");
                System.out.println("Ahora se tiene espacio para: " + maxSpace + " unidades");
                System.out.println("Ahora se tiene un presupuesto de $: " + maxBudget);
                System.out.println("Ahora se tiene costo total de $: " + maxCost);
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
        LocalDate nuevoDate;
        double nuevoPvp;
        double nuevoCost;
        double nuevoTotalCost;

        if (productos.isEmpty()){
            System.out.println("\t NO HAY PRODUCTOS A EDITAR");
            return;
        }

        System.out.println("=========================================");
        for (Producto p: productos){
            p.mostrarIds();
            System.out.println("=========================================");
        }

        idEdit = ingresoEnteros(500, "Ingrese el ID del producto que desea cambiar: ");

        for (Producto p: productos){
            if (p.getId() == idEdit){
                found = true;
                int oldAmount = p.getAmount();
                double oldCost = p.getCost();
                int restoreSpace = maxSpace + oldAmount;
                double restoreBudget = maxBudget + oldCost;

                nuevoName = validName("Ingrese el nuevo nombre del producto: ");
                nuevoAmount = ingresoEnteros(restoreSpace, "Ingrese la nueva cantidad: ");
                nuevoDate = allowDate("Ingrese la nueva fecha de reabastecimiento (dd/MM/aaaa): ", "Información no valido, ingrese nuevamente");
                nuevoCost = ingresoCosto(restoreBudget, "Ingrese el nuevo costo unitario del producto: $ ");
                nuevoPvp = (nuevoCost * 1.10);
                nuevoTotalCost = (nuevoCost * nuevoAmount);

                if (nuevoTotalCost > restoreBudget){
                    System.out.println("\t-> !EL NUEVO COSTO EXCEDE EL PRESUPUESTO: $" + restoreBudget + "¡ <-");
                    System.out.println("\t-> !NO SE HA EDITADO EL ULTIMO PRODUCTO QUE INGRESO¡ <-\n");
                }

                maxBudget = (restoreBudget - nuevoTotalCost);
                maxCost = maxCost + (oldCost * oldAmount) - nuevoTotalCost;
                maxSpace = maxSpace + oldAmount - nuevoAmount;

                p.setName(nuevoName);
                p.setAmount(nuevoAmount);
                p.setRestockDate(nuevoDate);
                p.setPvp(nuevoPvp);
                p.setCost(nuevoCost);
                p.setTotalCost(nuevoTotalCost);

                System.out.println("\t=== SE HA EDITADO DE MANERA ADECUADA EL PRODUCTO ===\n");
                break;
            }
        }

        if (!found){
            System.out.println("\t=== EL ID ingresado no existe en el inventario para editar ===\n");
        }
    }

    public void venderProductos(){
        LocalDate deliveryDate;
        LocalDate saleDate;
        boolean found = false;
        int saleId;
        int saleAmount;
        double sale;
        double nuevoTotalCost;
        int restockAmount;

        if (productos.isEmpty()){
            System.out.println("\tNO HAY PRODUCTOS A VENDER");
            return;
        }

        System.out.println("=========================================");
        for (Producto p: productos){
            p.mostrarIds();
            System.out.println("=========================================");
        }

        saleId = ingresoEnteros(500, "Ingrese el ID del producto que desea vender: ");

        for (Producto p: productos){
            if (p.getId() == saleId){
                found = true;
                if (p.getAmount() == 0){
                    System.out.println("\tPRODUCTO AGOTADO, DEBE REABASTECER");
                    entry.nextLine();
                    restock("\tEL FORMATO DE LA FECHA ES INCORRECTO");
                    restockAmount = ingresoEnteros(maxSpace, "Ingrese la cantidad para reabastecer el producto: ");
                    p.setAmount(p.getAmount() + restockAmount);
                    maxSpace-=restockAmount;
                    p.setTotalCost(restockAmount * p.getCost());
                    maxBudget-=p.getTotalCost();
                    System.out.println("====== SE HA REABASTECIDO CON EXITO ======");
                    System.out.println("AHORA TIENE: " + p.getAmount() + " UNIDADES");
                    System.out.println("ESPACIO: " + maxSpace);
                    System.out.println("PRESUPUESTO: $" + maxBudget);
                    System.out.println("=========================================");
                    System.out.println("\tSE PROCEDE A LA VENTA");
                }

                saleDate = allowSaleDate("Información no valida, ingrese nuevamente");
                System.out.println("=====================================");
                System.out.println("El producto con nombre: " + p.getName()
                        + "\nID: " + saleId
                        + "\nCantidad disponible: " + p.getAmount());
                System.out.println("=====================================");
                saleAmount = ingresoEnteros(p.getAmount(), "Ingrese la cantidad a vender: ");
                p.setAmount(p.getAmount() - saleAmount);
                p.setTotalCost(p.getAmount() * p.getCost());
                maxCost-=(p.getCost() * saleAmount);
                System.out.println("=====================================");
                System.out.println("\tUNIDADES: " + p.getAmount());
                maxSpace+=saleAmount;
                System.out.println("\tESPACIO: " + maxSpace);
                System.out.println("=====================================");
                deliveryDate = deliveryTime(saleDate);
                System.out.println("El producto llegara en un limite de " + MAXTIME + " dias, es decir el " + deliveryDate);
                sale = (p.getPvp() * saleAmount);
                System.out.print("\n\tLA VENTA SE REALIZO POR UN PRECIO DE: $" + sale);
                maxBudget+=sale;
                System.out.println("\n\tPRESUPUESTO DEL INVENTARIO TRAS LA VENTA: $" + maxBudget);
                break;
            }
        }

        if (!found){
            System.out.println("\t=== EL ID ingresado no existe en el inventario para vender ===\n");
        }
    }
}
