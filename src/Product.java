import java.util.Objects;

public class Product
{
    private String ID;
    private String name;
    private String description;
    private double cost;

    public Product (String ID, String name, String description, double cost)
    {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    // Getters and Setters
    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    /** Returns a String which lists all the fields' values for this Product separated by commas.
     *
     * @return a String which lists all the fields' values for this Product separated by commas.
     */
    public String toCSV()
    {
        return (this.ID + ", " + this.name + ", " + this.description + ", " + this.cost);
    }

    /** Returns a String which lists all the fields' values for this Product in JSON syntax.
     *
     * @return a String which lists all the fields' values for this Product in JSON syntax
     */
    public String toJSON()
    {
        final char DQ = '"';    // DQ removes the need for /" and improves the readability of the returned String
        return ("{" + DQ + "ID" + DQ + ":" + DQ + this.ID + DQ + ", " + DQ + "name" + DQ + ":" + DQ + this.name + DQ + ", " + DQ + "description" + DQ + ":" + DQ + this.description + DQ + ", " + DQ + "cost" + DQ + ":" + this.cost + "}");
    }

    /** Returns a String which lists all the fields' values for this Product in XML syntax.
     *
     * @return a String which lists all the fields' values for this Product in XML syntax
     */
    public String toXML()
    {
        return ("<product><ID>" + this.ID + "</ID><name>" + this.name + "</name><description>" + this.description + "</description><cost>" + this.cost + "</cost></product>");
    }

    @Override
    public String toString() {
        return "Product{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(cost, product.cost) == 0 && Objects.equals(ID, product.ID) && Objects.equals(name, product.name) && Objects.equals(description, product.description);
    }
}
