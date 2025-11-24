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
        boolean allow = true; //Este método se creo para validar que el usuario no pueda ingresar números al nombre del producto
        if (name == null || name.isEmpty()){
            allow = false;
        }
        for(char check : name.toCharArray()){ //Se transforma el String en un arreglo de char para que el análisis de cada dato sea más sencillo
            if (!Character.isAlphabetic(check)){ //Se verifica que todos los caracteres sean alfabéticos
                allow = false; //Si uno no lo es no se permite el paso a otros datos de entrada
            }
        }
        if (!allow){
            System.out.println(warning); //Si el booleana consigue el valor de FALSE entonces entra en el if e imprime una advertencia para el usuario
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
        } while(!allow); //Una instrucción do-while que nos ayuda a validar hasta que el usuario ingrese un nombre valido
        return name;
    }
    //Se usa private por principio de encapsulación, queremos que la fecha que ingrese el usuario no úede ser accedida de manera indebida solo dentro a la aplicación
    private LocalDate allowDate(String message, String warning){ //Haciendo uso del package Time que nos permite manipular fechas
        String date;
        LocalDate restockDate;
        int year, yearComputer;
        System.out.print(message);
        date = entry.nextLine().trim(); //Primero se pide un String en donde el usuario ingresa la fecha en un formato especial, y .trim() es para que si el usuario ingresa una fecha se borre un espacio que puede generarse por un espacio no intencionado
        try{ //Uso de try - cath para que el usuario tenga restricciones sobre el formate de la fecha de manera más optimizada
            restockDate = LocalDate.parse(date,FORMAT); //Se pasa el String a un método parse del tipo LocalDate para que así sea formato y que cumpla el formato de la variable global FORMAT
            year = restockDate.getYear();
            yearComputer = LocalDate.now().getYear(); //Se obtiene la fecha del dispositivo para así realizar una validación dinámica
            if (year < yearComputer || year > MAXYEAR){ //Se veerífica si el año ingresado en el fecha ingresada por el usuario cumple con que no sea menor a 2025 o mayor a un intervalo de 5 años, es decir, 2030
                System.out.println("\tAÑO NO VALIDO, INGRESE NUEVAMENTE");
                return allowDate("Ingrese nuevamente la fecha (dd/MM/aaaa): ", "Información no valido, ingrese nuevamente");
                //Se usa la recursividad para que el código quede más limpio y legible, pero existe el riesgo de a una gran cantidad de intentos se detenga el programa por StackOverflow (Que la memoria quedo sin espacio)
            }
            return restockDate;
        } catch (DateTimeParseException e){
            System.out.println(warning);
            return allowDate("Ingrese nuevamente la fecha (dd/MM/aaaa): ", "Información no valido, ingrese nuevamente");
            //Si el formato es incorrecto se llama a la función otra vez, aplicando recursividad al código para una optimización visual
        }
    }

    public LocalDate allowSaleDate(String warning){
        LocalDate saleDate;
        try {
            saleDate = allowDate("Ingrese la fecha en la que se realizo la venta (dd/MM/yyyy): ", "Información no válida, ingrese nuevamente");
            if (saleDate.isEqual(restockDate)){ //Si la fecha de venta es igual a la fecha de reabastecimiento se debe soltar una excepción de entrada, ya que no se puede vender algo el mismo día que los reabastezco
                System.out.println("\tLAS FECHAS DE VENTA Y REABASTECIMIENTO NO PUEDEN SER IGUALES");
                return allowSaleDate("Información noo valida, ingrese nuevamente");
            } else if (saleDate.isBefore(restockDate)) { //Si la fehca de venta es antes que la fecha de reabastecimiento no tendría sentido ya que ese antes de la fecha de reabastecimiento no tendría producto o tendría pero en menor cantidad a la pide un cliente
                System.out.println("\tLA FECHA DE VENTA NO PUEDE SER ANTES A LA FECHA DE REABASTECIMIENTO");
                return allowSaleDate("Información noo valida, ingrese nuevamente");
            } else {
                return saleDate;
            }
        }catch (DateTimeParseException e){ //Si el formato de la fecha ingresada no es correcto se lanza una excepción
            System.out.println(warning);
            return allowSaleDate("Información noo valida, ingrese nuevamente");
        }
    }

    public LocalDate deliveryTime(LocalDate saleDate){
        return saleDate.plusDays(MAXTIME); //Se hace uso de un método .plusDays para que así la fecha se actualice de manera automática, esto es útil si se ingresa una fecha cerca del fin de mes el método automáticamente desplegara la fecha del siguiente mes
    }

    public LocalDate restock(String warning){
        LocalDate todayDate;
        try {
            todayDate = allowDate("Ingrese la fecha de reabastecimiento (dd/MM/aaaa): ", "Información no valida, ingrese nuevamente");
            if (todayDate.isEqual(restockDate)){ //SI la fecha que ingreso el usuario es igual a la fecha de reabastecimiento del inicio entonces se puede reabastecer el producto
                System.out.println("\tLA FECHA INGRESADA COINCIDE CON LA FECHA DE REABASTECIMIENTO");
                return todayDate;
            } else {
                System.out.println("\tLA FECHA INGRESADA NO COINCIDE CON LA FECHA DE REABASTECIMIENTO, INGRESE NUEVAMENTE");
                return restock("\tEL FORMATO DE LA FECHA ES INCORRECTO");
            }
        }catch (DateTimeParseException e){ //Si el formato es incorrecto se lanza una excepción
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
            try{ //Se hace uso de un try - catch para que en el caso el usuario ingrese un carácter por error en las variables que piden numeros se debe dr la oportunidad de ingresar valores correctos sin detener la ejecución
                System.out.print(message);
                value = Integer.parseInt(entry.nextLine()); //Mediante el método Integer.parse se garantiza que el String ingresado por el usuario se convierte en el tipo Integer para su posterior uso
                allow = valorPermitido(value, max, "Valor no permitido/Sobrepasa el total");
            } catch (NumberFormatException e){ //Si la conversioón falla entonces significa que el usuario ingreso un valor que no representa un Integer y por lo tanto se lanza una excepción
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
            try{ //Se usa un try - catch para atrapar cualquier valor no deseado como carácteres o símbolos extraños que no representan números
                System.out.print(message);
                value = Double.parseDouble(entry.nextLine());
                allow = costoPermitido(value, max, "Valor no permitido/Sobrepasa el total");
            } catch(NumberFormatException e){ //Si el valor convertido no es una double se lanza una excepción
                System.out.println("\tNO SE PUEDE INGRESAR CARACTERES EN ESTE ESPACIO");
            }
        } while(!allow);
        return value;
    }

    public void ingresarDatos(){
        if (maxSpace == 0){ //Si la cantidad de espacio del inventario en 0 se solicita vender productos para liberar espacio
            System.out.println("\t-> !HA USADO TODO EL ESPACIO DEL INVENTARIO¡ <-");
            System.out.println("\t-> !DEBE VENDER PRODUCTOS PARA LIBERAR ESPACIO¡ <-\n");
            return;
        }
        System.out.println("\tESPACIO DE ALMACENAMIENTO: " + maxSpace + " UNIDADES"); //Se muestra la cantidad de espacio de almacenamiento
        System.out.println("\tPRESUPUESTO DEL INVENTARIO: $" + maxBudget + "\n"); //Se muestra el presupuesto de la empresa destinada al inventario
        System.out.println("=======INGRESO DE PRODUCTO=======");
        name = validName("Ingrese el nombre del producto");
        int amount = ingresoEnteros(maxSpace, "Ingrese la cantidad de productos: ");
        restockDate = allowDate("Ingrese la fecha de reabastecimiento (dd/MM/aaaa): ", "Información no valido, ingrese nuevamente");
        double cost = ingresoCosto(maxBudget, "Ingrese el costo unitario del producto: $ ");
        double pvp = (cost * 1.10); //Es por el 1.10 por el costo unitario para representar las ganancias de la empresa, se uso 1.10 para representar una ganancias exagerada pero tangible por el usuario
        double totalCost = (cost * amount);
        //Se verifica que el costo total no sea mayor al presupuesto, de serlo se paso el límite y por lo tanto no se puede ingresar más productos
        if (totalCost > maxBudget){
            System.out.println("\t-> !HA USADO TODO EL PRESUPUESTO DEL INVENTARIO¡ <-");
            System.out.println("\t-> !NO SE HA AGREGADO EL ULTIMO PRODUCTO QUE INGRESO¡ <-\n");
            return;
        }
        //Se reduce el espacio y presupuesto, pero el costo total de todos los productos del inventario se suma
        maxSpace-= amount;;
        maxBudget-= totalCost;
        maxCost+= totalCost;
        //Se crea un objeto de la clase Producto y luego este objeto se añade al ArrayList productos
        Producto nuevoproducto = new Producto(name, amount,restockDate, pvp, cost, totalCost);
        productos.add(nuevoproducto);
        System.out.println("\tSe ha agregado de manera exitosa el producto: " + name + " con ID: " + nuevoproducto.getId());
    }

    public void mostrarInfo(){
        System.out.println("\t***********************************************");
        System.out.println("\t   DESPLIEGUE DEL INVENTARIO DE LA EMPRESA       ");
        System.out.println("\t***********************************************  ");
        if (productos.isEmpty()){
            System.out.println("\tNO HAY PRODUCTOS A ELIMINAR"); //Si el ArrayList está vacía se lanza una excepción y no se regresa al menu hasta que se tenga algún producto
            return;
        }
        System.out.println("LOS PRODUCTOS REGISTRADOS SON: ");
        System.out.println("======================================================================");
        for(Producto p: productos){
            p.mostrarInfo(); //Se llama a nu método de la clase Producto para hacer uso de la herencia y además para desplegar la información de los productos
            System.out.println("======================================================================");
        }
        System.out.println("\tEL COSTO TOTAL DEL INVENTARIO ES DE: $" + maxCost);
        System.out.println("\tEL PRESUPUESTO DEL INVENTARIO: $" + maxBudget);
    }

    public void eliminarProducto(){
        boolean found = false;
        int idErase;
        if (productos.isEmpty()){ //Si el ArrayList está vacía se lanza una excepción y no se regresa al menu hasta que se tenga algún producto
            System.out.println("\tNO HAY PRODUCTOS A ELIMINAR"); //
            return;
        }
        System.out.println("=========================================");
        for (Producto p: productos){ //Se despliega todos los ID de los productos para facilitar el ingreso de datos
            p.mostrarIds();
            System.out.println("=========================================");
        }
        //Se pide el usuario ingresar un ID para que sea buscado en el ArrayList
        idErase = ingresoEnteros(500, "Ingrese el ID del producto a eliminar: ");
        //Sew hace uso de un for each para recorrer el ArrayList y eliminar el producto con ID seleccionado
        for (Producto p: productos){
            if (p.getId() == idErase){
                found = true; //Si se encuentra el producto se pone el valor TRUE
                maxSpace+=p.getAmount(); //Se restaura el espacio del inventario
                maxBudget+=p.getCost(); //Se restaura el presupuesto del inventario
                maxCost-=p.getCost(); //Se reduce el costo del inventario
                productos.remove(p); //Se quita el producto seleccionado
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
        if (productos.isEmpty()){ //Si el ArrayList está vacía se lanza una excepción y no se regresa al menu hasta que se tenga algún product
            System.out.println("\t NO HAY PRODUCTOS A EDITAR");
            return;
        }
        //Se despliega todos los ID de los productos para darle más facilidad al usuario
        System.out.println("=========================================");
        for (Producto p: productos){
            p.mostrarIds();
            System.out.println("=========================================");
        }
        //Se solicita un ID para que así el usuario puede editar un producto en específico
        idEdit = ingresoEnteros(500, "Ingrese el ID del producto que desea cambiar: ");
        //Se hace uso de un for each para poder editar el producto seleccionado
        for (Producto p: productos){
            if (p.getId() == idEdit){
                found = true; //Si el ID fue encontrado se da el valor de TRUE
                int oldAmount = p.getAmount(); //Se otorga el valor de la cantidad anterior a la variable oldAmount
                double oldTotalCost = p.getTotalCost(); //Se otorga el valor de la cantidad anterior a la variable oldTotalCost
                int restoreSpace = maxSpace + oldAmount; //Se restaura el valor de espacio
                double restoreBudget = maxBudget + oldTotalCost; //Se restaura el valor de presupuesto
                nuevoName = validName("Ingrese el nuevo nombre del producto: ");
                while (nuevoName.equalsIgnoreCase(p.getName())) { //Se debe validar que el nombre nuevo no sea igual al anterior
                    System.out.println("\tEL NUEVO NOMBRE NO PUEDE SER IGUAL AL NOMBRE ANTERIOR");
                    nuevoName = validName("Ingrese un nombre diferente al actual: ");
                }
                nuevoAmount = ingresoEnteros(restoreSpace, "Ingrese la nueva cantidad: ");
                nuevoDate = allowDate("Ingrese la nueva fecha de reabastecimiento (dd/MM/aaaa): ", "Información no valido, ingrese nuevamente");
                nuevoCost = ingresoCosto(restoreBudget, "Ingrese el nuevo costo unitario del producto: $ ");
                nuevoPvp = (nuevoCost * 1.10);
                nuevoTotalCost = (nuevoCost * nuevoAmount);
                //Se verifica que el nuevo costo total no sea mayor al presupuesto
                if (nuevoTotalCost > restoreBudget){
                    System.out.println("\t-> !EL NUEVO COSTO TOTAL EXCEDE EL PRESUPUESTO: $" + restoreBudget + "¡ <-");
                    System.out.println("\t-> !NO SE HA EDITADO EL ULTIMO PRODUCTO QUE INGRESO¡ <-\n");
                }
                //Con los nuevos valores se retira de cantidades del espacio, presupuesto y costo total
                maxBudget = (restoreBudget - nuevoTotalCost);
                maxCost = maxCost - oldTotalCost + nuevoTotalCost;
                maxSpace = maxSpace + oldAmount - nuevoAmount;
                //Se asignan los nuevos valores a la posición seleccionada mediante setters
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

        if (productos.isEmpty()){ //Si el ArrayList está vacía se lanza una excepción y no se regresa al menu hasta que se tenga algún product
            System.out.println("\tNO HAY PRODUCTOS A VENDER");
            return;
        }
        System.out.println("=========================================");
        for (Producto p: productos){
            p.mostrarIds();
            System.out.println("=========================================");
        }
        //Se pide un ID para que así se realice la venta del producto
        saleId = ingresoEnteros(500, "Ingrese el ID del producto que desea vender: ");
        //Se hace uso de un for each para que asi se pueda vender el producto
        for (Producto p: productos){
            if (p.getId() == saleId){
                found = true; //Si se encuentra se asigna el valor de TRUE
                if (p.getAmount() == 0){ //Si la cantidad es 0 se debe reabastecer
                    System.out.println("\tPRODUCTO AGOTADO, DEBE REABASTECER");
                    entry.nextLine();
                    restock("\tEL FORMATO DE LA FECHA ES INCORRECTO");
                    restockAmount = ingresoEnteros(maxSpace, "Ingrese la cantidad para reabastecer el producto: ");
                    p.setAmount(restockAmount); //La nueva cantidad se obtiene asignando el valor de nueva cantidad al set de cantidad
                    maxSpace-=restockAmount; //Se retira el espacio con la nueva cantidad
                    p.setTotalCost(restockAmount * p.getCost());
                    maxBudget-=p.getTotalCost(); //Se retira el presupuesto con el nuevo costo total
                    System.out.println("====== SE HA REABASTECIDO CON EXITO ======");
                    System.out.println("AHORA TIENE: " + p.getAmount() + " UNIDADES");
                    System.out.println("ESPACIO: " + maxSpace);
                    System.out.println("PRESUPUESTO: $" + maxBudget);
                    System.out.println("=========================================");
                    System.out.println("\tSE PROCEDE A LA VENTA");
                }

                saleDate = allowSaleDate("Información no valida, ingrese nuevamente");
                System.out.println("=====================================");
                System.out.println("El producto con nombre: " + p.getName() //Se muestra la información del producto
                        + "\nID: " + saleId
                        + "\nCantidad disponible: " + p.getAmount());
                System.out.println("=====================================");
                saleAmount = ingresoEnteros(p.getAmount(), "Ingrese la cantidad a vender: "); //La cantidad a vender no puede ser mayor a la cantidad del producto
                p.setAmount(p.getAmount() - saleAmount); //La cantidad restante se calcula reduciendo la cantidad vendida a la cantidad original
                p.setTotalCost(p.getAmount() * p.getCost()); //El costo total se calcula con la cantidad restante y el costo unitario
                maxCost-=(p.getCost() * saleAmount); //El costo del inventario se reduce
                System.out.println("=====================================");
                System.out.println("\tUNIDADES: " + p.getAmount());
                maxSpace+=saleAmount; //Se libera cantidad de espacio asi que aumenta
                System.out.println("\tESPACIO: " + maxSpace);
                System.out.println("=====================================");
                deliveryDate = deliveryTime(saleDate); //Se muestra el día límite para que llegue el producto
                System.out.println("El producto llegara en un limite de " + MAXTIME + " dias, es decir el " + deliveryDate);
                sale = (p.getPvp() * saleAmount); //Se obtiene la venta que son ingresos para la empresa
                System.out.print("\n\tLA VENTA SE REALIZO POR UN PRECIO DE: $" + sale);
                maxBudget+=sale; //Al presupuesto se suma el valor de la venta realizada
                System.out.println("\n\tPRESUPUESTO DEL INVENTARIO TRAS LA VENTA: $" + maxBudget);
                break;
            }
        }

        if (!found){
            System.out.println("\t=== EL ID ingresado no existe en el inventario para vender ===\n");
        }
    }
}
