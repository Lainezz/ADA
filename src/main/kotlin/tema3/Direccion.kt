package tema3

import jakarta.persistence.*

@Entity
@Table(name = "direcciones")
class Direccion(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name  = "id")
    var id: Long? = null,
    @Column(name  = "calle")
    var calle: String,
    @Column(name  = "numero")
    var numero: Int,
    @Column(name  = "cp")
    var cp: String) {
}