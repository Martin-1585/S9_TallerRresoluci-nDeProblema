
public class Producto {
    /**Declaración de atributos*/
    private String name;
    private int amount;
    private int restockYear;
    private int restockMonth;
    private int restockDay;
    private double pvp;
    private static int contador = 1;
    private int id;
    private double cost;
    /**Declaración de constructores*/
    public Producto() {
    }

    public Producto(String name, int amount, int restockYear, int restockMonth, int restockDay, double pvp, double cost) {
        this.name = name;
        this.amount = amount;
        this.restockYear = restockYear;
        this.restockMonth = restockMonth;
        this.restockDay = restockDay;
        this.pvp = pvp;
        this.id = contador++;
        this.cost = cost;
    }

    /**Métodos propios de Java*/
    public int getId() {
        return id;
    }

    /**Métodos propios del desarrollador*/
    public void mostrarInfo(){
        System.out.println("Producto:" + id +
                "\nNombre:" + name +
                "\nCantidad: " + amount +
                "\nFecha de reabastecimiento:" + restockYear + "/" + restockMonth + "/" + restockDay +
                "\nPVP: $" + pvp +
                "\nCosto: $" + cost);

    }
}
