package org.zix.ActividadTaller.console;

import org.zix.ActividadTaller.model.producto;
import org.zix.ActividadTaller.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class ConsoleMenu {

    @Autowired
    private ProductoService productoService;

    private Scanner scanner = new Scanner(System.in);

    public void start() {
        boolean running = true;

        while (running) {
            mostrarMenuPrincipal();
            int option = obtenerOpcion();

            switch (option) {
                case 1 -> añadirProducto();
                case 2 -> listarTodosProductos();
                case 3 -> buscarProductoPorCodigo();
                case 4 -> buscarProductosPorNombre();
                case 5 -> actualizarPrecio();
                case 6 -> actualizarStock();
                case 7 -> editarProductoCompleto();
                case 8 -> eliminarProducto();
                case 9 -> mostrarProductosStockBajo();
                case 10 -> mostrarValorTotalInventario();
                case 11 -> running = false;
                default -> System.out.println(" Opción no válida");
            }
        }
        System.out.println("¡Hasta pronto!");
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n===  SISTEMA DE INVENTARIO ===");
        System.out.println("1. ** Añadir producto **");
        System.out.println("2. ** Listar todos los productos **");
        System.out.println("3. ** Buscar producto por código **");
        System.out.println("4. ** Buscar productos por nombre **");
        System.out.println("5. ** Actualizar precio **");
        System.out.println("6. ** Actualizar stock **");
        System.out.println("7. **  Editar producto completo **");
        System.out.println("8. **  Eliminar producto **");
        System.out.println("9. **  Productos con stock bajo **");
        System.out.println("10. ** Valor total del inventario **");
        System.out.println("11. ** Salir **");
        System.out.print("Seleccione una opción: ");
    }

    private int obtenerOpcion() {
        try {
            int option = scanner.nextInt();
            scanner.nextLine();
            return option;
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }

    private void añadirProducto() {
        System.out.println("\n---  AÑADIR PRODUCTO ---");
        System.out.print("Código: ");
        String codigo = scanner.nextLine().toLowerCase();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Precio: ");
        Double precio = scanner.nextDouble();
        System.out.print("Cantidad: ");
        Integer cantidad = scanner.nextInt();
        scanner.nextLine();

        try {
            producto producto = new producto(codigo, nombre, precio, cantidad);
            productoService.añadirProducto(producto);
            System.out.println(" Producto añadido exitosamente");
        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

    private void listarTodosProductos() {
        System.out.println("\n---  LISTA DE PRODUCTOS ---");
        List<producto> productos = productoService.obtenerTodosProductos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados");
        } else {
            productos.forEach(System.out::println);
            System.out.println("Total de productos: " + productos.size());
        }
    }

    private void buscarProductoPorCodigo() {
        System.out.print("\nIngrese el código del producto: ");
        String codigo = scanner.nextLine().toLowerCase();

        Optional<producto> producto = productoService.buscarProductoPorCodigo(codigo);
        if (producto.isPresent()) {
            System.out.println(" Producto encontrado:");
            System.out.println(producto.get());
        } else {
            System.out.println(" Producto no encontrado");
        }
    }

    private void buscarProductosPorNombre() {
        System.out.print("\nIngrese el nombre o parte del nombre: ");
        String nombre = scanner.nextLine();

        List<producto> productos = productoService.buscarProductosPorNombre(nombre);
        if (productos.isEmpty()) {
            System.out.println(" No se encontraron productos con ese nombre");
        } else {
            System.out.println(" Productos encontrados:");
            productos.forEach(System.out::println);
            System.out.println("Total encontrados: " + productos.size());
        }
    }

    private void actualizarPrecio() {
        System.out.print("\nIngrese el código del producto: ");
        String codigo = scanner.nextLine().toLowerCase();
        System.out.print("Nuevo precio: ");
        Double nuevoPrecio = scanner.nextDouble();
        scanner.nextLine();

        try {
            producto actualizado = productoService.actualizarPrecio(codigo, nuevoPrecio);
            System.out.println(" Precio actualizado:");
            System.out.println(actualizado);
        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

    private void actualizarStock() {
        System.out.print("\nIngrese el código del producto: ");
        String codigo = scanner.nextLine().toLowerCase();
        System.out.print("Cambio en stock (+ para entrada, - para salida): ");
        Integer cambio = scanner.nextInt();
        scanner.nextLine();

        try {
            producto actualizado = productoService.actualizarStock(codigo, cambio);
            System.out.println(" Stock actualizado:");
            System.out.println(actualizado);
        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        System.out.print("\nIngrese el código del producto a eliminar: ");
        String codigo = scanner.nextLine().toLowerCase();

        try {

            Optional<producto> producto = productoService.buscarProductoPorCodigo(codigo);
            if (producto.isPresent()) {
                System.out.println("Producto a eliminar:");
                System.out.println(producto.get());
                System.out.print("¿Está seguro? (s/n): ");
                String confirmacion = scanner.nextLine();

                if (confirmacion.equalsIgnoreCase("s")) {
                    productoService.eliminarProducto(codigo);
                    System.out.println(" Producto eliminado exitosamente");
                } else {
                    System.out.println(" Eliminación cancelada");
                }
            } else {
                System.out.println(" Producto no encontrado");
            }
        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

    private void mostrarProductosStockBajo() {
        System.out.println("\n---  PRODUCTOS CON STOCK BAJO ---");
        List<producto> productos = productoService.obtenerProductosStockBajo();
        if (productos.isEmpty()) {
            System.out.println(" No hay productos con stock bajo");
        } else {
            productos.forEach(System.out::println);
            System.out.println("Total con stock bajo: " + productos.size());
        }
    }

    private void mostrarValorTotalInventario() {
        System.out.println("\n--- VALOR TOTAL DEL INVENTARIO ---");
        Double valorTotal = productoService.obtenerValorTotalInventario();
        System.out.printf("Valor total del inventario: $%.2f%n", valorTotal);
    }

    private void editarProductoCompleto() {
        System.out.println("\n--- ✏️ EDITAR PRODUCTO COMPLETO ---");
        System.out.print("Ingrese el código actual del producto: ");
        String codigoActual = scanner.nextLine().toLowerCase();

        try {

            Optional<producto> productoOpt = productoService.buscarProductoPorCodigo(codigoActual);
            if (productoOpt.isEmpty()) {
                System.out.println(" Producto no encontrado");
                return;
            }

            producto productoActual = productoOpt.get();
            System.out.println("Producto actual:");
            System.out.println(productoActual);
            System.out.println("\nIngrese los nuevos datos (deje en blanco para mantener el valor actual):");


            System.out.print("Nuevo código [" + productoActual.getCodigo() + "]: ");
            String nuevoCodigo = scanner.nextLine();
            if (nuevoCodigo.trim().isEmpty()) {
                nuevoCodigo = productoActual.getCodigo();
            }


            System.out.print("Nuevo nombre [" + productoActual.getNombre() + "]: ");
            String nuevoNombre = scanner.nextLine();
            if (nuevoNombre.trim().isEmpty()) {
                nuevoNombre = productoActual.getNombre();
            }


            System.out.print("Nuevo precio [" + productoActual.getPrecio() + "]: ");
            String precioInput = scanner.nextLine();
            Double nuevoPrecio;
            if (precioInput.trim().isEmpty()) {
                nuevoPrecio = productoActual.getPrecio();
            } else {
                nuevoPrecio = Double.parseDouble(precioInput);
            }


            System.out.print("Nueva cantidad [" + productoActual.getCantidad() + "]: ");
            String cantidadInput = scanner.nextLine();
            Integer nuevaCantidad;
            if (cantidadInput.trim().isEmpty()) {
                nuevaCantidad = productoActual.getCantidad();
            } else {
                nuevaCantidad = Integer.parseInt(cantidadInput);
            }


            System.out.println("\n¿Confirmar cambios? (s/n)");
            System.out.println("Nuevo código: " + nuevoCodigo);
            System.out.println("Nuevo nombre: " + nuevoNombre);
            System.out.println("Nuevo precio: " + nuevoPrecio);
            System.out.println("Nueva cantidad: " + nuevaCantidad);
            System.out.print("Confirmar: ");

            String confirmacion = scanner.nextLine();
            if (confirmacion.equalsIgnoreCase("s")) {
                producto productoEditado = productoService.editarProducto(
                        codigoActual, nuevoCodigo, nuevoNombre, nuevoPrecio, nuevaCantidad
                );
                System.out.println(" Producto editado exitosamente:");
                System.out.println(productoEditado);
            } else {
                System.out.println(" Edición cancelada");
            }

        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }
}
