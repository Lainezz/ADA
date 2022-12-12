package tema3

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="alumnos")
class Alumno(
    @Id
    @Column(name="dni")
    var dni: String,

    @Column(name="nombre")
    var nombre: String,

    @Column(name="edad")
    var edad: Int,

    @Column(name="ciudad")
    var ciudad: String
    ) {
}