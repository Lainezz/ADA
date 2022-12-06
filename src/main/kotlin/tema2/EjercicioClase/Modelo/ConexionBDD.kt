package tema2.EjercicioClase.Modelo

import Sentencias
import tema2.EjercicioClase.Modelo.Clases.Asignatura
import tema2.EjercicioClase.Modelo.Clases.Persona
import tema2.JDBCSingleton.Sentencias.SQLSentences2
import java.sql.*

class ConexionBDD private constructor() {
    /*
    Para implementar el patron creacional Singleton, en kotlin tenemos dos aproximaciones.
    "Las más sencillas"

    La primera, la más parecida a la aproximación en Java, sería declarar el constructor
    por defecto de la clase como privado y dentro de la clase declarar un companion object
    que se encargara de inicializar la clase

    La otra aproximación, consistiría aprovecharnos que Kotlin tiene una manera de aplicar
    este patrón creacional de forma nativa.
    Para ello, simplemente deberíamos declarar la clase... como un objeto. Sólo eso.
    De esta manera, este objeto se crea una única vez, y todos los intentos
    de creación de un objeto nuevo, serían en realidad llamadas al mismo objeto una
    y otra vez.


    https://dev4phones.wordpress.com/2020/05/01/como-crear-una-clase-singleton-en-kotlin-para-android/
     */
    //1a Aproximación
    companion object {
        @Volatile
        private var instance: ConexionBDD? = null

        fun getInstance(): ConexionBDD {
            if (instance == null) {
                println("[Instancia de clase no existente, se procede a crearla]")
                instance = ConexionBDD()
            } else {
                println("[Instancia ya existente]")
            }
            return instance!!
        }
    }

    //Declaración de variables globales PRIVADAS
    private val url: String = "jdbc:mysql://localhost/"
    private val bd: String = "bddeprueba"
    private val user: String = "root"
    private val pass: String = ""

    //La conexion también debe ser privada
    private var conn: Connection? = null

    /**
     * Funcion para realizar una conexion a la base de datos
     */
    fun conectarBDD() {
        //Si la conexión no está hecha, se hace.
        if (conn == null) {
            Class.forName("com.mysql.cj.jdbc.Driver")
            conn = DriverManager.getConnection(url + bd, user, pass)
            println("[Conexion realizada correctamente]")
        } else {
            println("[Conexion ya existente]")
        }
    }

    /**
     * Funcion para desconectar la BDD
     */
    fun desconexion() {
        //Si existe una conexión, procedemos a cerrarla
        if (!conn?.isClosed!!) {
            conn!!.close()
            println("[Desconectado de la Base de Datos]")
        } else {
            println("[Conexion inexistente]")
        }
    }

    /**
     * Función para comprobar todos los alumnos de una tabla
     */
    fun selectAll() {
        if (conn != null) {
            val st: Statement = conn!!.createStatement()
            val rs: ResultSet = st.executeQuery(Sentencias.selectAll)
            while (rs.next()) {
                println(rs.getString(1))
                println(rs.getString(2))
                println(rs.getString(3))
                println(rs.getString(4))
            }
            st.close()
            rs.close()
        } else {
            println("[Base de Datos no conectada]")
        }
    }

    /**
     * Funcion para comprobar un usuario en concreto
     */
    fun checkUser(dni: String): Boolean {
        if (conn != null) {
            val ps: PreparedStatement = conn!!.prepareStatement(SQLSentences2.selectUser)
            ps.setString(1, dni)
            val rs: ResultSet = ps.executeQuery()
            while (rs.next()) {
                return (rs.getString(1).isNotBlank())
            }
        } else {
            println("[Base de Datos no conectada]")
        }
        return false
    }

    /**
     * Imaginamos que tenemos que actualizar la tabla alumnos y la tabla asignaturas
     */
    fun altaAlumno(alumn: Persona, listaAsig: List<Asignatura>): Int {

        if (conn != null && !conn?.isClosed!!) {
            try {

                //Comprobamos primero si el estudiante está dado de alta ya
                if (checkUser(alumn.dni)) return 2

                var todoBien: Boolean = true

                //Con esto establecemos que el commit lo vamos a controlar nosotros
                conn!!.autoCommit = false

                //Insertamos al estudiante
                val nFilasAlumn = insertPersona(alumn)
                if (nFilasAlumn < 1) todoBien = false

                //INSERTAMOS LAS ASIGNATURAS DEL ESTUDIANTE
                for (asig in listaAsig) {
                    //Insertamos la asignatura llamando a un método propio
                    val nFilasAsig = insertAsignatura(asig, alumn.dni)
                    //Comprobamos si ha ido bien. Si no es así, ponemos el flag a false
                    if (nFilasAsig < 1) todoBien = false
                }

                //Comprobamos nuestro flag para hacer un commit o hacer un rollback
                if (todoBien) {
                    conn!!.commit()
                    return 0
                } else {
                    conn!!.rollback()
                    return 1
                }
            } catch (e: SQLIntegrityConstraintViolationException) {
                conn!!.rollback()
                e.printStackTrace()
                return 1
            } catch (e: SQLException) {
                conn!!.rollback()
                e.printStackTrace()
                return 1
            }
        } else {
            return 1
        }
    }

    private fun insertPersona(persona: Persona): Int {
        //INSERTAMOS AL ESTUDIANTE
        val psAlumn: PreparedStatement = conn!!.prepareStatement(SQLSentences2.insertEstudiante)

        psAlumn.setString(1, persona.dni)
        psAlumn.setString(2, persona.nombre)
        psAlumn.setInt(3, persona.edad)
        psAlumn.setString(4, persona.ciudad)

        val nFilas =  psAlumn.executeUpdate()

        psAlumn.close()

        return nFilas
    }

    private fun insertAsignatura(asig: Asignatura, dni: String): Int {
        //INSERTAMOS LAS ASIGNATURAS DEL ESTUDIANTE
        val psAsig: PreparedStatement = conn!!.prepareStatement(SQLSentences2.insertAsignatura)

        psAsig.setInt(1, asig.codigo)
        psAsig.setString(2, dni)
        psAsig.setString(3, asig.titulo)
        psAsig.setString(4, asig.familia)

        val nFilasAsig = psAsig.executeUpdate()

        psAsig.close()

        return nFilasAsig
    }
}