import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class RouteFinder {
    int nodes;
    int edges;
    Node[] nodeList;
    WEdge[] edgeList;
    int numberOfExtractedNodes = 0;

    public Node[] getNodeList() {
        return nodeList;
    }

    public WEdge[] getEdgeList() {
        return edgeList;
    }

    public void extractNodesFromFile(BufferedReader bufferedReader) throws IOException {
        StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());

        nodes = Integer.parseInt(stringTokenizer.nextToken());
        nodeList = new Node[nodes];

        for (int i = 0; i < nodes; i++) {
            stringTokenizer = new StringTokenizer(bufferedReader.readLine());
            int nodeNumber = Integer.parseInt(stringTokenizer.nextToken());
            double latitude = Double.parseDouble(stringTokenizer.nextToken()) * (Math.PI/180);
            double longitude = Double.parseDouble(stringTokenizer.nextToken()) * (Math.PI/180);

            nodeList[i] = new Node(nodeNumber, latitude, longitude);
        }
    }

    public void extractEdgesFromFile(BufferedReader br) throws IOException{
        StringTokenizer st = new StringTokenizer(br.readLine());
        edges = Integer.parseInt(st.nextToken());
        edgeList = new WEdge[edges];

        for(int i = 0; i < edges; i++) {
            st = new StringTokenizer(br.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int driveTime = Integer.parseInt(st.nextToken());
            int length = Integer.parseInt(st.nextToken());
            int speedLimit = Integer.parseInt(st.nextToken());

            WEdge wEdge = new WEdge(nodeList[to], nodeList[from].edge1, driveTime, length, speedLimit);
            nodeList[from].edge1 = wEdge;
        }
    }

    public void extractInterestPointsFromFile(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        int numberOfInterestPoints = Integer.parseInt(st.nextToken());

        for(int i = 0; i < numberOfInterestPoints; i++) {
            st = new StringTokenizer(br.readLine());
            int index = Integer.parseInt(st.nextToken());
            int type = Integer.parseInt(st.nextToken());
            String locationName = st.nextToken("").replace("\"","").trim();

            nodeList[index].type = type;
            nodeList[index].locationName = locationName;
        }
    }

    public void extractInformationFromFiles(BufferedReader nodes, BufferedReader edges, BufferedReader interestPoints) throws IOException{
        extractNodesFromFile(nodes);
        extractEdgesFromFile(edges);
        extractInterestPointsFromFile(interestPoints);
    }

    public void findShortestPath(Node node, WEdge edge, PriorityQueue<Node> priorityQueue) {
        Predecessor nd = (Predecessor)node.predecessor;
        Predecessor md = (Predecessor)edge.to.predecessor;

        if(md.distance > nd.distance + edge.driveTime) {
            md.distance = nd.distance + edge.driveTime;
            md.combinedHeuristicDistance = md.distance + md.heuristicDistance;
            md.predecessor = node;
            priorityQueue.remove(edge.to);
            priorityQueue.add(edge.to);
        }
    }

    public Node dijkstra(Node startNode, Node endNode) {
        numberOfExtractedNodes = 0;
        initializePredecessorDijkstra(startNode);
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(nodes, Comparator.comparingInt(a -> (((Predecessor) a.predecessor).distance)));
        priorityQueue.add(startNode);

        for(int i = nodes; i > 1; --i) {
            Node extractNode = priorityQueue.poll();
            numberOfExtractedNodes++;
            for (WEdge wEdge = (WEdge) extractNode.edge1; wEdge != null; wEdge = (WEdge) wEdge.next) {
                findShortestPath(extractNode, wEdge, priorityQueue);
            }
            if(extractNode == endNode) {
                return extractNode;
            }
        }
        System.out.println("Could not find a path to this location.");
        return null;
    }

    public Node[] dijkstraLocations(Node startNode, int type, int numberOfLocations) {
        Node[] locationNodes = new Node[numberOfLocations];
        int indexCounter = 0;
        initializePredecessorDijkstra(startNode);
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(nodes, Comparator.comparingInt(a -> (((Predecessor) a.predecessor).distance)));
        priorityQueue.add(startNode);

        while (!priorityQueue.isEmpty()) {
            Node extractNode = priorityQueue.poll();
            for (WEdge wEdge = (WEdge) extractNode.edge1; wEdge != null; wEdge = (WEdge) wEdge.next) {
                findShortestPath(extractNode, wEdge, priorityQueue);
            }
            if (extractNode.type == type || extractNode.type == 6) {
                locationNodes[indexCounter] = extractNode;
                indexCounter++;
            }
            if (indexCounter == numberOfLocations) {
                printLocations(locationNodes);
                return locationNodes;
            }
        }
        return null;
    }

    public Node aStar (Node startNode, Node endNode) {
        numberOfExtractedNodes = 0;
        initializePredecessorAstar(startNode, endNode);
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(nodes, (a, b) -> (((Predecessor)a.predecessor).combinedHeuristicDistance - ((Predecessor)b.predecessor).combinedHeuristicDistance));
        priorityQueue.add(startNode);

        for(int i = nodes; i > 1; --i) {
            Node extractNode = priorityQueue.poll();
            numberOfExtractedNodes++;
            for (WEdge wEdge = (WEdge) extractNode.edge1; wEdge != null; wEdge = (WEdge) wEdge.next) {
                findShortestPath(extractNode, wEdge, priorityQueue);
            }
            if(extractNode == endNode) {
                return extractNode;
            }
        }
        System.out.println("Could not find a path to this location.");
        return null;
    }

    public void printTrip(Node n) {
        for(Node node = n; node != null; node = ((Predecessor)node.predecessor).predecessor) {
            node.printCoordinates();
        }
    }

    public void printTripInformation(Node n) {
        double time = 0;
        int length = 0;

        for(Node node = n; node != null && ((Predecessor)node.predecessor).predecessor != null; node = ((Predecessor)node.predecessor).predecessor) {
            for(WEdge wEdge = (WEdge) ((Predecessor)node.predecessor).predecessor.edge1; wEdge != null; wEdge = (WEdge) wEdge.next) {
                if(wEdge.to == node) {
                    time += ((double) wEdge.length / (wEdge.speedLimit/3.6));
                    length += wEdge.length;
                    break;
                }
            }
        }
        double hours = time/3600;
        double minutes =  (time - (int)hours * 3600)/60;
        double seconds = time - ((int)hours * 3600) - ((int)minutes * 60);
        System.out.println("Expected time: " + (int)hours + " hours " + (int)minutes + " minutes " + (int)seconds + " seconds\nLength: " + length/1000 + " km.");
    }


    public void initializePredecessorDijkstra(Node startNode) {
        for(int i = nodes; i-- > 0;) {
            nodeList[i].predecessor = new Predecessor();
        }
        ((Predecessor)startNode.predecessor).distance = 0;
    }

    public void initializePredecessorAstar(Node startNode, Node endNode) {
        for(int i = nodes; i-- > 0;) {
            nodeList[i].predecessor = new Predecessor();
            ((Predecessor)nodeList[i].predecessor).heuristicDistance = estimateHaversineLength(nodeList[i], endNode);
        }

        ((Predecessor)startNode.predecessor).distance = 0;
    }

    public int estimateHaversineLength(Node node1, Node node2) {
        double sin_latitude = Math.sin((node1.latitude-node2.latitude)/2.0);
        double sin_longitude = Math.sin((node1.longitude-node2.longitude)/2.0);
        return (int) (35285538.46153846153846153846*Math.asin(Math.sqrt((sin_latitude*sin_latitude)+(Math.cos(node1.latitude)*Math.cos(node2.latitude)*sin_longitude*sin_longitude))));
    }

    public void printLocations(Node[] listOfLocations) {
        String name = "";
        for(int i = 0; i < listOfLocations.length; i++) {
            if(listOfLocations[i].type == 2) {
                name = "Gas station: ";
            }else if (listOfLocations[i].type == 4) {
                name = "Charging station: ";
            }else {
                name = "Combination: ";
            }
            System.out.println(name + listOfLocations[i].locationName);
            listOfLocations[i].printCoordinates();
        }
    }


    public Node findNode(int nodeNumber) {
        return nodeList[nodeNumber];
    }

    public int getNumberOfExtractedNodes() {
        return numberOfExtractedNodes;
    }

    static class Predecessor {
        int distance;
        Node predecessor;
        static int infinity = 1000000000;
        int heuristicDistance;
        int combinedHeuristicDistance;

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

    static class WEdge extends Edge {
        int driveTime;
        int length;
        int speedLimit;
        public WEdge(Node to, WEdge next, int driveTime, int length, int speedLimit) {
            super(to, next);
            this.driveTime = driveTime;
            this.length = length;
            this.speedLimit = speedLimit;
        }
    }
}

