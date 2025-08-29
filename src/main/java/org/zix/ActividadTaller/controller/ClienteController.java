package org.zix.ActividadTaller.controller;

import org.zix.ActividadTaller.service.VentaService;
import org.zix.ActividadTaller.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;

@Component
@ViewScoped
public class ClienteController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private VentaService ventaService;

    private Map<Long, String> cantidades = new HashMap<>();

    public void realizarCompra() {
        try {
            // Convertir String a Integer
            Map<Long, Integer> itemsParaVender = new HashMap<>();
            for (Map.Entry<Long, String> entry : cantidades.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                    try {
                        Integer cantidad = Integer.parseInt(entry.getValue().trim());
                        if (cantidad > 0) {
                            itemsParaVender.put(entry.getKey(), cantidad);
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar valores no numéricos
                        System.out.println("Valor no numérico ignorado: " + entry.getValue());
                    }
                }
            }

            if (itemsParaVender.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Seleccione al menos un producto para comprar"));
                return;
            }

            // Realizar la venta
            ventaService.realizarVenta(itemsParaVender);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Compra realizada correctamente"));

            cantidades.clear();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            e.printStackTrace();
        }
    }

    public Map<Long, String> getCantidades() {
        return cantidades;
    }
}