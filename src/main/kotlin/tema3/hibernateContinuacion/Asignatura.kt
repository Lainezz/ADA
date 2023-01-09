package tema3.hibernateContinuacion

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name= "asignaturas")
class Asignatura(

    @Id
    var id: Int
) {
}