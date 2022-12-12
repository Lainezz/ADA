import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.Persistence
import tema3.Alumno

fun main() {
    /*
    Crear un SessionFactory (Que es lo mismo que un EntityManagerFactory).
    Inicializar la unidad de persistencia indicada en el persistence.xml.
    Con otras palabras, lo que haría sería coger toda la configuración con la Base de Datos que le
    has especificado en el archivo de configuración, y montar el "entorno" para poder realizar
    conexiones en sí. Es como que... "se prepara para conectarse". Establece la caché para almacenar
    objetos persistentes, establece el control de hilos, las sesiones abiertas, etc...
    SessionFactory -> Pertenece a Hibernate
    EntityManagerFactory -> Pertenece a JPA
    https://www.geeksforgeeks.org/hibernate-architecture/
     */
    val emf: EntityManagerFactory = Persistence.createEntityManagerFactory("PersistenciaAlumno")


    /*
    En este punto, por entendernos, lo que hacemos es "Conectarnos a la BDD"... crear la Session.
    Inicializamos un objeto de tipo Session (que es lo mismo que EntityManager)
    Session -> Pertenece a Hibernate
    EntityManager -> Pertenece a JPA
    https://stackoverflow.com/questions/16670447/difference-between-hibernate-session-and-entitymanager
     */
    val manager: EntityManager = emf.createEntityManager()

    var listaAlumnos = manager.createQuery("FROM Alumno").resultList as List<Alumno>
    listaAlumnos.forEach {
        println(it.nombre)
    }

    //En este punto ya estaríamos "conectados a la base de datos"
    val julia: Alumno = Alumno("76412387K", "Julia", 35, "Mojacar")
    //JULIA es un OBJETO TRANSIENT en este punto
    //QUIERE DECIR, que aún no está "ligado" o "linkeado" a la base de datos

    //Iniciamos una transacción
    manager.transaction.begin()

    manager.persist(julia) //AHORA... ES UN OBJETO PERSIST (persistido)

    julia.nombre="Julio" //Si realizamos cualquier cambio a este objeto, también se estaría cambiando en la Base de Datos

    manager.transaction.commit()

    println("***********")

    listaAlumnos = manager.createQuery("FROM Alumno").resultList as List<Alumno>
    listaAlumnos.forEach {
        println(it.nombre)
    }
}