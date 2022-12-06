package tema1.Practica.EscribirLeerConf

class PropiedadesUsuario(user: String, password: String, port: String, server: String) {

    var user: String = user
        set(value) {
            if (user.isEmpty()) throw IllegalArgumentException()
            field = value
        }
        init {
            this.user = user
        }

    var password: String = password
        set(value) {
            if (password.isEmpty()) throw IllegalArgumentException()
            field = value
        }
        init {
            this.password = password
        }

    var port: String = port

    var server: String = server


}