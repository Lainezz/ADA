package tema2.EjercicioClase.Vista

//Esta clase correspondería a los archivos .xml de un proyecto Android
//Como por ejemplo el MainActivity.xml
class AppVista {

    fun mainMenu(): String{
        println("Bienvenido a la aplicación. Seleccione una opción")
        println("1. Login")
        println("2. Alta User")
        println("0. Salir")
        val opc: String = readln();
        return opc;
    }

    fun login(): String{
        println("user: (dni del usuario)")
        var opc: String = readln()
        return opc.trim().lowercase()
    }

    fun dataStudent(): String{
        var dataStudent: String = ""

        println("**Introduzca los datos del estudiante**")
        print("DNI(sin espacios y con la letra en mayus): ")
        dataStudent = readln().trim()
        dataStudent += ";"
        print("Nombre: ")
        dataStudent += readln().trim()
        dataStudent += ";"
        print("Edad: ")
        dataStudent += readln().trim()
        dataStudent += ";"
        print("Ciudad")
        dataStudent += readln().trim()

        return dataStudent
    }

    fun dataSubject(): String{
        var dataSubject: String = ""

        println("**Introduzca los datos de la asignatura**")
        print("Codigo(sin espacios): ")
        dataSubject = readln().trim()
        dataSubject += ";"
        print("Titulo: ")
        dataSubject += readln().trim()
        dataSubject += ";"
        print("Familia: ")
        dataSubject += readln().trim()

        return dataSubject
    }

    fun exit(){
        println("Hasta pronto")
    }

    fun error(msg: String) {
        println("****ERROR****")
        println(msg)
        println("*************")
    }

    fun success(msg: String) {
        println("*************")
        println(msg)
        println("*************")
    }

    fun altaSuccess() {
        println(">>>Alta alumno correcta.")
    }

    fun altaError(){
        println(">>>Error durante transaccion. RollBack aplicado")
    }

}