// Ilyas Galiev
// from lab13 and geeks for geeks materials

import java.util.*;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] line1 = sc.nextLine().split(" ");
        int n = Integer.parseInt(line1[0]);
        Graph<Integer> graph = new Graph<>();
        for (int  i = 0; i < n; i++){
            graph.addVertex(i);
        }
        for (int i = 0; i < n; i++){
            String[] line = sc.nextLine().split(" ");
            for (int j = 0; j < n; j++){
                int ds = Integer.parseInt(line[j]);
                if (ds != 100000){
                    Vertex<Integer> v1 = graph.vertices.get(i).data;
                    Vertex<Integer> v2 = graph.vertices.get(j).data;
                    graph.addEdge(v1, v2, ds);
                }
            }
        }

        ArrayList<Vertex<Integer>> res = graph.IlyasGaliev_sp(graph.vertices.head.data);
        if (res == null){
            System.out.println("NO");
        } else {
            System.out.println("YES");
            System.out.println(res.size());
            String str = "";
            for (int i = res.size() - 1; i >= 0; i--){
                str += (res.get(i).value + 1) + " ";
            }
            System.out.println(str);
        }
    }
}

class Graph<K>{
    public DoubleLinkedList<Vertex<K>> vertices;
    public int vertices_size;
    public DoubleLinkedList<Edge<K>> edges;

    public Graph() {
        vertices = new DoubleLinkedList<>();
        edges = new DoubleLinkedList<>();
        vertices_size = 0;
    }

    Edge<K> addEdge(Vertex<K> from, Vertex<K> to, int weight){
        Edge<K> edge = new Edge<>(from, to, weight);
        this.edges.InsertToEnd(edge);
        from.outAdjacent.InsertToEnd(to);
        to.inAdjacent.InsertToEnd(from);
        return edge;
    }

    Vertex<K> addVertex(K value) {
        Vertex<K> v = new Vertex<>(value);
        this.vertices.InsertToEnd(v);
        vertices_size++;
        return v;
    }

    ArrayList<Vertex<K>> IlyasGaliev_sp(Vertex<K> source){
        // Step 1: Initialize distances
        for (Node<Vertex<K>> node = vertices.head; node != null; node = node.past) {
            node.data.distance = 0;
            node.data.parent = null;
        }

        // Step 2: Relax edges repeatedly
        for (int i = 0; i <= vertices_size - 1; i++) {
            for (Node<Edge<K>> node = edges.head; node != null; node = node.past) {
                Vertex<K> u = node.data.from;
                Vertex<K> v = node.data.to;
                int weight = node.data.weight;
                if (u.distance != 100000 && u.distance + weight < v.distance) {
                    v.distance = u.distance + weight;
                    v.parent = u;
                }
            }
        }

        // Step 3: Check for negative cycles
        for (Node<Edge<K>> node = edges.head; node != null; node = node.past) {
            Vertex<K> u = node.data.from;
            Vertex<K> v = node.data.to;
            int weight = node.data.weight;
            if (u.distance != 100000 && u.distance + weight < v.distance) {
                // Negative cycle found
                return printNegativeCycle(v);
            }
        }
        return null;
    }
    private ArrayList<Vertex<K>> printNegativeCycle(Vertex<K> startVertex) {
        //Finding the start of the loop
        HashSet<Vertex<K>> cycleFind = new HashSet<>();
        Vertex<K> start = startVertex;
        while (start != null){ //Predicting infinite loop in case of loop vertex
            if (!cycleFind.add(start)) { // If adding fails, cycle start found
                break;
            }
            start = start.parent;
        }
        // Collecting cycle elements
        Vertex<K> currentVertex = start.parent;
        ArrayList<Vertex<K>> cycle = new ArrayList<>();
        while (!cycle.contains(currentVertex)){
            cycle.add(currentVertex);
            currentVertex = currentVertex.parent;
        }
        return cycle;
    }
}

class Vertex<V>{
    public V value;
    public DoubleLinkedList<Vertex<V>> inAdjacent;
    public DoubleLinkedList<Vertex<V>> outAdjacent;
    public int inDegree, outDegree;
    int distance;
    Vertex<V> parent;

    public Vertex(V value) {
        this.value = value;
        this.inDegree = 0;
        this.outDegree = 0;
        this.inAdjacent = new DoubleLinkedList<>();
        this.outAdjacent = new DoubleLinkedList<>();
    }
}

class Edge<K>{
    public Vertex<K> from;
    public Vertex<K> to;
    public int weight;

    public Edge(Vertex<K> start, Vertex<K> end, int weight) {
        this.from = start;
        this.to = end;
        this.weight = weight;
    }
}

class DoubleLinkedList<O>{
    Node<O> head;
    Node<O> tail;

    public DoubleLinkedList() {
        head = null;
        tail = null;
    }
    void InsertToEnd(O obj){
        Node<O> temp = new Node<>(obj);
        if (tail == null) {
            head = temp;
            tail = temp;
        }
        else {
            tail.past = temp;
            temp.prev = tail;
            tail = temp;
        }
    }
    public Node<O> get(int index){
        Node<O> result = head;
        for (int i = 0; i < index; i++){
            result = result.past;
        }
        return result;
    }
}

class Node<T>{
    T data;
    Node<T> prev;
    Node<T> past;

    public Node(T data) {
        this.data = data;
        prev = null;
        past = null;
    }

}