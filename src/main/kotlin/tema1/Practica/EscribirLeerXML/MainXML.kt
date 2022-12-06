import org.w3c.dom.*
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

fun main() {

    /*
   LEER FICHEROS XML
    */

    /*
    Los pasos a seguir son "más o menos" iguales a los que hemos visto antes
     */
    //1º Abrir el archivo
    val ficheroXML: File = File("resources/productos.xml")

    //2º Comprobamos su correcta apertura
    if (ficheroXML.exists() || ficheroXML.createNewFile()) {
        println("Fichero creado correctamente: ${ficheroXML.name}")
    }

    //3º Ahora tenemos que conseguir... parsear ese documento y crear
    //el ARBOL DOM:
    val nodoPadre: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ficheroXML)

    //4º Ahora vienen las lágrimas
    val productos: NodeList = nodoPadre.getElementsByTagName("producto")
    val mMap: MutableMap<String, String> = mutableMapOf<String, String>()

    for (i in 0..productos.length - 1) {
        println(productos.item(i))
        if (productos.item(i).nodeType == Node.ELEMENT_NODE) {
            val producto: Element = productos.item(i) as Element

            val itemsProducto: NodeList = producto.childNodes

            for (j in 0..itemsProducto.length - 1) {

                if (itemsProducto.item(j).nodeType == Node.ELEMENT_NODE) {
                    val itemProducto: Element = itemsProducto.item(j) as Element

                    mMap.put(itemProducto.nodeName, itemProducto.nodeValue)

                    val textos: NodeList = itemProducto.childNodes

                    for (k in 0..textos.length - 1) {
                        if (textos.item(k).nodeType == Node.TEXT_NODE) {
                            val texto: Text = textos.item(k) as Text
                            println(texto)
                        }
                    }
                }
            }
        }
    }
}