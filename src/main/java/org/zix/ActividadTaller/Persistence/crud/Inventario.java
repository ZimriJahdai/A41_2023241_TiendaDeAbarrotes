package org.zix.ActividadTaller.Persistence.crud;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zix.ActividadTaller.dominio.service.entity.Producto;
import org.zix.ActividadTaller.dominio.service.Persistance.crud.ProductoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class Inventario {

    @Autowired
    private ProductoRepository productoRepository;

    // Agregar un nuevo producto
    public void agregarProducto(Producto producto) {
        productoRepository.save(producto);  // Save agrega o actualiza el producto
        System.out.println("Producto agregado exitosamente.");
    }

    // Listar todos los productos
    public List<Producto> listarProductos() {
        return productoRepository.findAll();  // Devuelve todos los productos
    }

    // Buscar un producto por ID
    public Producto buscarProducto(int id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.orElse(null);  // Si no encuentra el producto, devuelve null
    }

    // Actualizar precio o cantidad de un producto
    public void actualizarProducto(int id, double nuevoPrecio, int nuevaCantidad) {
        Producto producto = buscarProducto(id);
        if (producto != null) {
            producto.setPrecio(nuevoPrecio);
            producto.setCantidad(nuevaCantidad);
            productoRepository.save(producto);  // Save lo actualiza
            System.out.println("Producto actualizado exitosamente.");
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    // Eliminar un producto
    public void eliminarProducto(int id) {
        productoRepository.deleteById(id);  // Elimina el producto por id
        System.out.println("Producto eliminado exitosamente.");
    }
}


