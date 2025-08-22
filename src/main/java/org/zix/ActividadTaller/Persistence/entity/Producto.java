

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private double precio;
    private int cantidad;
    private String descripcion;

    // Constructor, getters, setters, y toString
    public Producto(String nombre, double precio, int cantidad, String descripcion) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
    }

    public Producto(int id, String nombre, double precio, int cantidad, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
    }

    // Getters y setters aquí

    @Override
    public String toString() {
        return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", cantidad=" + cantidad + ", descripcion=" + descripcion + "]";
    }
}
