import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

fun main() {

    /*
    VAMOS A LEER CON RECURSIVIDAD TODOS LOS ELEMENTOS DE UN XML
     */
    //0º Abrir el fichero
    val ficheroXML: File = File("resources/productos.xml")

    //1º Abrimos el fichero XML (CREAR EL ARBOL DOM)
    val productosDocument: Document =
        DocumentBuilderFactory.newInstance().
        newDocumentBuilder().parse(ficheroXML)

    productosDocument.documentElement.normalize()

    //2º Ahora, iteramos sobre todos los elementos
    val nodoPadre: Node = productosDocument as Node
    //Me devuelve un nodeList
    val nodosHijos: NodeList = nodoPadre.childNodes

    val mapaDatos: MutableMap<String, String> =
        mutableMapOf<String, String>()

    //Estamos donde siempre hemos estado
    for(i in 0..nodosHijos.length-1){
        val producto: Node = nodosHijos.item(i)

        buscarConRecursividadFun(producto)
    }
}

fun buscarConRecursividadFun(producto: Node){
    if(producto.nodeType == Node.ELEMENT_NODE){
        val productos: NodeList = producto.childNodes
        for(i in 0..productos.length-1){
            buscarConRecursividadFun(productos.item(i))
        }
    }
    if(producto.nodeType == Node.TEXT_NODE && producto.textContent.trim().length != 0){

        println(producto.parentNode.nodeName+": "+producto.textContent)
    }
}