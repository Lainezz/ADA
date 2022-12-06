import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import tema1.Ejercicios.Ejercicio4.Comic
import tema1.Ejercicios.Ejercicio4.Personaje
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

object Buscar {

    val pathXMLComics: String = "resources/marvel/comicsMarvelTODOS.xml"
    val pathXMLPjs: String = "resources/marvel/personajesMarvelTODOS.xml"

    fun buscarComic(idComic: String) {
        println("***Buscando Comic***")

        var response: MutableMap<Number, Comic> = mutableMapOf<Number, Comic>()

        val fichXML: File = File(pathXMLComics)
        val document: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fichXML)
        document.documentElement.normalize()

        val results: NodeList = document.getElementsByTagName("results")

        for (i in 0..results.length-1){
            buscarComicRecursividad(results.item(i), idComic, response)
        }

    }

    private fun buscarComicRecursividad(nodo: Node, idComic: String, response: MutableMap<Number, Comic>) {
        if (nodo.nodeType == Node.ELEMENT_NODE){
            val nodosHijos: NodeList = nodo.childNodes
            for (i in 0..nodosHijos.length-1){
                buscarComicRecursividad(nodosHijos.item(i), idComic, response)
            }
        }

        if(nodo.nodeType == Node.TEXT_NODE &&  nodo.textContent.trim().isNotEmpty() || nodo.textContent.trim().isNotBlank()){
            val contenidoNodo: String = nodo.textContent.trim()
            if(contenidoNodo == idComic){
                println("Comic encontrado: $idComic")

                //Rellena el objeto Comic desde el nodo donde sabemos que está el comic
                val comic: Comic = getDatosComic(nodo)

                //Inicializo el mapa con la información a devolver
                response.put(200, comic)
            }
        }
    }

    private fun getDatosComic(nodo: Node) : Comic{

        val id: String = nodo.textContent
        val nodoResult: Element = nodo.parentNode.parentNode as Element

        val titulo: String = nodoResult.getElementsByTagName("title").item(0).textContent
        val descr: String = nodoResult.getElementsByTagName("description").item(0).textContent

        val comic: Comic = Comic(id, titulo, descr)
        return comic
    }

    fun buscarPj(namePj: String): MutableMap<Number, Personaje> {
        println("***Buscando PJ****")
        var response: MutableMap<Number, Personaje> = mutableMapOf<Number, Personaje>()

        val fichXML: File = File(pathXMLPjs)
        val document: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fichXML)
        document.documentElement.normalize()

        val results: NodeList = document.getElementsByTagName("results")
        for (i in 0..results.length-1){
            buscarPjRecursividad(results.item(i), namePj, response)
        }

        //Si comprobamos que en respuesta no hay un codigo 200... entonces es que no lo ha encontrado
        if(response.get(200) == null){
            response.put(404, Personaje("", "", "", null))
        }

        return response
    }

    private fun buscarPjRecursividad(nodo: Node, namePj: String, response: MutableMap<Number, Personaje>) {
        if (nodo.nodeType == Node.ELEMENT_NODE){
            val nodosHijos: NodeList = nodo.childNodes
            for (i in 0..nodosHijos.length-1){
                buscarPjRecursividad(nodosHijos.item(i), namePj, response)
            }
        }

        if(nodo.nodeType == Node.TEXT_NODE &&  nodo.textContent.trim().isNotEmpty() || nodo.textContent.trim().isNotBlank()){
            val contenidoNodo: String = nodo.textContent.trim()
            if(contenidoNodo == namePj){
                println("personaje encontrado: ${namePj}")

                //Rellena el objeto personaje desde el nodo donde sabemos que está el pj
                val pj: Personaje = getDatosPj(nodo)

                //Inicializo el mapa con la información a devolver
                response.put(200, pj)
            }
        }
    }

    private fun getDatosPj(nodo: Node) : Personaje{

        val nombre: String = nodo.textContent
        val nodoResult: Element = nodo.parentNode.parentNode as Element

        val id: String = nodoResult.getElementsByTagName("id").item(0).textContent
        val descr: String = nodoResult.getElementsByTagName("description").item(0).textContent

        val pj: Personaje = Personaje(id, nombre, descr, null)
        return pj
    }

}