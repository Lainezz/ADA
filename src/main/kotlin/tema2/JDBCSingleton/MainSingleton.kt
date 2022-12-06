import tema2.JDBCSingleton.GestorBDD

fun main() {


    val conexion1: GestorBDD = GestorBDD.getInstance()

    conexion1.conectarBDD()


}