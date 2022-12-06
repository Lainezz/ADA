import tema1.Ejercicios.PruebaPractica.Pokemon

fun main() {

    //PRUEBAS PARA EJER 1
    if(GestXML.ejer1("diego", "1234567890"))  println("Bienvenid@") else println("Acceso denegado")
    if(GestXML.ejer1("diego", "123456789"))  println("Bienvenid@") else println("Acceso denegado")
    if(GestXML.ejer1("h4ck3r", "1234567890"))  println("Bienvenid@") else println("Acceso denegado")


    //PRUEBAS PARA EJER 2
    val prueba1: MutableMap<Number, Pokemon?> = GestXML.ejer2("BulbasauR")
    if(prueba1.get(200) == null) println("404, no encontrado") else println("200, OK")

    val prueba2: MutableMap<Number, Pokemon?> = GestXML.ejer2("GALVANTULA")
    if(prueba2.get(200) == null) println("404, no encontrado") else println("200, OK")

    val prueba3: MutableMap<Number, Pokemon?> = GestXML.ejer2("Charmeleon")
    if(prueba3.get(200) == null) println("404, no encontrado") else println("200, OK")

    val prueba4: MutableMap<Number, Pokemon?> = GestXML.ejer2("BLAZE")
    if(prueba4.get(200) == null) println("404, no encontrado") else println("200, OK")

    //PRUEBAS PARA EJER 3
    val nom: String = "Pikachu"
    val tipo: String = "Electrico"
    val abilities: MutableList<String> = mutableListOf<String>()
    abilities.add("static")
    abilities.add("lightning-rod")
    val baseStats: MutableMap<String, String> = mutableMapOf<String, String>()
    baseStats.put("HP","35")
    baseStats.put("ATT","40")
    baseStats.put("DF","40")
    baseStats.put("SPATT","50")
    baseStats.put("SPDF","50")
    baseStats.put("SPD","90")
    val pikachu: Pokemon = Pokemon(nom, tipo, abilities, baseStats)

    val nom2: String = "Evee"
    val tipo2: String = "Normal"
    val abilities2: MutableList<String> = mutableListOf<String>()
    abilities2.add("cuteness")
    abilities2.add("fluffyness")
    val baseStats2: MutableMap<String, String> = mutableMapOf<String, String>()
    baseStats2.put("HP","20")
    baseStats2.put("ATT","30")
    baseStats2.put("DF","30")
    baseStats2.put("SPATT","20")
    baseStats2.put("SPDF","60")
    baseStats2.put("SPD","100")
    val evee: Pokemon = Pokemon(nom2, tipo2, abilities2, baseStats2)

    val nom3: String = "Butterfree"
    val tipo3: String = "Bicho"
    val abilities3: MutableList<String> = mutableListOf<String>()
    abilities3.add("volar")
    abilities3.add("ser cute")
    val baseStats3: MutableMap<String, String> = mutableMapOf<String, String>()
    baseStats3.put("HP","45")
    baseStats3.put("ATT","50")
    baseStats3.put("DF","60")
    baseStats3.put("SPATT","60")
    baseStats3.put("SPDF","50")
    baseStats3.put("SPD","120")
    val butterfree: Pokemon = Pokemon(nom3, tipo3, abilities3, baseStats3)

    GestXML.ejer3(pikachu)
    GestXML.ejer3(evee)
    GestXML.ejer3(butterfree)
}