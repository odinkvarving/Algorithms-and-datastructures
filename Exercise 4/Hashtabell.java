import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Hashtabell {
    HashNode[] hashTable;

    public Hashtabell(int tableSize) {
        hashTable = new HashNode[tableSize];
    }

    static class HashNode {
        String key;
        int unicodeValue;
        HashNode next;

        HashNode(String key, int unicodeValue) {
            this.key = key;
            this.unicodeValue = unicodeValue;
        }

        public String toString() {
            return key + " - Unicode: " + unicodeValue;
        }
    }

    public ArrayList<String> extractNamesFromFile(String filePath) {
        try {
            File myFile = new File(filePath);
            Scanner sc = new Scanner(myFile);
            ArrayList<String> nameList = new ArrayList<>();
            while (sc.hasNextLine()) {
                String name = sc.nextLine();
                name = name.replaceAll(",", " ");
                nameList.add(name);
                }

            sc.close();
            return nameList;
        } catch (FileNotFoundException e) {
            System.out.println("An error has occured");
            e.printStackTrace();
            return null;
        }
    }

    //Method for extracting a hashed value from a given string - multiplied by i+1 to get unique value
    private int convertStringToHashNumber(String hashString) {
        int data = 0;
        for(int i = 0; i < hashString.length(); i++) {
            data += ((int) hashString.charAt(i) * (i+1));
        }
        return data;
    }

    //Method for getting an index based on the remainder from hashed number % size of table
    private int calculateIndexFromRestDivision(String key, int tableSize) {
        return convertStringToHashNumber(key) % tableSize;
    }

    //Node inserted if index is empty, if not - a chained link is created between the collision-node and the new node
    private boolean insertIntoHashTable(String key) {
        boolean collision = false;
        int unicodeValue = calculateIndexFromRestDivision(key, hashTable.length);
        HashNode node = new HashNode(key, unicodeValue);
        if(hashTable[unicodeValue] == null) {
            hashTable[unicodeValue] = node;
        } else {
            System.out.println("Node: " + node.key + " has collided with node: " + hashTable[unicodeValue].key + ". Linking to existing node...");
            collision = true;
            HashNode current = hashTable[unicodeValue];
            hashTable[unicodeValue] = node;
            node.next = current;
        }
        return collision;
    }

    //Implements the insertIntoHashTable-method on every string from the given ArrayList
    public int addHashNodesFromArray(ArrayList<String> nameList) {
        int numberOfCollisions = 0;
        for (String s : nameList) {
            if(insertIntoHashTable(s)) {
                numberOfCollisions++;
            }
        }
        return numberOfCollisions;
    }

    //Prints null if the index is empty - if not, prints the node at the index - and any nodes linked to this node
    public void printHashTable(){
        Arrays.stream(hashTable).forEach(hashNode -> {
            if(hashNode == null || hashNode.next == null){
                System.out.println(hashNode);
            }else{
                HashNode n = hashNode;
                while(n != null){
                    System.out.print(n + " | ");
                    n = n.next;
                }
                System.out.println();
            }
        });
    }

    //Uses the calculateIndexFromRestDivision-method to decide the index of the name, and checks if this corresponds with a name in the table
    public HashNode findNameInTable(String name) {
        int index = calculateIndexFromRestDivision(name, this.hashTable.length);

        if(hashTable[index] == null) {
            return null;
        } else if (hashTable[index].key.equalsIgnoreCase(name)) {
            return hashTable[index];
        } else {
            HashNode node = hashTable[index];
            while(node != null && !node.key.equalsIgnoreCase(name)) {
                node = node.next;
            }
            return node;
        }
    }

    //Method for finding empty indexes
    public int findNotNullIndexes(Hashtabell table) {
        int numberOfFilledIndexes = 0;
        for(int i = 0; i < table.hashTable.length; i++) {
            if(hashTable[i] != null && hashTable[i].next != null) {
                numberOfFilledIndexes++;
            }
        }
        return numberOfFilledIndexes;
    }

    public static void main(String[] args) {
        Hashtabell table = new Hashtabell(109);
        ArrayList<String> names = table.extractNamesFromFile("src/names.txt");

        int numberOfCollisions = table.addHashNodesFromArray(names);
        double loadFactor = (1.0) * table.findNotNullIndexes(table) / table.hashTable.length;
        double averageCollisions =  ((double)numberOfCollisions / names.size());

        table.printHashTable();

        System.out.println("Node found for the given name: " + table.findNameInTable("Odin Kvarving"));
        System.out.println("Total amount of collisions: " + numberOfCollisions + ", Loadfactor: " + loadFactor);
        System.out.println("The average amount of collision per name was: " + averageCollisions);

    }
}
