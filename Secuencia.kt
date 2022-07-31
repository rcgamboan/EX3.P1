// Pregunta 1, Parte b, Examen 3 CI3641.
// Elaborado por Roberto Gamboa, 16-10394

// Para compilar el archivo se puede usar el comando:
// kotlinc Secuencia.kt -include-runtime -d Secuencia.jar
// y posteriormente correrlo con java -jar Secuencia.jar

// Implementacion de la clase secuencia, se trata de una secuencia de cualquier tipo de elementos

// interfaz donde se definen las funciones para las secuencias
// se instancia segun se trate de una cola o pilo y se sobreescriben las funciones remover y agregar
// segun corresponda
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
        if (this.vacio()){
            throw RuntimeException "No quedan elementos en la cola"
        }
        var elem : Any = this.elementos[0]
        this.elementos.removeAt(0)
        return elem
    }

    override fun toString() : String {

	    return elementos.toString()
    }
}

public class Pila : Secuencia {

    override var elementos : MutableList<Any> = mutableListOf()

    override fun agregar(elem:Any) {
        this.elementos.add(0,elem)
    }

    override fun remover() : Any {
        if (this.vacio()){
            throw RuntimeException "No quedan elementos en la pila"
        }
        var elem : Any = this.elementos[0]
        this.elementos.removeAt(0)
        return elem
    }

    override fun toString() : String {
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