import java.sql.Connection
import java.sql.DatabaseMetaData
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.sql.Statement





fun main() {

    val url: String = "jdbc:mysql://localhost/"
    val bd: String = "bddeprueba"
    val user: String = "root"
    val pass: String = ""

    //Cargar el driver de la BBDD
    Class.forName("com.mysql.cj.jdbc.Driver")
    //Una vez cargado el Driver, abrimos la conexión
    val con: Connection = DriverManager.getConnection(url+bd, user, pass)

    //VAMOS A VER ALGUNAS CLASES MÁS ADEMÁS DE LAS QUE VIMOS
    /*
    DatabaseMetadata
    Información relativa a la base de datos
     */
    val dbMeta: DatabaseMetaData = con.metaData
    println("Nombre del SGBD: ${dbMeta.databaseProductName}")
    println("Version del SGBD: ${dbMeta.databaseProductVersion}")

    println("Nombre del Driver: ${dbMeta.driverName}")
    println("Version del Driver: ${dbMeta.driverVersion}")

    /*
    Statement

    executeQuery(sql: String)
    - Para sentencias SELECT
    - Devuelve un objeto ResultSet

    executeUpdate(sql: String)
    - Para sentencias INSERT/UPDATE/DELETE
    - Devuelve el nº de filas afectadas

    execute(sql: String)
    - Cuando no sabemos lo que nos viene
    - true / false
    - true -> Se ha ejecutado un SELECT -> Tenemos un ResultSet
        - .getResultSet()
    - false -> Se ha ejecutado un executeUpdate -> El nº de filas afectadas
        - .getUpdateCount()
     */
    val st: Statement = con.createStatement();
    val rs1: ResultSet = st.executeQuery(Sentencias.selectAll)
    while(rs1.next()){
        println(rs1.getString(1))
        println(rs1.getString(2))
        println(rs1.getString(3))
        println(rs1.getString(4))
    }

    val st2: Statement = con.createStatement();
    var nFilas: Int = st2.executeUpdate(Sentencias.insertFila2)
    if(nFilas>0) println("${nFilas} afectada(s)") else println("Ninguna fila afectada")

    nFilas = st2.executeUpdate(Sentencias.deleteFila)
    if(nFilas>0) println("${nFilas} afectada(s)") else println("Ninguna fila afectada")

    //VEMOS EL EXECUTE
    val st3: Statement = con.createStatement()
    var bool: Boolean = st3.execute(Sentencias.selectAll)
    if(bool){
        val rs2: ResultSet = st3.resultSet
        //iteramos sobre el RSet como siempre lo hemos hecho
        while(rs2.next()){
            println(rs2.getString(1))
            println(rs2.getString(2))
            println(rs2.getString(3))
            println(rs2.getString(4))
        }
    } else {
        var nFilasAfectadas: Int = st3.updateCount
        if(nFilasAfectadas>0) println("${nFilasAfectadas} afectada(s)") else println("Ninguna fila afectada")
    }

    //También, podemos explorar la clase ResultSetMetadata
    /*
    ResultSetMetadata
    Contiene información relativa a las columnas o las filas que obtenemos en una consulta
     */
    val st4: Statement = con.createStatement()
    val rs4: ResultSet = st4.executeQuery(Sentencias.selectAll)
    val rsMetaData: ResultSetMetaData = rs4.metaData //<-- getMetaData()

    //Podemos por ejemplo, conocer el número de columnas que tiene nuestro conjunto de resultados
    val nColumns: Int = rsMetaData.columnCount //<-- getColumnCount()

    //Si sé el nº de columnas, puedo iterar sobre ellas para conocer sus nombres.
    for(i in 1..nColumns){
        println("Col(\"${i}\"): ${rsMetaData.getColumnName(i)}, con tipo: ${rsMetaData.getColumnTypeName(i)}")
    }
    /*
        getInt <- INTEGER
        getLong <- BIG INT
        getFloat <- REAL
        getDouble <- FLOAT
        getBignum <- DECIMAL
        getBoolean <- BIT
        getString <- VARCHAR
        getString <- CHAR
        getDate <- DATE
        getTime <- TIME
        getTimesstamp <- TIME STAMP
        getObject <- cualquier otro tipo
     */

    //POR ÚLTIMO vamos a ver la clase PreparedStatement
    /*
    PreparedStatement

    Con Statement: INSERT INTO alumnos VALUES('12345678Z','Menganito Di Sousa',46,'Leganés');
    Con PreparedStatement: INSERT INTO alumnos VALUES(?,?,?,?);
    */
    //"Compilamos" la sentencia SQL
    val ps: PreparedStatement = con.prepareStatement("INSERT INTO alumnos VALUES(?,?,?,?)")
    ps.setString(1, "12345678Z")
    ps.setString(2, "Menganito Di Sousa")
    ps.setInt(3, 46)
    ps.setString(4, "Alcalá la Real")

    val filasAfectadas: Int = ps.executeUpdate()
    if(filasAfectadas>0) println("${filasAfectadas} afectada(s)") else println("Ninguna fila afectada")


    //LIBERAMOS RECURSOS DE LA BDD
    st.close()
    st2.close()
    st3.close()
    st4.close()

    rs1.close()
    rs4.close()

    ps.close()

    con.close()

    //EJERCICIO PARA CLASE
    /*
    Uno de los principios básicos de la programación orientada a objetos es el del
    "Principio de ocultación"
    Este nos dice que cada objeto está aislado del exterior, y este sólo expone
    una "interfaz" al resto de objetos para que puedan interactuar con este.

    En nuestro caso, no tendría sentido que cualquiera pudiera, por ejemplo,
    cambiar el nombre de la base de datos, o el usuario, o cualquier cosa relativa a la BD

    Para conseguir esto, vamos a crear una clase que contenga 4 métodos

    -> Uno para realizar una conexión a una base de datos
        *Los datos no se los tenemos que pasar, sino que están en la clase, y son privados
        *Incluso la conexión
    -> Otro para realizar una desconexión de una base de datos
    -> Otro para realizar una consulta de todos los datos de dicha BDD
    -> Otro para realizar una consulta de datos concretos de dicha BDD

    */
}