import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

 class Edge {
    Edge next;
    Node to;

    public Edge(Node n, Edge e ) {
        this.to = n;
        this.next = e;
    }
}
 class Node {
    WEdge edge1;
    Object predecessor;
    int nodeIndex;

    public Node(int index) {
        this.nodeIndex = index;
    }
}

 class Predecessor {
    int distance;
    Node predecessor;
    static int infinity = 1000000000;

    public int findDistance() {
        return distance;
    }

    public Node findPredecessor() {
        return predecessor;
    }

    public Predecessor() {
        distance = infinity;
    }
}

 class WEdge extends Edge {
    int weight;
    public WEdge(Node to, WEdge next, int weight) {
        super(to, next);
        this.weight = weight;
    }
}

class Graph {
    int nodes;
    int edges;
    Node[] nodeList;
    PriorityQueue<Node> priorityQueue;

    public void newGraph(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer((br.readLine()));

        nodes = Integer.parseInt(st.nextToken());
        nodeList = new Node[nodes];

        for(int i = 0; i < nodes; i++) {
            nodeList[i] = new Node(i);
        }

        edges = Integer.parseInt(st.nextToken());
        for(int i = 0; i < edges; i++) {
            st = new StringTokenizer(br.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());

            WEdge edge = new WEdge(nodeList[to], (WEdge)nodeList[from].edge1, weight);
            nodeList[from].edge1 = edge;
        }
    }

    public void initializePredecessor(Node startNode) {
        for(int i = nodes; i-- > 0;) {
            nodeList[i].predecessor = new Predecessor();
        }
        ((Predecessor)startNode.predecessor).distance = 0;
    }

    public void findShortestPath(Node node, WEdge edge) {
        Predecessor nd = (Predecessor)node.predecessor;
        Predecessor md = (Predecessor)edge.to.predecessor;

        if(md.distance > nd.distance + edge.weight) {
            md.distance = nd.distance + edge.weight;
            md.predecessor = node;
            this.priorityQueue.remove(edge.to);
            this.priorityQueue.add(edge.to);
        }
    }

    public void dijkstra(Node startNode) {
        initializePredecessor(startNode);
        this.priorityQueue = new PriorityQueue<>(this.nodes, Comparator.comparingInt(a -> (((Predecessor) a.predecessor).distance)));
        this.priorityQueue.addAll(Arrays.asList(this.nodeList));

        for(int i = nodes; i > 1; --i) {
            Node extractNode = this.priorityQueue.poll();
            for (WEdge wEdge = (WEdge) extractNode.edge1; wEdge != null; wEdge = (WEdge) wEdge.next) {
                findShortestPath(extractNode, wEdge);
            }
        }
    }

    public void printRoads(){
        System.out.format("%-7s%-7s%-7s%n", "Node", "From", "Distance");

        for(int i = 0; i < nodes; i ++){

            String from;
            if(((Predecessor)nodeList[i].predecessor).distance == 0){
                from = "Start";
            }else if(((Predecessor)nodeList[i].predecessor).predecessor == null){
                from = "";
            }else{
                from = Integer.toString(((Predecessor)nodeList[i].predecessor).predecessor.nodeIndex);
            }

            String distance;
            if(((Predecessor)nodeList[i].predecessor).distance == 1000000000){
                distance = "can't reach";
            }else{
                distance = Integer.toString(((Predecessor)nodeList[i].predecessor).distance);
            }
            System.out.format("%-7s%-7s%-7s%n", i, from, distance);
        }
    }
}

public class DijkstraAlgorithm {


    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/vg1"));
        Graph graph = new Graph();

        graph.newGraph(bufferedReader);

        graph.dijkstra(graph.nodeList[1]);
        System.out.println("\nFrom graph file vg1 with start point at 1");
        graph.printRoads();


        bufferedReader = new BufferedReader(new FileReader("src/vg1"));
        graph = new Graph();

        graph.newGraph(bufferedReader);

        graph.dijkstra(graph.nodeList[0]);
        System.out.println("\nFrom graph file vg1 with start point at 0");
        graph.printRoads();

        bufferedReader = new BufferedReader(new FileReader("src/vg2"));
        graph = new Graph();

        graph.newGraph(bufferedReader);

        graph.dijkstra(graph.nodeList[7]);
        System.out.println("\nFrom graph file vg2 with start point at 7");
        graph.printRoads();
    }
}
