package tema3.hibernateContinuacion

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

    @Column(name = "ciudad")
    var ciudad: String,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name= "id_direccion")
    var direccion: Direccion,

    //@OneToMany(mappedBy = "alumno", cascade = [CascadeType.ALL])
    //var asignaturas: List<Asignatura> = mutableListOf<Asignatura>()
) {
}