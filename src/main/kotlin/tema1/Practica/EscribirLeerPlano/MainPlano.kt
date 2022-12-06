import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

fun main() {

    /*
    ***************************
    LEER ARCHIVO DE TEXTO PLANO
    ***************************
     */
    println("****LEER UN ARCHIVO DE TEXTO PLANO*****")

    //1º Abrimos el fichero donde queramos escribir/leer
    val ficheroLectura: File = File("resources/pruebaLectura.txt")

    //2º Comprobamos si el fichero se ha abierto correctamente
    if (ficheroLectura.createNewFile() || ficheroLectura.exists() ) {
        println("Fichero creado: ${ficheroLectura.name}")
    } else {
        println("Fichero ya existente: ${ficheroLectura.name}")
    }

    println("\r\n")

    //3º Creamos un flujo de lectura / escritura
    //Queremos usar la clase BufferedReader. Para ello, necesitamos pasarle al constructor
    //un objeto de tipo Reader. Un FileReader nos vale
    //https://docs.oracle.com/javase/7/docs/api/java/io/BufferedReader.html
    val fl: FileReader = FileReader(ficheroLectura)
    val br: BufferedReader = BufferedReader(fl)
    //4º Para poder leer todas las lineas del documento hay varias maneras
    //a. Usando readLines (te devuelve una lista de Strings)
    val lineas: List<String> = br.readLines()
    for (linea in lineas) {
        println("ln rl> ${linea}")
    }

    println("\r\n")

    //3º Creamos un flujo de lectura / escritura
    val fl2: FileReader = FileReader(ficheroLectura)
    val br2: BufferedReader = BufferedReader(fl2)
    //4º Para poder leer todas las lineas del documento hay varias maneras
    //b. Usando use (te devuelve un String con tod.o el texto)
    val texto: String = br2.readText()
    println("**Use ReadText**")
    println(texto)

    println("\r\n")

    //3º Creamos un flujo de lectura / escritura
    val fl3: FileReader = FileReader(ficheroLectura)
    val br3: BufferedReader = BufferedReader(fl3)
    //4º Para poder leer todas las lineas del documento hay varias maneras
    //c. Usando forEachLine
    br3.forEachLine { linea ->
        println("ln fe> ${linea}")
    }


    println("\r\n")

    //3º Creamos un flujo de lectura / escritura
    val fl4: FileReader = FileReader(ficheroLectura)
    val br4: BufferedReader = BufferedReader(fl4)
    //4º Para poder leer todas las lineas del documento hay varias maneras
    br4.useLines { lineas ->
        (
                lineas.forEach { linea ->
                    println("ln map> ${linea}")
                })

    }

    /*
    ***************************
    ESCRIBIR ARCHIVO DE TEXTO PLANO
    ***************************
     */
    //1º Abrimos el fichero donde quedamos escribir/leer
    val ficheroEscritura: File = File("resources/pruebaEscritura.txt")

    //2º Comprobamos si el fichero se ha abierto correctamente
    if ( ficheroEscritura.createNewFile() || ficheroEscritura.exists() ) {
        println("Fichero creado: ${ficheroEscritura.name}")
    } else {
        println("Fichero ya existente: ${ficheroEscritura.name}")
    }

    //3º Crear flujo de escritura
    //PrintWriter es una clase recomendada para realizar escrituras
    //Al constructor de PrintWriter le entra un File y un charset
    //https://docs.oracle.com/javase/7/docs/api/java/io/PrintWriter.html
    val pw: PrintWriter = PrintWriter(ficheroEscritura, Charsets.UTF_8)

    //4º Escribimos en el fichero con el flujo de escritura creado
    pw.write("Prueba de escritura\r\n")
    pw.write("Línea 2 del documento\r\n")
    pw.write("Línea 3 del documento ñ\r\n")
    pw.write("Última línea")

    //5º Cerramos el flujo de escritura
    pw.close()

}