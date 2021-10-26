import java.util.*;

public class BinaryTree {

    Node root;

    BinaryTree() {
        root = null;
    }

    class Node {
        String key;

        Node leftChild;
        Node rightChild;

        Node(String key) {
            this.key = key;
            this.rightChild = null;
            this.leftChild = null;

        }

        public String toString() {
            return key;
        }
    }

    void insert(String key) {
        root = insertRec(root, key);
    }

    /* A recursive function to insert a new key in BST */
    Node insertRec(Node root, String key) {

        /* If the tree is empty, return a new node */
        if (root == null) {
            root = new Node(key);
            return root;
        }

        /* Otherwise, recur down the tree */
        if (key.compareToIgnoreCase(root.key) < 0)
            root.leftChild = insertRec(root.leftChild, key);
        else if (key.compareToIgnoreCase(root.key) > 0)
            root.rightChild = insertRec(root.rightChild, key);

        /* return the (unchanged) node pointer */
        return root;
    }

    public void inOrderTraverseTree(Node focusNode) {
        if (focusNode != null) {
            inOrderTraverseTree(focusNode.leftChild);
            System.out.println(focusNode);

            inOrderTraverseTree(focusNode.rightChild);
        }
    }

    String str = "";

    void printLevelOrder() {
        int h = height(root);
        int i;
        for (i = 1; i <= h; i++) {
            if (i == 1) {
                str = "";
                for (int j = 0; j < 64; j++) {
                    str += " ";
                }
                printGivenLevel(root, i);
                System.out.print(str);
            } else if (i == 2) {
                str = "";
                for (int j = 0; j < 32; j++) {
                    str += " ";
                }
                printGivenLevel(root, i);
                System.out.print(str);
            } else if (i == 3) {
                str = "";
                for (int j = 0; j < 15; j++) {
                    str += " ";
                }
                printGivenLevel(root, i);
                System.out.print(str);
            } else if (i == 4) {
                str = "";
                for (int j = 0; j < 4; j++) {
                    str += " ";
                }
                System.out.print(str += str);
                printGivenLevel(root, i);
            }
            System.out.println("");
        }
    }

    /* Compute the "height" of a tree -- the number of
    nodes along the longest path from the root node
    down to the farthest leaf node.*/
    int height(Node root) {
        if (root == null)
            return 0;
        else {
            /* compute  height of each subtree */
            int lheight = height(root.leftChild);
            int rheight = height(root.rightChild);

            /* use the larger one */
            if (lheight > rheight)
                return (lheight + 1);
            else return (rheight + 1);
        }
    }

    /* Print nodes at the given level */
    void printGivenLevel(Node root, int level) {
        if (root == null) {
            System.out.print(str + str);
            return;
        }
        if (level == 1)
            System.out.print(str + root.key + str);
        else if (level > 1) {
            printGivenLevel(root.leftChild, level - 1);
            printGivenLevel(root.rightChild, level - 1);
        }
    }

    public static void main(String[] args) {
        BinaryTree theTree = new BinaryTree();

        //Test: hode, ben, albue, arm, hake, legg, tå, tann
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the root node:");
        String rotnode = sc.nextLine();
        theTree.insert(rotnode);

        String str;
        do {
            System.out.println("Enter a word: ('stop' to finish adding)");
            str = sc.nextLine();
            if (!str.equals("stop")) {
                theTree.insert(str);
            }
            theTree.printLevelOrder();
            theTree.inOrderTraverseTree(theTree.root);
        } while (!str.equals("stop"));
        System.out.println("\n" + "\n" + "\n");
        theTree.printLevelOrder();

    }
}


