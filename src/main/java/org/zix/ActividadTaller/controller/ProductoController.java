package org.zix.ActividadTaller.controller;

import org.zix.ActividadTaller.model.producto;
import org.zix.ActividadTaller.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.util.List;

@Component
@ViewScoped
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    private producto nuevoProducto = new producto();
    private producto productoSeleccionado;
    private String codigoProductoEliminar;

    // CREATE - Añadir nuevo producto
    public void añadirProducto() {
        try {
            productoService.añadirProducto(nuevoProducto);
            nuevoProducto = new producto(); // Resetear formulario

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto añadido correctamente"));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    // READ - Obtener todos los productos
    public List<producto> obtenerTodosProductos() {
        return productoService.obtenerTodosProductos();
    }

    // UPDATE - Actualizar producto
    public void actualizarProducto() {
        try {
            if (productoSeleccionado != null) {
                productoService.editarProducto(
                        productoSeleccionado.getCodigo(),
                        productoSeleccionado.getCodigo(),
                        productoSeleccionado.getNombre(),
                        productoSeleccionado.getPrecio(),
                        productoSeleccionado.getCantidad()
                );

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto actualizado correctamente"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    // DELETE - Eliminar producto
    public void eliminarProducto() {
        try {
            if (codigoProductoEliminar != null && !codigoProductoEliminar.trim().isEmpty()) {
                productoService.eliminarProducto(codigoProductoEliminar);
                codigoProductoEliminar = null;

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto eliminado correctamente"));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    // Método para preparar la edición
    public void prepararEdicion(producto producto) {
        this.productoSeleccionado = producto;
    }

    // Método para preparar eliminación
    public void prepararEliminacion(String codigo) {
        this.codigoProductoEliminar = codigo;
    }

    // Getters y Setters
    public producto getNuevoProducto() { return nuevoProducto; }
    public void setNuevoProducto(producto nuevoProducto) { this.nuevoProducto = nuevoProducto; }

    public producto getProductoSeleccionado() { return productoSeleccionado; }
    public void setProductoSeleccionado(producto productoSeleccionado) { this.productoSeleccionado = productoSeleccionado; }

    public String getCodigoProductoEliminar() { return codigoProductoEliminar; }
    public void setCodigoProductoEliminar(String codigoProductoEliminar) { this.codigoProductoEliminar = codigoProductoEliminar; }
}