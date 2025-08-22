package org.zix.ActividadTaller.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zix.ActividadTaller.dominio.service.Persistance.crud.Inventario;
import org.zix.ActividadTaller.dominio.service.entity.Producto;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private Inventario inventario;

    // Endpoint para agregar un producto
    @PostMapping
    public String agregarProducto(@RequestBody Producto producto) {
        inventario.agregarProducto(producto);
        return "Producto agregado correctamente.";
    }

    // Endpoint para listar productos
    @GetMapping
    public List<Producto> listarProductos() {
        return inventario.listarProductos();
    }

    // Endpoint para buscar un producto por id
    @GetMapping("/{id}")
    public Producto buscarProducto(@PathVariable int id) {
        return inventario.buscarProducto(id);
    }

    // Endpoint para actualizar un producto
    @PutMapping("/{id}")
    public String actualizarProducto(@PathVariable int id, @RequestBody Producto producto) {
        inventario.actualizarProducto(id, producto.getPrecio(), producto.getCantidad());
        return "Producto actualizado correctamente.";
    }

    // Endpoint para eliminar un producto
    @DeleteMapping("/{id}")
    public String eliminarProducto(@PathVariable int id) {
        inventario.eliminarProducto(id);
        return "Producto eliminado correctamente.";
    }
}

