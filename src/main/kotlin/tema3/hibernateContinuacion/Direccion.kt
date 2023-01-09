package tema3.hibernateContinuacion

import jakarta.persistence.*

@Entity
@Table(name = "direcciones")
class Direccion(

    @Column(name= "calle")
    var calle: String,

    @Column(name= "cp")
    var cp: String,

    @Column(name= "numero")
    var numero: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null
) {
}