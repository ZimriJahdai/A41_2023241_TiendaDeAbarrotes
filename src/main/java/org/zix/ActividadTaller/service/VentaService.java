package org.zix.ActividadTaller.service;

import org.zix.ActividadTaller.model.producto;
import org.zix.ActividadTaller.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
@Transactional
public class VentaService {

    @Autowired
    private ProductoRepository productoRepository;

    public void realizarVenta(Map<Long, Integer> itemsVenta) {
        for (Map.Entry<Long, Integer> entry : itemsVenta.entrySet()) {
            Long productoId = entry.getKey();
            Integer cantidad = entry.getValue();

            if (cantidad != null && cantidad > 0) {
                producto producto = productoRepository.findById(productoId)
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

                if (producto.getCantidad() < cantidad) {
                    throw new RuntimeException("Stock insuficiente para: " + producto.getNombre() +
                            ". Stock disponible: " + producto.getCantidad());
                }

                // actualizar stock en la base de datos
                int nuevoStock = producto.getCantidad() - cantidad;
                producto.setCantidad(nuevoStock);
                productoRepository.save(producto);

                System.out.println("Venta realizada: " + cantidad + " unidades de " + producto.getNombre() +
                        ". Nuevo stock: " + nuevoStock);


                registrarVentaCompleta(producto, cantidad);
            }
        }
    }

    private void registrarVentaCompleta(producto producto, Integer cantidad) {
        System.out.println("Registrando venta en sistema: " + producto.getNombre() +
                ", Cantidad: " + cantidad +
                ", Precio: $" + producto.getPrecio() +
                ", Total: $" + (producto.getPrecio() * cantidad));


    }
}