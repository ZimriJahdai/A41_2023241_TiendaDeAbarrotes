package org.zix.ActividadTaller.repository;

import org.zix.ActividadTaller.model.producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<producto, Long> {


    Optional<producto> findByCodigo(String codigo);


    boolean existsByCodigo(String codigo);


    void deleteByCodigo(String codigo);


    List<producto> findByNombreContainingIgnoreCase(String nombre);


    @Query("SELECT p FROM producto p WHERE p.cantidad < :cantidadMinima")
    List<producto> findProductosConStockBajo(@Param("cantidadMinima") Integer cantidadMinima);


    @Query("SELECT SUM(p.precio * p.cantidad) FROM producto p")
    Double calcularValorTotalInventario();
}