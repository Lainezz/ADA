import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

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

            //Con la conexion creada, vamos a obtener un objeto
            //Statement
            val st: Statement = con.createStatement();

            //El siguiente paso ya sería ejecutar una sentencia SQL
            val sentencia: String = "SELECT * FROM alumnos;"
            val results: ResultSet = st.executeQuery(sentencia)

            //Iteramos sobre los resultados que obtenemos
            while (results.next()){
                println(results.getString("DNI"))
                println(results.getString("Nombre"))
                println(results.getString("Edad"))
                println(results.getString("Ciudad"))
            }

        }


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