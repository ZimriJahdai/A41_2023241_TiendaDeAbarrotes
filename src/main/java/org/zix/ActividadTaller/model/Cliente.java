package org.zix.ActividadTaller.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "cliente")
//Lombok
@Data  //genera los Setter y getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode//codigo de autenticacion de la entidad
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    // Constructores, getters y setters
    public Cliente() {
        this.fechaRegistro = new Date();
    }

    public Cliente(String nombre, String email, String telefono) {
        this();
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

}