import org.w3c.dom.DOMImplementation
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Text
import java.io.File
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.*
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

fun main() {
    /*
    En esta ocasión, vamos a escribir un documento XML.
     */

    //1º Instanciamos la clase DocumentBuilderFactory. También instanciamos/llamamos a los métodos necesarios
    //para poder controlar todos los métodos del dom
    val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
    val builder: DocumentBuilder = factory.newDocumentBuilder()
    val imp: DOMImplementation = builder.domImplementation

    //En este punto, ya tendríamos todos los métodos y atributos de la implementacion de java para DOM
    //(entre ellos el parse, por ejemplo (que lo usamos anteriormente))
    //Nota: se ha hecho en tres pasos, pero la idea es la misma que hemos estado usando hasta ahora, antes lo hacíamos
    //tod0 en la misma línea.

    //2º Una vez tenemos la implementacion DOM, procedemos a crear un Document.
    //Recordamos que Document es el nodo padre, el primer nodo de todos.
    //Por ahora, ni namespaceURI ni doctype nos interesan
    val document: Document = imp.createDocument(null, "productos", null)

    //En este punto ya tendríamos el primer nodo creado. El nodo padre.
    //3º Ahora sólo tenemos que empezar a "meter" hijos a ese nodo padre

    //Inicio tag producto1
    val producto1: Element = document.createElement("producto")

        //Inicio tag nombre
        val nombreProd1: Element = document.createElement("nombre")
        val textoNombre: Text = document.createTextNode("Cereales")
        nombreProd1.appendChild(textoNombre)
        //Fin tag nombre
        //Inicio tag precio
        val precioProd1: Element = document.createElement("precio")
        val textoPrecio: Text = document.createTextNode("3.45")
        precioProd1.appendChild(textoPrecio)
        //Fin tag precio

        producto1.appendChild(nombreProd1)
        producto1.appendChild(precioProd1)

    document.documentElement.appendChild(producto1)
    //Fin tag producto1

    //Inicio tag producto2
    val producto2: Element = document.createElement("producto")

    //Inicio tag nombre
    val nombreProd2: Element = document.createElement("nombre")
    val textoNombre2: Text = document.createTextNode("Colacao")
    nombreProd2.appendChild(textoNombre2)
    //Fin tag nombre
    //Inicio tag precio
    val precioProd2: Element = document.createElement("precio")
    val textoPrecio2: Text = document.createTextNode("1.45")
    precioProd2.appendChild(textoPrecio2)
    //Fin tag precio

    producto2.appendChild(nombreProd2)
    producto2.appendChild(precioProd2)

    document.documentElement.appendChild(producto2)
    //Fin tag producto2

    //... Añadimos todos los nodos que queramos, aplicando la dificultad y la profundidad que queramos

    //4º Finalmente, sólo queda escribir el fichero XML
    //Creamos la fuente XML a partir de nuestro nodo padre document
    //-> ¿Qué queremos escribir?
    val source: Source = DOMSource(document)
    //Creamos un "flujo" de escritura. Digo flujo para que se entienda.
    //-> ¿Dónde queremos escribir? ¿Cómo escribimos?
    val result: Result = StreamResult(File("resources/productosGenerados.xml"))
    //Obtenemos una instanciacion de la clase TransformerFactory
    // -> ¿Qué usamos para escribir?
    val transformer: Transformer = TransformerFactory.newInstance().newTransformer()

    //Thanks to J.M.
    //Para indentar automáticamente nuestro XML, para que salga bonito
    transformer.setOutputProperty(OutputKeys.INDENT, "yes")

    //Se realiza la "transformación" del documento a fichero
    // -> Realizamos la acción de escribir
    transformer.transform(source, result)

}