import tema2.EjercicioClase.Vista.AppVista

fun main() {
    /*
    Este MAIN actúa como un MainActivity en Android.
    Aquí se estarían escuchando los "eventos" que tienen asociados, por ejemplo, los botones.
    Lo que son los elementos propios de la vista, están en AppVista...
    y aquí sólo queda la implementación lógica de qué hacer cuando esos elementos son manipulados

    En nuestro pequeño ejemplo, esos eventos quedaría ejemplificados con:
        Lo que nos devuelven los métodos de AppVista (sería un poco... qué boton ha sido clicado)
        Qué se hace dependiendo de qué botón ha sido clicado.

    https://medium.com/@anilkumar2681/kotlin-login-demo-using-mvc-pattern-with-validation-a4e030b50f9f
     */

    /*
    Para intentar plantear una situación lo más cercana a la realidad posible... podemos usar
    multithreading.

    En kotlin realizar multithreading es muy sencillo.
    Basta con usar la función thread(start=true){ ... }

    Con la llamada a ese método, ya estaríamos creando un hilo nuevo
    https://www.baeldung.com/kotlin/threads-coroutines
     */

    val appVista: AppVista = AppVista()
    val app: AppController = AppController(appVista)


    when (app.onStart()) {
        1 -> {
            app.onLogin()
        }

        2 -> {
            app.onAlta()
        }

        0 -> {
            app.onExit()
        }
    }
    app.onExit()


}