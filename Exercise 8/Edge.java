public class Edge {
    Node to;
    Edge next;

    public Edge(Node to, Edge next) {
        this.to = to;
        this.next = next;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    public Edge getNext() {
        return next;
    }

    public void setNext(Edge next) {
        this.next = next;
    }
}
