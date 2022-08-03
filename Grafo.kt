// Pregunta 1, Parte b, Examen 3 CI3641.
// Elaborado por Roberto Gamboa, 16-10394

// Para compilar el archivo se puede usar el comando:
// kotlinc Grafo.kt -include-runtime -d Grafo.jar
// y posteriormente correrlo con java -jar Grafo.jar

// Implementacion de grafos dirigidos y no dirigidos
// junto con las clases Busqueda, DFS y BFS

import java.util.Queue
import java.util.Stack
import java.util.LinkedList

// Clase enum que representa los colores de los nodos al aplicar DFS o BFS
enum class Color { BLANCO, GRIS, NEGRO }

// Clase nodo para facilitar el control de los datos de cada vertice a la hora de ejecutar BFS y DFS
data class Node(var color: Color, 
                var distancia: Int = Int.MAX_VALUE, 
                var predecesor: Int = -1,
                var ti : Int = 0,
                var tf : Int = 0);

// Clase abstracta lado de la cual heredan los arcos o aristas
// que son usados segun se tenga un grafo dirigido o no dirigido
// recibe dos numeros enteros que representan cada vertice del lado
abstract class Lado(val a: Int, val b: Int) {

    // Retorna cualquiera de los dos vértices del grafo
    fun cualquieraDeLosVertices() : Int {
        return this.a
    }

    // Dado un vertice w, si w == a entonces retorna b, de lo contrario si w == b
    // entonces retorna a,  y si w no es igual a a ni a b, entonces se lanza una RuntimeExpception 
    fun elOtroVertice(w: Int) : Int {
        if (w==a){
            return b
        }else if (w==b){
            return a
        }else{
            throw RuntimeException("Error")
        }
    }
}

// Clase arco, que representa los lados de un grafo dirigido
public class Arco(val inicio: Int, val fin: Int ) : Lado(inicio, fin) {

    fun fuente() : Int {
        
	    return this.inicio
    }


    // Retorna el vértice final del arco
    fun sumidero() : Int {

	    return this.fin
    }

    // Representación del arco
    override fun toString() : String {

	    return " ${this.inicio} ---> ${this.fin}"
    }
}

// Clase arco, que representa los lados de un grafo no dirigido
public class Arista(val v: Int,val u: Int) : Lado(v, u) {

    // Representación en string de la arista
    override fun toString() : String {
        // Tiempo de ejecucion : O(1) se retorma una variable solamente
        return "${this.v} ---- ${this.u}  = ${this.u} ---- ${this.v}"
    }

}

// interfaz Grafo de la cual se hereda para crear los grafos dirigidos y no dirigidos
interface Grafo {

    var lados: HashSet<Int>
    
    // Retorna el número de lados del grafo
    fun obtenerNumeroDeLados() : Int

    // Retorna el número de vértices del grafo
    fun obtenerNumeroDeVertices() : Int

    // Retorna los adyacentes de v
    fun adyacentes(v: Int) : Iterable<Lado>

    fun lista() : Iterable<Lado>

    fun contieneLado(v:Int) : Boolean
}

public class GrafoDirigido : Grafo {

    // lista donde se almacenan los lados del grafo
    override var lados: HashSet<Int> = hashSetOf()
    var adjacencyList: List<MutableList<Arco>> = List(0){mutableListOf()}

    constructor(numDeVertices: Int) {

        adjacencyList = List(numDeVertices+1){mutableListOf()}
    }

    // Agrega un lado al digrafo
    fun agregarArco(a: Arco) {

        adjacencyList[a.inicio].add(a)

        // Se añade a la lista lados cada vertice del grafo para llevar un control sobre ellos
        // Como se trata de un conjunto no pueden haber elementos repetidos
        lados.add(a.inicio)
        lados.add(a.fin)
    }

    override fun lista() : Iterable<Arco>{     
        return adjacencyList.flatten()
    }

    override fun adyacentes(v: Int) : Iterable<Arco> {
    
        if(!lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }
    
        return adjacencyList[v]
    }

    override fun contieneLado(v:Int) : Boolean{
        return lados.contains(v)
    }

    // Retorna el número de lados del grafo
    override fun obtenerNumeroDeLados() : Int {
        return lados.size
    }

    // Retorna el número de vértices del grafo
    override fun obtenerNumeroDeVertices() : Int {

        return adjacencyList.size
    }
}

public class GrafoNoDirigido : Grafo {
    override var lados: HashSet<Int> = hashSetOf()
    var adjacencyList: List<MutableList<Arista>> = List(0){mutableListOf()}

    constructor(numDeVertices: Int) {
	    adjacencyList = List(numDeVertices+1){mutableListOf()}
    }

    // Agrega un lado al grafo no dirigido
    fun agregarArista(a: Arista) {

	    adjacencyList[a.v].add(a)
        adjacencyList[a.u].add(Arista(a.u,a.v))

        // Se añade a la lista lados cada vertice del grafo para llevar un control sobre ellos
        // Como se trata de un conjunto no pueden haber elementos repetidos
        lados.add(a.u)
        lados.add(a.v)
    }

    override fun contieneLado(v:Int) : Boolean{
        return lados.contains(v)
    }

    // Retorna el número de lados del grafo
    override fun obtenerNumeroDeLados() : Int {
        return lados.size
    }

    // Retorna el número de vértices del grafo
    override fun obtenerNumeroDeVertices() : Int {
	    return adjacencyList.size
    }

    override fun lista() : Iterable<Arista>{
        return adjacencyList.flatten()
    } 

    // Retorna los lados adyacentes al vértice v, es decir, los lados que contienen al vértice v
    override fun adyacentes(v: Int) : Iterable<Arista> {
        
        if(!lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }
	    return adjacencyList[v]
    }
}

// Clase abstracta busqueda
// contiene todas las funciones que DFS o BFS usan
// ambos sobreescriben el metodo buscar
abstract class Busqueda(val g: Grafo) {

    // Lista donde se almacenaran los nodos que representan cada vertice del grafo con las
    // propiedades de tiempo, distancia, color y predecesor
    lateinit var nodos : MutableList<Node>
    var tiempo : Int = 0
    val cola : Queue<Int> = LinkedList<Int>()
    val pila : Stack<Lado> = Stack()

    abstract fun buscar(inicio:Int,fin:Int) : Int

    fun obtenerPredecesor(v: Int) : Int { 
        if(!g.contieneLado(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }
        return nodos[v].predecesor
    }

    // Retorna la distancia desde el nodo s hasta v.
    fun obtenerDistancia(v: Int) : Int { 

        if(!g.contieneLado(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }

        return nodos[v].distancia
    }
}

// Clase BFS que explora los vertices del grafo desde el vertice inicial
// obtiene la distancia entre el vertice inicial y todos los demas vertices
// si algun nodo no es alcanzable, la distancia hacia ese nodo es infinita
public class BFS(g:Grafo) : Busqueda(g) {

    override fun buscar(inicio:Int,fin:Int) : Int{

        nodos = mutableListOf()
        for (i in 0..g.obtenerNumeroDeVertices()-1){
            nodos.add(Node(Color.BLANCO))
        }

        nodos[inicio].distancia = 0

        // Se añade el nodo inicial a la cola
        cola.add(inicio)

        // Mientras que queden elementos en la cola, se ejecuta el BFS
        while (cola.size!=0) {
            
            // El metodo poll() remueve el primer elemento de la cola y lo retorna
            var u = cola.poll()

            // Se verifica cuales nodos faltan por visitar y se trabaja sobre ellos
           for (ady in g.adyacentes(u)) {
               if (nodos[ady.elOtroVertice(u)].color == Color.BLANCO){
                   nodos[ady.elOtroVertice(u)].color = Color.GRIS
                   nodos[ady.elOtroVertice(u)].distancia = nodos[u].distancia + 1
                   nodos[ady.elOtroVertice(u)].predecesor = u
                   cola.add(ady.elOtroVertice(u))
               }
           }
           // Se marca con negro los nodos ya explorados
           nodos[u].color = Color.NEGRO
        }
        
        // despues de aplicar BFS, se obtiene la distancia desde el nodo inicial hasta el nodo final
        // si la distancia no es infinita, el nodo final es alcanzable
        // y se retorna la distancia entre ambos
        // caso contrario se retorna -1
        var distancia : Int = obtenerDistancia(fin)
        if (distancia<Int.MAX_VALUE){
            return distancia
        }
        return -1
    }
}

// Clase DFS que explora los vertices del grafo desde el vertice inicial
// obtiene la distancia entre el vertice inicial y todos los demas vertices
// si algun nodo no es alcanzable, la distancia hacia ese nodo es infinita
public class DFS(g:Grafo) : Busqueda(g) {

    override fun buscar(inicio:Int,fin:Int) : Int{

        for (ady in g.lista().reversed()){
            pila.push(ady)
        }

        nodos = mutableListOf()
        for (i in 0..g.obtenerNumeroDeVertices()-1){
            nodos.add(Node(Color.BLANCO))
        }
        
        while (!pila.empty()){
            var ady : Lado = pila.pop()
            if (nodos[ady.cualquieraDeLosVertices()].color == Color.BLANCO){
                dfsVisit(g,ady.cualquieraDeLosVertices())
            }
        }

        // despues de aplicar DFS, se itera sobre los predecesores del nodo final hasta llegar al inicial
        // si se llega al nodo inicial, se puede alcanzar el nodo final desde el inicial
        // si se llega a un nodo con predecesor -1, el nodo final no es alcanzable desde el inicial
        var pred : Int = nodos[fin].predecesor
        if (pred == -1){
            return -1
        }
        var acm : Int = 0
        while (pred != -1 || pred != inicio) {
            acm ++
            pred = nodos[pred].predecesor
            if (pred == inicio){
                acm ++
                break
            }
            if (pred == -1){
                break
            }
        }

        if (acm == 0){
            return -1
        }
        return acm
    }

    // Metodo auxiliar de dfs donde se recorren los nodos a partir del nodo u
    // se recorren los nodos a profundidad
    private fun dfsVisit(g: Grafo, u: Int) {

        tiempo ++
        nodos[u].ti = tiempo
        nodos[u].color = Color.GRIS
        for (ady in g.adyacentes(u)){

            if (nodos[ady.elOtroVertice(u)].color == Color.BLANCO){
                nodos[ady.elOtroVertice(u)].predecesor = u
                dfsVisit(g,ady.elOtroVertice(u))
            }
        }
        nodos[u].color = Color.NEGRO
        tiempo++
        nodos[u].tf = tiempo
    }  
}

fun main() {
    val grafo : GrafoDirigido = GrafoDirigido(10)
    grafo.agregarArco(Arco(0,1))
    grafo.agregarArco(Arco(1,2))
    grafo.agregarArco(Arco(3,0))
    val bfs : BFS = BFS(grafo)
    println(bfs.buscar(0,3))
    println(bfs.buscar(3,2))
    val dfs : DFS = DFS(grafo)
    println(dfs.buscar(0,3))
}