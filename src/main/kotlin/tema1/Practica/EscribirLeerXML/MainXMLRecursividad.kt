import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

fun main() {

    /*
    NOS PIDEN HACER UNA FUNCIÓN QUE BUSQUE EN UN XML UNOS DATOS CONCRETOS DE UN PERSONAJE CONCRETO
    El personaje puede cambiar, los datos pueden cambiar
    No sabemos mucho del XML, sólo nos dicen que el nombre del personaje es un nodo del XML, y que toda la información
    está contenida dentro de esa etiqueta.
    **NOTA**
    Se podría complicar más si no existiera tal etiqueta, es decir, si el id del personaje
    estuviera en una etiqueta id, dentro de un elemento "personaje" por ejemplo
     */
    val reader = Scanner(System.`in`)
    var opc: Number = 1
    var opcInfo: Number = 1
    var personaje: String
    val mSet: MutableSet<String> = mutableSetOf<String>()
    var mMapResultado: MutableMap<String, String> = mutableMapOf<String, String>()

    println("¿Qué personaje quiere buscar?")
    personaje = readln()

    do {
        println("¿Qué información quiere obtener?")
        val info: String = readln()
        mSet.add(info)

        println("¿Quiere introducir más datos?")
        val opcInfo: Number = reader.nextInt()
    } while (opcInfo != 0)

    println(personaje)
    println(mSet)

    mMapResultado = buscarEnXML(personaje, mSet)

    println("Personaje buscado: "+personaje)
    println("Info: ")
    println(mMapResultado)

}

fun buscarEnXML(personaje: String, mSet: MutableSet<String>) : MutableMap<String, String> {
    val returnMap: MutableMap<String, String> = mutableMapOf<String, String>()

    //0º Sabemos que la info es de un personaje que nos dan, pues esa info ya la podemos meter en nuestro returnMap
    returnMap.put("personaje", personaje)

    //1º Abrimos el fichero XML
    val ficheroXML: File = File("resources/personajesLol.xml")

    //2º Creamos el arbol DOM
    val nodoPadre: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ficheroXML)

    //3º Vamos a buscar el personaje dentro de nuestro XML (sabemos que sólo hay uno... pero aún así tenemos que iterar)
    val personajes: NodeList = nodoPadre.getElementsByTagName(personaje)

    //4º Iteramos sobre todos los nodos
    for(i in 0..personajes.length-1){
        //Cada item corresponde con todos los personajes encontrados en el getElementsByTagname
        val personaje: Node = personajes.item(i)

        //5º Teniendo ya cada personaje, vamos a iterar sobre todos sus atributos para ENCONTRAR la info deseada
        //PERO DE FORMA RECURSIVA
        //(Adicionalmente) tenemos que iterar sobre nuestro set de informacion
        //donde tenemos toda la info que queremos encontrar
        mSet.forEach{info -> buscarConRecursividad(personaje, info, returnMap)}
    }



    //Devolvemos el resultado con toda la información pedida
    return returnMap
}

fun buscarConRecursividad(node: Node, infoABuscar: String ,returnMap: MutableMap<String, String>){

    println("BreakPoint")
    if(node.nodeName != infoABuscar){
        val nodoActual: NodeList = node.childNodes
        for(i in 0..nodoActual.length-1){
            buscarConRecursividad(nodoActual.item(i), infoABuscar, returnMap)
        }
    } else {
        val name: String = node.nodeName
        val detalle: String = node.textContent
        returnMap.put(name, detalle)
    }
}
