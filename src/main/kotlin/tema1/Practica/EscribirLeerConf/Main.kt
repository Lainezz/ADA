import tema1.Practica.EscribirLeerConf.PropiedadesUsuario
import java.io.FileReader
import java.io.FileWriter
import java.io.Reader
import java.io.Writer
import java.util.*

fun main(args: Array<String>) {
    /*
    ESCRIBIR UN ARCHIVO .CONF
     */

    //Grupo de funciones diferentes
    val writer: Writer = FileWriter(
        System.getProperty("user.dir") +
                System.getProperty("file.separator") +
                "resources" + System.getProperty("file.separator") +
                "config.conf");

    val propEscribir = Properties()

    //Instancio un objeto de la clase PropiedadesUsuario
    val propUsuario: PropiedadesUsuario = PropiedadesUsuario("diego", "1232443", "80", "localhost")

    propEscribir.setProperty("user", propUsuario.user)
    propEscribir.setProperty("password",propUsuario.password)
    propEscribir.setProperty("server",propUsuario.server)
    propEscribir.setProperty("port",propUsuario.port)

    propEscribir.store(writer, "Archivo de configuracion")

    println("***Archivo de configuracion creado y escrito***")


    /*
    LEER ARCHIVOS DE CONFIGURACION
     */
    //Sabemos que los archivos de configuracion siguen un
    // formato más o menos estricto clave valor
    //La API que usamos para "manejar" esos archivos de conf
    //es Properties()
    val prop = Properties()

    val reader: Reader = FileReader(
        System.getProperty("user.dir") +
                System.getProperty("file.separator") +
                "resources" + System.getProperty("file.separator") +
                "config.conf"
    )
    //Para leer archivos de configuracion usamos el método load()
    prop.load(reader)
    println("***Archivo de configuracion cargado***")

    //Si quiero listar todas las keys de mi archivo de configuracion
    //PropertyNames me devuelve un Enum
    val todasKeys = prop.propertyNames();

    //El enum lo "casteo" a lista
    val todasKeysAsList = todasKeys.toList();

    for (elemento in todasKeysAsList){
        println(elemento)
    }

    println("Contraseña: "+ prop.getProperty("password"))
    println("Usuario: "+ prop.getProperty("user"))

    reader.close()
    writer.close()
}