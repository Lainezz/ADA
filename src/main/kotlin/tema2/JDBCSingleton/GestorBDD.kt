package tema2.JDBCSingleton

import tema2.JDBCSingleton.Sentencias.SQLSentences2
import tema2.JDBCSingleton.Clases.Asignatura
import tema2.JDBCSingleton.Clases.Persona
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.Savepoint

class GestorBDD private constructor() {

    companion object {
        @Volatile
        private var instance: GestorBDD? = null
        fun getInstance(): GestorBDD {
            if (instance == null) {
                instance = GestorBDD()
            }
            return instance!!
        }
    }

    private val url: String = "jdbc:mysql://localhost/"
    private val bd: String = "bddeprueba"
    private val user: String = "root"
    private val pass: String = ""

    //La conexion también debe ser privada
    @Volatile
    private var conn: Connection? = null

    fun conectarBDD() {

        try {
            if (conn == null) {
                println("[Conectado]")
                conn = DriverManager.getConnection(url + bd, user, pass)
            } else {
                println("[Conexión ya existente]")
            }
        } catch (e: Exception){
            e.printStackTrace()
            println("[Error al conectarse]")
            if (conn!=null) desconexion()
        }

    }

    fun desconexion() {
        if (conn != null) {
            conn!!.close()
            println("[Desconectado]")
        } else {
            println("[No se puede desconectar]")
        }
    }

    fun altaUsuario(alumn: Persona, listaAsig: List<Asignatura>): Boolean {

        if (conn != null || !conn!!.isClosed) {
            try {
                //EStablecemos que hacemos commit nosotros "manualmente"
                conn!!.autoCommit = false

                val psAltaAlumno: PreparedStatement = conn!!.prepareStatement(SQLSentences2.insertEstudiante)
                psAltaAlumno.setString(1, alumn.dni)
                psAltaAlumno.setString(2, alumn.nombre)
                psAltaAlumno.setInt(3, alumn.edad)
                psAltaAlumno.setString(4, alumn.ciudad)

                psAltaAlumno.executeUpdate()



                val psAltaAsignaturas: PreparedStatement = conn!!.prepareStatement(SQLSentences2.insertAsignatura)
                var savePoint: Savepoint? = null
                for (asignatura in listaAsig) {
                    try {
                        psAltaAsignaturas.setInt(1, asignatura.codigo)
                        psAltaAsignaturas.setString(2, alumn.dni)
                        psAltaAsignaturas.setString(3, asignatura.titulo)
                        psAltaAsignaturas.setString(4, asignatura.familia)

                        psAltaAsignaturas.executeUpdate()

                        savePoint = conn!!.setSavepoint("EnAsignaturas")
                    } catch (e: Exception){
                        conn!!.rollback(savePoint)
                    }
                }

                conn!!.commit()
                return true

            } catch (e: Exception) {
                conn!!.rollback()
                println("[Error alta alumno]")
            }
        }
        return false
    }
}