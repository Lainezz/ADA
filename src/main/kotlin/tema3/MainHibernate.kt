import jakarta.persistence.*
import org.hibernate.SessionFactory
import tema3.Persona

fun main() {

    /*
    Creamos el gestor de persistencia
     */
    val emf: EntityManagerFactory = Persistence.createEntityManagerFactory("Persistencia")
    val manager: EntityManager = emf.createEntityManager()

    var listaAlumnos: List<Persona> = manager.createQuery("FROM Persona").resultList as List<Persona>
    listaAlumnos.forEach {
        println(it.nombre)
        println(it.ciudad)
    }

    //Objeto transient, es decir, aún no ligado a la base de datos
    val alumnoNuevo: Persona = Persona("35673262H", "Antonio", 29, "Garrucha")

    manager.transaction.begin()

    manager.persist(alumnoNuevo)

    manager.transaction.commit()

    listaAlumnos = manager.createQuery("FROM Persona").resultList as List<Persona>
    listaAlumnos.forEach {
        println(it.nombre)
        println(it.ciudad)
    }

}