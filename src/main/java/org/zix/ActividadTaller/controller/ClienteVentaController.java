package org.zix.ActividadTaller.controller;

import org.zix.ActividadTaller.model.producto;
import org.zix.ActividadTaller.service.VentaService;
import org.zix.ActividadTaller.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.faces.view.ViewScoped;
import java.util.*;

@Component
@ViewScoped
public class ClienteVentaController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    private Map<Long, Integer> cantidades = new HashMap<>();
    private List<producto> carrito = new ArrayList<>();

    public void agregarAlCarrito(producto producto) {
        Integer cantidad = cantidades.get(producto.getId());
        if (cantidad != null && cantidad > 0 && cantidad <= producto.getCantidad()) {
            // Verificar si el producto ya está en el carrito
            boolean existe = carrito.stream()
                    .anyMatch(p -> p.getId().equals(producto.getId()));

            if (!existe) {
                carrito.add(producto);
            }
        }
    }

    public void removerDelCarrito(producto producto) {
        carrito.removeIf(p -> p.getId().equals(producto.getId()));
        cantidades.remove(producto.getId());
    }

    public void realizarCompra() {
        Map<Long, Integer> itemsVenta = new HashMap<>();

        for (producto producto : carrito) {
            Integer cantidad = cantidades.get(producto.getId());
            if (cantidad != null && cantidad > 0) {
                itemsVenta.put(Long.valueOf(producto.getCodigo()), cantidad);
            }
        }

        if (!itemsVenta.isEmpty()) {
            ventaService.realizarVenta(itemsVenta);
            limpiarCarrito();
        }
    }

    public void limpiarCarrito() {
        carrito.clear();
        cantidades.clear();
    }

    public double getTotalVenta() {
        double total = 0;
        for (producto producto : carrito) {
            Integer cantidad = cantidades.get(producto.getId());
            if (cantidad != null) {
                total += producto.getPrecio() * cantidad;
            }
        }
        return total;
    }

    public Map<Long, Integer> getCantidades() { return cantidades; }
    public List<producto> getCarrito() { return carrito; }
}