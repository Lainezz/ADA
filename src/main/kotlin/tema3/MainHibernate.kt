import jakarta.persistence.*
import tema3.Persona

var manager: EntityManager? = null
var emf: EntityManagerFactory? = null

fun main() {

    /*
    Creamos el gestor de persistencia
     */
    emf = Persistence.createEntityManagerFactory("Persistencia")
    manager = emf?.createEntityManager()

    var listaAlumnos: List<Persona> = manager?.createQuery("FROM Persona")?.resultList as List<Persona>
    println(listaAlumnos)

}