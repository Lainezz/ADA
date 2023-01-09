import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import tema3.hibernateContinuacion.Alumno
import tema3.hibernateContinuacion.Direccion

fun main() {


    val emf: EntityManagerFactory = Persistence.createEntityManagerFactory("PersistenciaContinuacion")
    val manager: EntityManager = emf.createEntityManager()

    var dir1: Direccion = Direccion("Nuevo", "11100", 4)
    var pepeGonzalez: Alumno = Alumno("12345678Z", "Pepe", 37, "Barbate", dir1)

    manager.transaction.begin()

    manager.persist(pepeGonzalez)

    manager.transaction.commit()


    manager.close()


}