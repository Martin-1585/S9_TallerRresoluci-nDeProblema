package udla.molalla.S9ResolucionProblemas.clases;

import java.time.LocalDate;

public class Producto {
    /**Declaración de atributos*/
    private String name;
    private int amount;
    private LocalDate restockDate;
    private double pvp;
    private static int contador = 1;
    private int id;
    private double cost;
    /**Declaración de constructores*/
    public Producto() {
    }

    public Producto(String name, int amount, LocalDate restockDate, double pvp, double cost) {
        this.name = name;
        this.amount = amount;
        this.restockDate = restockDate;
        this.pvp = pvp;
        this.id = contador++;
        this.cost = cost;
    }
    /**Métodos propios de Java*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getRestockDate() {
        return restockDate;
    }

    public void setRestockDate(LocalDate restockDate) {
        this.restockDate = restockDate;
    }

    public double getPvp() {
        return pvp;
    }

    public void setPvp(double pvp) {
        this.pvp = pvp;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    /**Métodos propios del desarrollador*/
    public void mostrarInfo(){
        System.out.println("ID:" + id +
                "\nNombre:" + name +
                "\nCantidad: " + amount +
                "\nFecha de reabastecimiento:" + restockDate +
                "\nPVP: $" + pvp +
                "\nCosto: $" + cost);

    }

    public void mostrarIds(){
        System.out.println("Producto " + id + " con ID: " + id);
    }
}
