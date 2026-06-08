package Entity;

public abstract class Phone {
    // Attributes
    protected int id;
    protected String name;
    protected double price;
    protected int quantity;
    protected String manufacturer;

    // Constructor
    public Phone(int id, String name, double price, int quantity, String manufacturer) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.manufacturer = manufacturer;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getManufacturer() { return manufacturer; }

    // Abstract method
    public abstract void displayInfo();
}