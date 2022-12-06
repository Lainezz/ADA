package tema2.EjercicioClase.Modelo.Sentencias

object SQLSentences {
    val selectAll: String = "SELECT * FROM alumnos"
    val selectUser: String = "SELECT * FROM alumnos WHERE dni = ?"

    val insertEstudiante: String = "INSERT INTO alumnos(DNI, Nombre, Edad, Ciudad) VALUES (?,?,?,?);"
    val insertAsignatura: String = "INSERT INTO asignaturas(codigo, dni_alumno, titulo, familia) VALUES (?,?,?,?)"
}