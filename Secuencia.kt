//kotlinc Secuencia.kt -include-runtime -d Secuencia.jar

// interfaz donde se las funciones para las secuencias
interface Secuencia {

    var elementos: MutableList<Any>

    // Recibe un elemento de tipo E, el mismo tipo de los demas elementos de la secuencia
    // y lo agrega a la misma
    fun agregar(elem:Any)

    // Retorna un elemento de la secuencia y lo elimina de la misma
    fun remover() : Any

    fun vacio() : Boolean {
        return this.elementos.size == 0
    }

}

public class Cola : Secuencia {

    override var elementos : MutableList<Any> = mutableListOf()

    override fun agregar(elem:Any) {
        this.elementos.add(elem)
    }

    override fun remover() : Any {
        var elem : Any = this.elementos[0]
        this.elementos.removeAt(0)
        return elem
    }

    override fun toString() : String {

        // Tiempo de ejecucion : O(1) se retorma una variable solamente
	    return elementos.toString()
    }
}

public class Pila : Secuencia {

    override var elementos : MutableList<Any> = mutableListOf()

    override fun agregar(elem:Any) {
        this.elementos.add(0,elem)
    }

    override fun remover() : Any {
        var elem : Any = this.elementos[0]
        this.elementos.removeAt(0)
        return elem
    }

    override fun toString() : String {

        // Tiempo de ejecucion : O(1) se retorma una variable solamente
	    return elementos.toString()
    }

}

fun main(){
    var x : Cola = Cola()
    var y : Pila = Pila()
    x.agregar(2)
    x.agregar(3)
    println(x)
    println(x.remover())
    y.agregar(2)
    y.agregar(3)
    println(y)
    println(y.remover())
}