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

    listarAlumnos()

    //En este punto ya estaríamos "conectados a la base de datos"
    var julio: Alumno = Alumno("76412387A", "Julio", 35, "Mojacar")
    //JULIO es un OBJETO TRANSIENT en este punto
    //QUIERE DECIR, que aún no está "ligado" o "linkeado" a la base de datos

    //Iniciamos una transacción
    manager.transaction.begin()
    manager.persist(julio) //AHORA... ES UN OBJETO PERSIST (persistido)
    julio.nombre = "Julia" //Si realizamos cualquier cambio a este objeto, también se estaría cambiando en la Base de Datos
    manager.transaction.commit()
    //Cambios commiteados a la BDD
    listarAlumnos()

    /*
    PARA BUSCAR UN ALUMNO. Usamos el método .find()
    .find() nos devuelve un objeto del tipo que le indiquemos, PERSISTED (o MANAGED).
    Cuando está persisted, ya sabemos que cualquier cambio que realicemos a ese objeto, se verá reflejado
    en la base de datos
     */
    var alumno: Alumno = manager.find(Alumno::class.java, "12345678Z")
    manager.transaction.begin()
    alumno.nombre = "Menganito"
    manager.transaction.commit()

    /*
    Para que esa entidad deje de ser PERSISTED (MANAGED), tenemos que "detachearla" de la base de datos.
    Cuando hacemos un .detach(), lo que hacemos es convertir esa entidad managed en un objeto normal y corriente
    de nuestra aplicacion
    Si hacemos cualquier cambio en ese objeto, vemos que no quedará reflejado (PERSISTIDO) en la BDD,
     */
    manager.transaction.begin()
    manager.detach(alumno) //Convierto a alumno, otra vez en Transient. En este punto, los cambios a alumno ya no afectarían en la BDD
    alumno.nombre = "XXXXXXXXXXX"
    manager.transaction.commit()

    listarAlumnos()

    /*
    ¿Qué pasa si ahora queremos volver a trabajar con ese alumno y que esté persisted?
    Es decir, qué pasa si hago cambios a ese alumno y quiero reflejar esos cambios en la BDD
    No puedo hacer un .persist() porque la entidad ya existe en la BDD y eso intenta insertar
    UTILIZO .merge().
     */
    manager.transaction.begin()
    alumno = manager.merge(alumno) //Esto nos devuelve el alumno de la BDD pero mantiene los cambios realizados en el objeto
    manager.transaction.commit()

    //Si vemos la BDD en este punto, veremos que el nombre del alumno es XXXXXXX

    /*
    Para eliminar registros de la base de datos, la manera más sencilla es hacerlo mediante el uso
    de .remove()
    IMPORTANTE, para eliminar registros de la BDD, lo que intentemos eliminar debe estar en la BDD (obviamente)
     */
    manager.transaction.begin()
    var julia: Alumno = Alumno("76412387A", "Julia", 35, "Mojacar") //Está transient
    julia = manager.merge(julia) //Primero tendríamos que convertir ese objeto transient a persisted
    manager.remove(julia) //Una vez persisted, ya podemos eliminarlo
    manager.transaction.commit()

    manager.transaction.begin()
    var diego: Alumno = manager.find(Alumno::class.java, "72716745B")
    manager.remove(diego)
    manager.transaction.commit()


    manager.close()
}

/**
 * Funcion para insertar un alumno en la base de datos
 */
fun insertAlumno(alumno: Alumno) {
    val emf: EntityManagerFactory = Persistence.createEntityManagerFactory("PersistenciaAlumno")
    val manager: EntityManager = emf.createEntityManager()

    try {
        manager.transaction.begin()
        manager.persist(alumno)
        manager.transaction.commit()
    } catch (e: Exception) {
        manager.transaction.rollback()
        println("Se ha producido un error, rollback aplicado")

    } finally {
        manager.close()
    }
}

fun buscarAlumno(dni: String) {
    val emf: EntityManagerFactory = Persistence.createEntityManagerFactory("PersistenciaAlumno")
    val manager: EntityManager = emf.createEntityManager()

    manager.transaction.begin()
    var alumno: Alumno = manager.find(Alumno::class.java, dni)

    println(alumno.nombre)
    manager.transaction.commit()
    manager.close()
}

fun listarAlumnos() {
    val emf: EntityManagerFactory = Persistence.createEntityManagerFactory("PersistenciaAlumno")
    val manager: EntityManager = emf.createEntityManager()

    println("*_____List Alumnos_______*")
    val listaAlumnos = manager.createQuery("FROM Alumno").resultList as List<Alumno>
    listaAlumnos.forEach {
        println("${it.nombre} con dni: ${it.dni}.")
    }
    println("*_____FIN_______*")

    manager.close()

}