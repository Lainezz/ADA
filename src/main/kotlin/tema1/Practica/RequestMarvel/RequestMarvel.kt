package tema1.Practica.RequestMarvel

import java.io.File
import java.io.PrintWriter
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object RequestMarvel {

    private val ts: String = "1"
    private val publicKey: String = "57e587a01d7d84315096885dbd35e159"
    private val apiKey: String = "a59eb286a83d02b0f14f8a4b96d67e4a"

    /**
     * Funcion que pilla de la API de Marvel 2000 comics y los escribe en un documento json
     * @param offset Especifica desde qué número quieres empezar a pillar cómics
     * (debido a que la API establece un límite de 100, sólo se puede buscar de 100 en 100)
     */
    fun pillarComics(offset: Int) {
        var offsetObj: Int = offset
        var bodyResponse: StringBuilder = StringBuilder("")
        for (i in 0..20) {
            doRequest(offsetObj, bodyResponse)
            offsetObj += 100
        }
        writeDocument("comics", bodyResponse)
    }

    /**
     * Pilla 100 personajes de la API de Marvel y los escribe en un fichero .json
     * @param offset Especifica desde dónde se quiere buscar en la API
     * (debido a que la API establece un límite de 100, sólo se puede buscar de 100 en 100)
     */
    fun pillarPersonajes(offset: Int) {
        var offsetObj: Int = offset
        var bodyResponse: StringBuilder = StringBuilder("")
        doRequest(offsetObj, bodyResponse)
        writeDocument("personajes", bodyResponse)
    }

    private fun doRequest(offsetObj: Int, bodyResponse: java.lang.StringBuilder) {
        val client = HttpClient.newBuilder().build();

        val requestApi: String =
            "http://gateway.marvel.com/v1/public/comics?limit=100&offset=${offsetObj}&ts=$ts&apikey=$publicKey&hash=$apiKey"

        val request = HttpRequest.newBuilder()
            .uri(URI.create(requestApi))
            .version(HttpClient.Version.HTTP_1_1)
            .header("Content-Type", "application/json")
            .build();

        val response = client.send(request, HttpResponse.BodyHandlers.ofString());
        bodyResponse.append(response.body())
    }

    private fun writeDocument(name: String, bodyResponse: java.lang.StringBuilder) {
        //1º Abrimos el fichero donde quedamos escribir/leer
        val ficheroEscritura: File = File("resources/${name}Marvel.json")

        //2º Comprobamos si el fichero se ha abierto correctamente
        if (ficheroEscritura.createNewFile() || ficheroEscritura.exists()) {
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
        pw.write(bodyResponse.toString())

        //5º Cerramos el flujo de escritura
        pw.close()
    }

}