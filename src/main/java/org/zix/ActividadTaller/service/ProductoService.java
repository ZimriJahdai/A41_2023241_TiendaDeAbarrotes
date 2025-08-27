package org.zix.ActividadTaller.service;

import org.zix.ActividadTaller.model.producto;
import org.zix.ActividadTaller.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;


    public producto añadirProducto(producto producto) {
        if (productoRepository.existsByCodigo(producto.getCodigo())) {
            throw new RuntimeException("Ya existe un producto con ese código");
        }
        return productoRepository.save(producto);
    }


    public List<producto> obtenerTodosProductos() {
        return productoRepository.findAll();
    }


    public Optional<producto> buscarProductoPorCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo);
    }


    public List<producto> buscarProductosPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }


    public producto actualizarPrecio(String codigo, Double nuevoPrecio) {
        producto producto = productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setPrecio(nuevoPrecio);
        return productoRepository.save(producto);
    }


    public producto actualizarStock(String codigo, Integer cambioCantidad) {
        producto producto = productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        int nuevaCantidad = producto.getCantidad() + cambioCantidad;
        if (nuevaCantidad < 0) {
            throw new RuntimeException("No hay suficiente stock");
        }

        producto.setCantidad(nuevaCantidad);
        return productoRepository.save(producto);
    }


    public void eliminarProducto(String codigo) {
        if (!productoRepository.existsByCodigo(codigo)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteByCodigo(codigo);
    }


    public List<producto> obtenerProductosStockBajo() {
        return productoRepository.findProductosConStockBajo(10);
    }


    public Double obtenerValorTotalInventario() {
        return productoRepository.calcularValorTotalInventario();
    }

    public producto editarProducto(String codigoActual, String nuevoCodigo, String nuevoNombre, Double nuevoPrecio, Integer nuevaCantidad) {
        producto producto = productoRepository.findByCodigo(codigoActual)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));


        if (!codigoActual.equals(nuevoCodigo) && productoRepository.existsByCodigo(nuevoCodigo)) {
            throw new RuntimeException("Ya existe un producto con el nuevo código: " + nuevoCodigo);
        }


        producto.setCodigo(nuevoCodigo);
        producto.setNombre(nuevoNombre);
        producto.setPrecio(nuevoPrecio);
        producto.setCantidad(nuevaCantidad);

        return productoRepository.save(producto);
    }
}