import java.sql.*

fun main() {
    //Si miramos la documentación, vemos que la clase DriverManager tiene un método "getConnection"
    //Para realizar dicha conexión debemos establecer 4 parámetros
    val url: String = "jdbc:mysql://localhost/"
    val bd: String = "bddeprueba"
    val user: String = "root"
    val pass: String = ""

    var con: Connection? = null
    try {
        // Cargamos el Driver que permite conectarme a una BBDD Mysql
        //Ya tendríamos un "traductor" entre MySQL y nuestra aplicación
        Class.forName("com.mysql.cj.jdbc.Driver")

        //Obtenemos un objeto Connection.
        //Ese objeto ES la conexión que se ha realizado con la base de datos
        //Utilizamos la clase DriverManager
        con = DriverManager.getConnection(url+bd, user, pass)

        if(con!=null){
            println("[Conexion realizada]")

            //Con la conexion creada, vamos a explorar otras opciones que tenemos

            //Tenemos la clase DatabaseMetaData. Con la que podemos obtener informacion de la BD
            val infoDB: DatabaseMetaData = con.metaData
            println("***DATOS SGBD***")
            println("Nombre SGBD: ${infoDB.databaseProductName}")
            println("Version del SGBD: ${infoDB.databaseProductVersion}")
            println("Nombre del Driver: ${infoDB.driverName}")
            println("Version del Driver: ${infoDB.driverVersion}")

            //Si seguimos con el proceso de siempre, ahora tendríamos que crear un Statement
            val st: Statement = con.createStatement()

            //Podemos explorar diferentes métodos de Statement
            //******
            //executeQuery(String sql)
            //-Para sentencias SELECT
            //-Devuelve un resultSet
            val rs: ResultSet = st.executeQuery(Sentencias.selectAll);
            while (rs.next()){
                //Podemos indicarle el numero de la columna o el nombre de la columna
                println(rs.getString(1))
                println(rs.getString(2))
                println(rs.getString(3))
                println(rs.getString(4))
            }
            rs.close()

            //******
            //executeUpdate(String sql)
            //-Para sentencias INSERT/UPDATE/DELETE
            //-Devuelve el nº de filas afectadas
            //var nFilas: Int = st.executeUpdate(Sentencias.insertFila)
            //if(nFilas > 0) println("Fila Insertada") else println("Ninguna fila afectada")

            var nFilas = st.executeUpdate(Sentencias.insertFila2)
            if(nFilas > 0) println("Fila Insertada") else println("Ninguna fila afectada")

            //(Eliminamos lo insertado con insertFila2)
            nFilas = st.executeUpdate(Sentencias.deleteFila)
            if(nFilas > 0) println("Fila Eliminada") else println("Ninguna fila afectada")

            //******
            //execute(String sql)
            //-Para sentencias donde no se sabe qué va a devolver la consulta
            //-Devuelve true o false
            // true -> Se devuelve un objeto ResultSet (la sentencia ha sido tipò SELECT)
            //      método .getResultSet()
            // false -> Se devuelve otra cosa similar a lo devuelto por sentencias INSERT/UPDATE/DELETE
            //      método .getUpdateCount()
            val bool: Boolean = st.execute(Sentencias.selectAll)
            if(bool){
                val rs2: ResultSet = st.resultSet;
                while (rs2.next()){
                    //Podemos indicarle el numero de la columna o el nombre de la columna
                    println(rs2.getString(1))
                    println(rs2.getString(2))
                    println(rs2.getString(3))
                    println(rs2.getString(4))
                }
                rs2.close()

            } else {
                if(st.updateCount > 0) println("Fila Eliminada") else println("Ninguna fila afectada")
            }

            //También podemos explorar otra clase. ResultSetMetadata
            //Esta clase nos permite obtener información de ResultSet
            val rs3: ResultSet = st.executeQuery(Sentencias.selectAll)
            val metaDatosRS: ResultSetMetaData = rs3.metaData;

            //Podemos saber el nº de columnas que tiene el resultSet
            println("Nº de Columnas: ${metaDatosRS.columnCount}")
            //Sabiendo el nº de columnas, podemos averiguar sus nombres y tipos
            for(i in 1..metaDatosRS.columnCount){
                println("Col(${i}): \"${metaDatosRS.getColumnLabel(i)}\" tipo: ${metaDatosRS.getColumnTypeName(i)}")
            }

            //Sabiendo que podemos obtener los tipos de las columnas, podemos ampliar lo que sabemos sobre los tipos SQL
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

            //Por último, podemos ver otra clase muy interesante para ejecutar sentencias SQL
            //PreparedStatement
            //Podemos usar esta clase cuando vayamos a ejecutar sentencias de forma repetida
            //En vez de tener que escribir la misma sentencia una y otra vez, podemos escribirla 1 vez
            //y simplemente cambiar los parámetros
            val ps: PreparedStatement = con.prepareStatement("INSERT INTO alumnos VALUES(?,?,?,?);")
            //Cada ? corresponderá a un parámetro
            //Ahora tenemos que ir añadiendo dichos parámetros. Indicamos el nº del param... y el valor del param
            ps.setString(1,"72734672C")
            ps.setString(2,"M.Rajoy")
            ps.setInt(3,75)
            ps.setString(4,"A Corunia")

            //Ejecutamos la query con executeUpdate
            //Ya tenemos la query cargada en nuestro PreparedStatement
            nFilas = ps.executeUpdate()
            if(nFilas > 0) println("Fila Insertada") else println("Ninguna fila afectada")

            //Probamos a ejecutar un DELETE
            val ps2: PreparedStatement = con.prepareStatement("DELETE FROM alumnos WHERE DNI = ?;")

            ps2.setString(1,"72734672C")

            nFilas = ps2.executeUpdate()
            if(nFilas > 0) println("Fila Eliminada") else println("Ninguna fila afectada")


            //FINALMENTE DEBERIAMOS LIBERAR LOS RECURSOS
            //Liberar ResultSets, Statements y PreparedStatements se considera una buena práctica.
            //Se debe hacer en una clausula finally, aunque por simplicidad lo hago aquí
            ps2.close()
            ps.close()
            st.close()
            rs.close()
            rs3.close()

        }

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


    } catch (e: ClassNotFoundException){
        e.printStackTrace()
    } catch (e: SQLException){
        e.printStackTrace()
    } finally {
        //Ultimo paso de todos es cerrar la conexion
        if (con != null){
            con.close()
        }
    }
}