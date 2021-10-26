import java.util.HashSet;
import java.util.Random;

public class DoubleHashTable {
    Integer[] hashTable;
    private long start = 0;
    private long end = 0;
    private long time = 0;

    public DoubleHashTable(int tableSize) {
        this.hashTable = new Integer[tableSize];
    }


    public int firstHashIntegerValue(int key) {
       return key % hashTable.length;
    }

    public int secondHashIntegerValue(int key) {
        return (452953 - (key % 452953));
    }

    public boolean insertIntoHashTable(int key) {
        boolean collision = false;
        int index = firstHashIntegerValue(key);

        if(hashTable[index] != null) {
            collision = true;
            int index2 = secondHashIntegerValue(key);

            while(true) {
                //Tips i oppgaven: bruk pos = (pos+h2) % m
                index = (index + index2) % hashTable.length;
                if(hashTable[index] == null) {
                    hashTable[index] = key;
                    break;
                }
            }
        } else {
            hashTable[index] = key;
        }
        return collision;
    }

    public Integer[] generateIntegerTable() {
        Integer[] numbers = new Integer[10000000];
        Random random = new Random();

        for(int i = 0; i < numbers.length; i++) {
            int number = random.nextInt(100000000) + 200000000;
            numbers[i] = number;
        }
        return numbers;
    }

    public int insertRandomNumbersInHashTable() {
        Integer[] numbers = generateIntegerTable();
        int collisions = 0;

        start = System.nanoTime();
        for (int i : numbers) {
            if(insertIntoHashTable(i)) {
                collisions++;
            }
        }
        end = System.nanoTime();

        time = (end - start) / 1000000;
        System.out.println("Amount of time used for hashing: " + time + " milliseconds");

        return collisions;
    }

    public long insertRandomNumbersInHashMap() {
        Integer[] numbers = generateIntegerTable();
        HashSet<Integer> hashMap = new HashSet<>();
        start = System.nanoTime();
        for (int i : numbers) {
            hashMap.add(i);
        }
        end = System.nanoTime();
        return (end - start) / 1000000;
    }

    public int findNotNullIndexes(DoubleHashTable table) {
        int numberOfFilledIndexes = 0;
        for(int i = 0; i < table.hashTable.length; i++) {
            if(hashTable[i] != null) {
                numberOfFilledIndexes++;
            }
        }
        return numberOfFilledIndexes;
    }

    public static void main(String[] args) {
        DoubleHashTable hashTable = new DoubleHashTable(13000001);

        System.out.println("Amount of time used by HashMap for hashing: " + hashTable.insertRandomNumbersInHashMap() + " milliseconds");
        System.out.println("Number of collisions: " + hashTable.insertRandomNumbersInHashTable());
        double loadFactor = (1.0) * hashTable.findNotNullIndexes(hashTable) / hashTable.hashTable.length;
        System.out.print("Loadfactor: " + loadFactor);
    }
}
