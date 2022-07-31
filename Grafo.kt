
import java.util.Queue
import java.util.Stack
import java.util.LinkedList

enum class Color { BLANCO, GRIS, NEGRO }

// Clase nodo para facilitar el control de los datos de cada vertice a la hora de ejecutar
// los algoritmos BFS y DFS
data class Node(var color: Color, 
                var distancia: Int = Int.MAX_VALUE, 
                var predecesor: Int = -1,
                var ti : Int = 0,
                var tf : Int = 0);

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

public class Arco(val inicio: Int, 
                val fin: Int, 
                var peso: Double =0.0) : Lado(inicio, fin) {

    fun fuente() : Int {
        
        // Precondicion
        // !!(this.inicio != null)

        // Tiempo de ejecucion : O(1) se retorma una variable solamente
	    return this.inicio
    }


    // Retorna el vértice final del arco
    fun sumidero() : Int {

        // Precondicion
        // !!(this.fin != null)

        // Tiempo de ejecucion : O(1) se retorma una variable solamente
	    return this.fin
    }

    fun obtenerPeso() : Double {

        // Precondicion
        // !!(this.peso != null)

        // Tiempo de ejecucion : O(1) se retorma una variable solamente
	    return this.peso
    }

    // Representación del arco
    override fun toString() : String {

        // Tiempo de ejecucion : O(1) se retorma una variable solamente
	    return " ${this.inicio} ---> ${this.fin}"
    }
}

public class Arista(val v: Int,
		    val u: Int,
		    val peso: Double =0.0) : Comparable<Arista>, Lado(v, u) {

    // Retorna el peso del arco
    fun peso() : Double {
        // Precondicion, la arista debe tener peso (asi sea 0.0)
        // !!(this.peso!=null)

        // Tiempo de ejecucion : O(1) se retorma una variable solamente
	    return this.peso
    }

    // Representación en string de la arista
    override fun toString() : String {
        // Tiempo de ejecucion : O(1) se retorma una variable solamente
        return "${this.v} ---- ${this.u}  = ${this.u} ---- ${this.v}"
    }

    /* 
     Se compara dos arista con respecto a su peso. 
     Si this.obtenerPeso > other.obtenerPeso entonces
     retorna 1. Si this.obtenerPeso < other.obtenerPeso 
     entonces retorna -1. Si this.obtenerPeso == other.obtenerPeso
     entonces retorna 0 
     */
     override fun compareTo(other: Arista): Int {

        // Precondicion
        // !!(other!=null)

        if (this.peso<other.peso) {
            return -1
        } else if (this.peso>other.peso){
            return 1
        }else{
            return 0
        }

        // Tiempo de ejecucion : O(1) se retorma una variable solamente
    }
}

interface Grafo {

    var lados: HashSet<Int>
    
    // Retorna el número de lados del grafo
    fun obtenerNumeroDeLados() : Int

    // Retorna el número de vértices del grafo
    fun obtenerNumeroDeVertices() : Int

    // Retorna los adyacentes de v
    fun adyacentes(v: Int) : Iterable<Lado>

    // Retorna el grado del grafo
    fun grado(v: Int) : Int

    fun lista() : Iterable<Lado>

    fun contieneLado(v:Int) : Boolean
}

public class GrafoDirigido : Grafo {

    // lista donde se almacenan los lados del grafo
    override var lados: HashSet<Int> = hashSetOf()
    var adjacencyList: List<MutableList<Arco>> = List(0){mutableListOf()}

    constructor(numDeVertices: Int) {

        // Precondicion, el numero de vertices debe ser mayor a 0
        !!(numDeVertices>0)
        adjacencyList = List(numDeVertices+1){mutableListOf()}
        // Tiempo de ejecucion : O(1) se crea la lista pero no se le añade ningun elemento
    }

    // Agrega un lado al digrafo
    fun agregarArco(a: Arco) {

        adjacencyList[a.inicio].add(a)

        // Se añade a la lista lados cada vertice del grafo para llevar un control sobre ellos
        // Como se trata de un conjunto no pueden haber elementos repetidos
        lados.add(a.inicio)
        lados.add(a.fin)
        
        // Tiempo de ejecucion : O(1) se añade en la posicion deseada un elemento
    }

    override fun grado(v: Int) : Int {
        if(!lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }
        // return adyacentes(v).size
        return gradoExterior(v) + gradoInterior(v)
        // Tiempo de ejecucion : O(1) se añade en la posicion deseada un elemento
    }

    override fun lista() : Iterable<Arco>{     
        return adjacencyList.flatten()
        // Tiempo de ejecucion : O(n) donde n es la cantidad de arcos
    }

    fun gradoExterior(v: Int) : Int {

        if(!lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }

        var grado : Int = 0
        for (i in adyacentes(v)){
            grado++
        }
        return grado
        // Tiempo de ejecucion : O(n) donde n es el numero de vertices adyacentes a v
    }

    // Retorna el grado interior del vertice v.
    // Representa el numero de arcos en los cuales v es el final
    fun gradoInterior(v: Int) : Int {

        if(!lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }

        var verticesIncidentes : MutableList<Int> = mutableListOf()
        for (arco in lista()){
            if (arco.fin == v){
                verticesIncidentes.add(arco.inicio)
            }
        }
        // Tiempo de ejecucion : O(n) donde n es el numero de arcos del grafo
        return verticesIncidentes.size
    }

    override fun adyacentes(v: Int) : Iterable<Arco> {
        //Precondicion
        !!(!adjacencyList.isEmpty())

        if(!lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }
        
        // Tiempo de ejecucion : O(1) ya que se retorna solamente el elemento deseado
        return adjacencyList[v]
    }

    override fun contieneLado(v:Int) : Boolean{
        return lados.contains(v)
    }

    // Retorna el número de lados del grafo
    override fun obtenerNumeroDeLados() : Int {
        // Tiempo de ejecucion : O(1) ya que se retorna solamente el elemento deseado
        return lados.size
    }

    // Retorna el número de vértices del grafo
    override fun obtenerNumeroDeVertices() : Int {

        // Precondicion , el grafo debe tener algun elemento
        !!(!adjacencyList.isEmpty())

        // Tiempo de ejecucion : O(1) se retorna el tamaño de la lista solamente
        return adjacencyList.size
    }
}

public class GrafoNoDirigido : Grafo {
    override var lados: HashSet<Int> = hashSetOf()
    var adjacencyList: List<MutableList<Arista>> = List(0){mutableListOf()}

    constructor(numDeVertices: Int) {

        // Precondicion, el numero de vertices debe ser mayor a 0
        !!(numDeVertices>0)
        
	    adjacencyList = List(numDeVertices+1){mutableListOf()}
        // Tiempo de ejecucion : O(1) se crea la lista pero no se le añade ningun elemento
    }

    // Agrega un lado al grafo no dirigido
    fun agregarArista(a: Arista) {

        // Precondicion , debe existir el archivo .txt
        //!!(File(archivo).exists())

	    adjacencyList[a.v].add(a)
        adjacencyList[a.u].add(Arista(a.u,a.v))

        // Se añade a la lista lados cada vertice del grafo para llevar un control sobre ellos
        // Como se trata de un conjunto no pueden haber elementos repetidos
        lados.add(a.u)
        lados.add(a.v)

        // Tiempo de ejecucion : O(1), solo se insertan valores
    }

    override fun contieneLado(v:Int) : Boolean{
        return lados.contains(v)
    }

    // Retorna el número de lados del grafo
    override fun obtenerNumeroDeLados() : Int {
        
        return lados.size
        // Tiempo de ejecucion : O(1), solo se retorna un valor
    }

    // Retorna el número de vértices del grafo
    override fun obtenerNumeroDeVertices() : Int {

        // Precondicion , el grafo debe tener algun elemento
        !!(!adjacencyList.isEmpty())

        // Tiempo de ejecucion : O(1) se retorna el tamaño de la lista solamente
	    return adjacencyList.size
    }

    // Grado del vertice v del grafo
    override fun grado(v: Int) : Int {
        if(!lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }
        var ady : MutableList<Int> = mutableListOf()
        for (i in adyacentes(v)){
            ady.add(i.elOtroVertice(v))
        }
        return ady.size
        // Tiempo de ejecucion : O(1) ya que se retorna solamente el elemento deseado
    }

    override fun lista() : Iterable<Arista>{
        return adjacencyList.flatten()
        // Tiempo de ejecucion : O(1) ya que se retorna solamente el elemento deseado
    } 

    // Retorna los lados adyacentes al vértice v, es decir, los lados que contienen al vértice v
    override fun adyacentes(v: Int) : Iterable<Arista> {
        //Precondicion
        !!(!adjacencyList.isEmpty())
        
        if(!lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }
        // Tiempo de ejecucion : O(1) ya que se retorna solamente el elemento deseado
	    return adjacencyList[v]
    }
}

abstract class Busqueda(val g: Grafo) {

    // Lista donde se almacenaran los nodos que representan cada vertice del grafo con las
    // propiedades de tiempo, distancia, color y predecesor
    lateinit var nodos : MutableList<Node>
    var tiempo : Int = 0
    val cola : Queue<Int> = LinkedList<Int>()
    val pila : Stack<Lado> = Stack()

    open fun buscar(inicio:Int,fin:Int) : Int    {
        return -1
    }

    fun obtenerPredecesor(v: Int) : Int { 
        if(!g.contieneLado(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }
        return nodos[v].predecesor
        // Tiempo de ejecucion : O(n) se devuelve solamente un valor
    }

    // Retorna la distancia desde el nodo s hasta v.
    // Al aplicar BFS se garantiza que esta distancia es la minima posible
    fun obtenerDistancia(v: Int) : Int { 

        if(!g.contieneLado(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }

        return nodos[v].distancia
        // Tiempo de ejecucion : O(n) se devuelve solamente un valor
    }

    // Se retorna un par con los tiempos inicial y final de cada vertice
    fun obtenerTiempos(v: Int) : Pair<Int, Int> {  
        if(!g.contieneLado(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }
        return Pair(nodos[v].ti,nodos[v].tf)
        // Tiempo de ejecucion : O(1) se devuelve un valor solamente
    }
}

public class BFS(g:Grafo) : Busqueda(g) {

    override fun buscar(inicio:Int,fin:Int) : Int{

        nodos = mutableListOf()
        for (i in 0..g.obtenerNumeroDeVertices()-1){
            nodos.add(Node(Color.BLANCO))
        }

        nodos[inicio].distancia = 0

        // Se añade el elemento s a la cola
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
        
        var distancia : Int = obtenerDistancia(fin)
        if (distancia<Int.MAX_VALUE){
            return distancia
        }
        return -1
    }

    fun hayCaminoHasta(v: Int) : Boolean {

        if(!g.lados.contains(v)){
            throw RuntimeException("El elemento no se encuentra en el grafo")
        }

        if (nodos[v].distancia<Int.MAX_VALUE){
            return true
        }else{
            return false
        }

        // Tiempo de ejecucion : O(n) se devuelve solamente un valor
    }
}

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
            print(ady)
            if (nodos[ady.cualquieraDeLosVertices()].color == Color.BLANCO){
                dfsVisit(g,ady.cualquieraDeLosVertices())
            }
        }

        var pred : Int = nodos[fin].predecesor
        var acm : Int = 0
        while (pred != -1 || pred != inicio) {
            acm ++
            pred = nodos[pred].predecesor
            if (pred == inicio){
                acm ++
                break
            }
        }

        if (acm == 0){
            return -1
        }
        return acm
    }

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
        // Tiempo de ejecucion : O(V+E) donde V y E son los vertices y lados del grafo respectivamente
    }  
}

fun main() {
    val grafo : GrafoDirigido = GrafoDirigido(10)
    grafo.agregarArco(Arco(0,1))
    grafo.agregarArco(Arco(1,2))
    grafo.agregarArco(Arco(2,3))
    val bfs : BFS = BFS(grafo)
    println(bfs.buscar(0,3))
    println(bfs.buscar(3,2))
    val dfs : DFS = DFS(grafo)
    println(dfs.buscar(0,3))
}