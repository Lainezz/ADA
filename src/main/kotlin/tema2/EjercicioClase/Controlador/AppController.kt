import tema2.EjercicioClase.Modelo.Clases.Asignatura
import tema2.EjercicioClase.Modelo.Clases.Persona
import tema2.EjercicioClase.Modelo.ConexionBDD
import tema2.EjercicioClase.Vista.AppVista

class AppController(private val vista: AppVista) {

    fun onStart(): Int{
        var opc: String = vista.mainMenu()
        return opc.toInt()
    }

    fun onLogin() {
        val modelo: ConexionBDD = ConexionBDD.getInstance()
        modelo.conectarBDD()

        val dni: String = vista.login()
        val loginCode: Boolean = modelo.checkUser(dni)
        if(loginCode) vista.success("Bienvenid@") else vista.error("Usuario no reconocido")

    }

    fun onAlta(){

        //Conectamos con la BDD
        val modelo: ConexionBDD = ConexionBDD.getInstance()
        modelo.conectarBDD()

        //Objetos estudiante y asignatura
        var estudiante: Persona?
        var asignatura: Asignatura?
        var listaAsignaturas: MutableList<Asignatura> = mutableListOf()

        //Obtenemos el estudiante
        estudiante = pedirDatosStudent()
        if(estudiante == null){
            vista.error("Error datos estudiante")
            return
        }

        //Introducimos la asignatura del estudiante
        asignatura = pedirDatosSubject()
        if(asignatura == null){
            vista.error("Error datos asignatura")
            return
        }

        val codigo = modelo.altaAlumno(estudiante, listaAsignaturas.toList())
        when(codigo){
            0 -> vista.success("Usuario dado de alta")
            1 -> vista.error("Error al dar de alta")
            2 -> vista.error("Usuario ya existente")
        }
    }

    fun onExit() {
        val modelo: ConexionBDD = ConexionBDD.getInstance()
        modelo.desconexion()
        vista.exit()
    }

    private fun pedirDatosStudent() : Persona? {
        //Obtenemos los datos del estudiante
        val datosEstudiante = vista.dataStudent()
        val arrEstudiante = datosEstudiante.split(";")
        if(arrEstudiante.size == 4){
            return Persona(arrEstudiante[0], arrEstudiante[1], arrEstudiante[2].toInt(), arrEstudiante[3])
        } else {
            return null
        }
    }

    private  fun pedirDatosSubject(): Asignatura? {
        //Obtenemos los datos de la asignatura
        val datosAsign = vista.dataSubject()
        val arrAsign = datosAsign.split(";")
        if(arrAsign.size == 3){
            return Asignatura(arrAsign[0].toInt(), arrAsign[1], arrAsign[2])
        } else {
            return null
        }
    }

}