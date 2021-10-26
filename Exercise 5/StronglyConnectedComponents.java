import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.StringTokenizer;

public class StronglyConnectedComponents {
    private int nodes;
    private LinkedList<Integer> adjacency[];
    public static int numberOfComponents = 0;

    StronglyConnectedComponents(int nodes) {
        this.nodes = nodes;
        this.adjacency = new LinkedList[nodes];
        for(int i = 0; i < nodes; ++i) {
            adjacency[i] = new LinkedList<>();
        }
    }

    public void addEdge(int fromNode, int toNode) {
        adjacency[fromNode].add(toNode);
    }

    public void depthFirstSearch(int currentNode, boolean visited[]) {
        visited[currentNode] = true;
        System.out.print(currentNode + " ");

        int nextNode;

        Iterator<Integer> adjacent  = adjacency[currentNode].iterator();
        while(adjacent.hasNext()) {
            nextNode = adjacent.next();
            if(!visited[nextNode]) {
                depthFirstSearch(nextNode, visited);
            }
        }
    }

    public StronglyConnectedComponents getTranspose() {
        StronglyConnectedComponents graph = new StronglyConnectedComponents(nodes);
        for (int i = 0; i < nodes; i++) {
            Iterator<Integer> adjacent = adjacency[i].listIterator();
            while(adjacent.hasNext()) {
                graph.adjacency[adjacent.next()].add(i);
            }
        }
        return graph;
    }

    public void depthFirstSearchSortByTime(int currentNode, boolean visited[], Stack stack) {
        visited[currentNode] = true;

        Iterator<Integer> adjacent = adjacency[currentNode].iterator();
        while(adjacent.hasNext()) {
            int nextNode = adjacent.next();
            if(!visited[nextNode]) {
                depthFirstSearchSortByTime(nextNode, visited, stack);
            }
        }
        stack.push(currentNode);
    }

    public void printSCCs() {
        Stack stack = new Stack();
        boolean visited[] = new boolean[nodes];

        for(int i = 0; i < nodes; i++) {
            visited[i] = false;
        }

        for(int i = 0; i < nodes; i++) {
            if(visited[i] == false) {
                depthFirstSearchSortByTime(i, visited, stack);
            }
        }

        StronglyConnectedComponents graph = getTranspose();

        for(int i = 0; i < nodes; i++) {
            visited[i] = false;

        }
        while(stack.empty() == false) {
            int v = (int)stack.pop();

            if(visited[v] == false) {
                System.out.print(++numberOfComponents + "                 ");
                graph.depthFirstSearch(v, visited);
                System.out.println();
            }
        }
    }

    public static void main(String[] args) throws IOException{
        File file = new File("src/L7g2.txt");
        BufferedReader br1 = new BufferedReader(new FileReader(file));
        StringTokenizer st = new StringTokenizer(br1.readLine());
        int numberOfNodes = 0;
        int numberOfEdges = 0;
        numberOfNodes = Integer.parseInt(st.nextToken());
        numberOfEdges = Integer.parseInt(st.nextToken());

        StronglyConnectedComponents graph = new StronglyConnectedComponents(numberOfNodes);
        for(int i = 0; i < numberOfEdges; i++) {
            st = new StringTokenizer(br1.readLine());
            graph.addEdge(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        }

        if(numberOfNodes < 100) {
            System.out.println("Component:" + "        " + "Nodes in the component:");
            graph.printSCCs();
            System.out.println("\nThe graph has " + numberOfComponents + " Strongly connected component(s).");
        }
    }
}
