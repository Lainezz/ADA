import org.w3c.dom.*
import tema1.Ejercicios.PruebaPractica.Pokemon
import java.io.File
import java.io.FileReader
import java.io.Reader
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.*
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

object GestXML {

    fun ejer1(user: String, pass: String): Boolean {

        var reader: Reader? = null
        try {
            val arch: File = File("resources/DocsParaPrueba/credentials.conf")
            reader = FileReader(arch)
            val prop: Properties = Properties()
            prop.load(reader)

            if (prop.getProperty("user") == user && prop.getProperty("password") == pass) {
                reader.close()
                return true
            } else {
                reader.close()
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            reader?.close()
            return false
        }
    }

    fun ejer2(pokABuscar: String): MutableMap<Number, Pokemon?> {

        //Creamos el objeto para la respuesta
        val response: MutableMap<Number, Pokemon?> = mutableMapOf<Number, Pokemon?>()

        //1º Abrimos el fichero XML
        val ficheroXML: File = File("resources/DocsParaPrueba/pokemon.xml")

        //2º Creamos el arbol DOM
        val nodoPadre: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ficheroXML)

        //3º Vamos a buscar el pokemon dentro de nuestro XML
        val pokemons: NodeList = nodoPadre.getElementsByTagName("pokemon")
        //4º Iteramos sobre todos los nodos
        for (i in 0..pokemons.length - 1) {
            //Cada item corresponde con todos los pokemons encontrados en el getElementsByTagname
            val pokemon: Node = pokemons.item(i)

            buscarConRecursividad(pokemon, pokABuscar, response)
        }

        if (response.get(200) == null) response.put(404, null)

        return response
    }

    private fun buscarConRecursividad(nodo: Node, pokABuscar: String, response: MutableMap<Number, Pokemon?>) {
        if (nodo.nodeType == Node.ELEMENT_NODE) {
            val nodosHijos: NodeList = nodo.childNodes
            for (i in 0..nodosHijos.length - 1) {
                buscarConRecursividad(nodosHijos.item(i), pokABuscar, response)
            }
        }
        //Compruebo que el nodo que se está comprobando es el de species, si no
        if (nodo.nodeType == Node.TEXT_NODE
            && nodo.parentNode.nodeName.trim().lowercase() == "species"
            && (nodo.textContent.trim().isNotEmpty() || nodo.textContent.trim().isNotBlank())
        ) {

            val contenidoNodo: String = nodo.textContent.trim().lowercase()
            if (contenidoNodo == pokABuscar.lowercase()) {
                println("Pokemon encontrado: $pokABuscar")

                //Rellena el objeto Pokemon desde el nodo donde sabemos que está el comic
                val pokemon: Pokemon = getDatosPokemon(nodo)

                //Inicializo el mapa con la información a devolver
                response.put(200, pokemon)
            }
        }

    }

    private fun getDatosPokemon(nodo: Node): Pokemon {

        val nombre: String = nodo.textContent.trim()

        val elemPadre: Element = nodo.parentNode.parentNode as Element

        //TIPO
        val type: String = elemPadre.getElementsByTagName("types").item(0).textContent

        //HABILIDADES
        val pokAbilities: MutableList<String> = mutableListOf<String>()
        val abilities: NodeList = elemPadre.getElementsByTagName("abilities")
        for (i in 0..abilities.length - 1) {

            val ability = abilities.item(i)
            if (ability.nodeType == Node.ELEMENT_NODE && ability.nodeName == "ability" || ability.nodeName == "dream") {
                pokAbilities.add(ability.textContent)
            }
        }

        //BASE STATS
        val pokStats: MutableMap<String, String> = mutableMapOf<String, String>()
        val stats: NodeList = elemPadre.getElementsByTagName("baseStats")
        for (i in 0..stats.length - 1) {

            val stat = stats.item(i)
            if (stat.nodeType == Node.ELEMENT_NODE && stat.nodeName == "HP") {
                pokStats.put(stat.nodeName, nodo.textContent)
            } else if (stat.nodeType == Node.ELEMENT_NODE && stat.nodeName == "ATK") {
                pokStats.put(stat.nodeName, nodo.textContent)
            } else if (stat.nodeType == Node.ELEMENT_NODE && stat.nodeName == "DEF") {
                pokStats.put(stat.nodeName, nodo.textContent)
            } else if (stat.nodeType == Node.ELEMENT_NODE && stat.nodeName == "SPD") {
                pokStats.put(stat.nodeName, nodo.textContent)
            } else if (stat.nodeType == Node.ELEMENT_NODE && stat.nodeName == "SATK") {
                pokStats.put(stat.nodeName, nodo.textContent)
            } else if (stat.nodeType == Node.ELEMENT_NODE && stat.nodeName == "SDEF") {
                pokStats.put(stat.nodeName, nodo.textContent)
            }
        }

        val pok: Pokemon = Pokemon(nombre, type, pokAbilities, pokStats)
        return pok
    }

    fun ejer3(pok: Pokemon) {

        //1º Instanciamos la clase DocumentBuilderFactory. También instanciamos/llamamos a los métodos necesarios
        //para poder controlar todos los métodos del dom
        val factory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        val builder: DocumentBuilder = factory.newDocumentBuilder()
        val imp: DOMImplementation = builder.domImplementation


        //2º Una vez tenemos la implementacion DOM, procedemos a crear un Document.
        //Recordamos que Document es el nodo padre, el primer nodo de todos.
        //Por ahora, ni namespaceURI ni doctype nos interesan
        val document: Document = imp.createDocument(null, pok.name, null)

        //En este punto ya tendríamos el primer nodo creado. El nodo padre.
        //3º Ahora sólo tenemos que empezar a "meter" hijos a ese nodo padre

        //Inicio tag nombre
        val name: Element = document.createElement("name")
        val textoNombre: Text = document.createTextNode(pok.name)
        name.appendChild(textoNombre)
        //Fin tag nombre

        //Inicio tag tipo
        val tipo: Element = document.createElement("type")
        val textoTipo: Text = document.createTextNode(pok.tipo)
        tipo.appendChild(textoTipo)
        //Fin tag tipo

        //Inicio tag habilidades
        val abilities: Element = document.createElement("abilities")
        pok.abilities.forEach { ability ->
            val habilidadElem: Element = document.createElement("ability")
            val habilidadText: Text = document.createTextNode(ability)
            habilidadElem.appendChild(habilidadText)
            abilities.appendChild(habilidadElem)
        }
        //Fin tag habilidades

        //Inicio tag BASESTATS
        val baseStats: Element = document.createElement("baseStats")
        pok.baseStats.forEach { stat ->
            val statElem: Element = document.createElement(stat.key)
            val statText: Text = document.createTextNode(stat.value)
            statElem.appendChild(statText)
            baseStats.appendChild(statElem)
        }
        //Fin tag BASESTATS

        document.documentElement.appendChild(name)
        document.documentElement.appendChild(tipo)
        document.documentElement.appendChild(abilities)
        document.documentElement.appendChild(baseStats)

        //4º Finalmente, sólo queda escribir el fichero XML
        //Creamos la fuente XML a partir de nuestro nodo padre document
        //-> ¿Qué queremos escribir?
        val source: Source = DOMSource(document)
        //Creamos un "flujo" de escritura. Digo flujo para que se entienda.
        //-> ¿Dónde queremos escribir? ¿Cómo escribimos?
        val result: Result = StreamResult(File("resources/DocsParaPrueba/${pok.name}.xml"))
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

}