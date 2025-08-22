package org.zix.ActividadTaller.Persistence.crud;



import org.springframework.data.jpa.repository.JpaRepository;
import org.zix.ActividadTaller.dominio.service.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // JpaRepository se encarga de las operaciones CRUD, no es necesario escribir código adicional
}
