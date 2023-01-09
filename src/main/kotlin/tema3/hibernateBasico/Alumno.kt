package tema3.hibernateBasico

import jakarta.persistence.*

@Entity
@Table(name = "alumnos")
class Alumno(
    @Id
    @Column(name = "dni")
    var dni: String,

    @Column(name = "nombre")
    var nombre: String,

    @Column(name = "edad")
    var edad: Int,

    @Column(name = "Ciudad")
    var ciudad: String,
) {
}