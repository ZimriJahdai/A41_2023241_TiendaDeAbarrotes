package org.zix.ActividadTaller.controller;

import org.zix.ActividadTaller.model.producto;
import org.zix.ActividadTaller.service.ProductoService;
import org.zix.ActividadTaller.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.faces.view.ViewScoped;
import java.util.HashMap;
import java.util.Map;

@Component
@ViewScoped
public class VentaController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProductoService ventaService;

    private Map<Long, Integer> cantidades = new HashMap<>();
    private Map<Long, producto> carrito = new HashMap<>();

    public void agregarProducto(producto producto) {
        Integer cantidad = cantidades.get(producto.getId());
        if (cantidad != null && cantidad > 0 && cantidad <= producto.getCantidad()) {
            carrito.put(producto.getId(), producto);
        }
    }

    public void procesarVenta() {
        // Lógica para procesar la venta
        for (Map.Entry<Long, producto> entry : carrito.entrySet()) {
            Long productoId = entry.getKey();
            producto producto = entry.getValue();
            Integer cantidad = cantidades.get(productoId);

            if (cantidad != null) {
                ventaService.actualizarStock(producto.getCodigo(), cantidad);
            }
        }
        limpiarCarrito();
    }

    public void limpiarCarrito() {
        carrito.clear();
        cantidades.clear();
    }

    public double getTotal() {
        double total = 0;
        for (Map.Entry<Long, producto> entry : carrito.entrySet()) {
            Long productoId = entry.getKey();
            producto producto = entry.getValue();
            Integer cantidad = cantidades.get(productoId);

            if (cantidad != null) {
                total += producto.getPrecio() * cantidad;
            }
        }
        return total;
    }

    public Map<Long, Integer> getCantidades() { return cantidades; }
    public void setCantidades(Map<Long, Integer> cantidades) { this.cantidades = cantidades; }
    public Map<Long, producto> getCarrito() { return carrito; }
    public void setCarrito(Map<Long, producto> carrito) { this.carrito = carrito; }
}