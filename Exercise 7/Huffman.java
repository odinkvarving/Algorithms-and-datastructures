import java.io.*;
import java.util.Arrays;

public class Huffman {

    int[] frequencyTable = new int[256];
    int characterAmount;
    BitString[] bitStringsTable = new BitString[256];

    byte[] byteArray;


    public Huffman() {}

    public void compressFile(String path) throws IOException {
        readNewFile(path);
        characterAmount = countCharacters();
        generateHuffmanTable();
        byte[] bytesToBeWritten = createCompressedBytes();

        FileOutputStream fileOutputStream = new FileOutputStream(path);
        DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);


        for(int i = 0; i < frequencyTable.length; i ++){
            dataOutputStream.writeInt(frequencyTable[i]);
        }
        dataOutputStream.write(bytesToBeWritten);
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    public void decompressFile(String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(path);
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        byte[] compressedData;

        for(int i = 0; i < frequencyTable.length; i ++){
            frequencyTable[i] = dataInputStream.readInt();
        }

        int bytesLeft = fileInputStream.available();
        compressedData = new byte[bytesLeft];
        dataInputStream.read(compressedData);

        characterAmount = countCharacters();
        Heap huffmanTree = createHuffmanTree();
        byte[] decompressedText = new byte[Arrays.stream(frequencyTable).sum()];

        int byteIndexCounter = 0;

        Node root = huffmanTree.node[0];
        for(int i = 0; i < compressedData.length - 1; i ++){
            int bits;
            if(i == compressedData.length - 2){
                bits = compressedData[compressedData.length-1]-1;
            }else{
                bits = 7;
            }
            for(int b = bits; b > -1; b --){
                int bit = ((compressedData[i] + 256) >> b) & (0b00000001);
                if(bit == 0){
                    root = root.left;
                    if(root.left == null && root.right == null){
                        decompressedText[byteIndexCounter] = (byte) root.character;
                        byteIndexCounter++;
                        root = huffmanTree.node[0];
                    }
                }else{
                    root = root.right;
                    if(root.left == null && root.right == null){
                        decompressedText[byteIndexCounter] = (byte) root.character;
                        byteIndexCounter++;
                        root = huffmanTree.node[0];
                    }
                }
            }
        }

        FileOutputStream output = new FileOutputStream(path);
        DataOutputStream outputStream = new DataOutputStream(output);
        outputStream.write(decompressedText);
        outputStream.flush();
        outputStream.close();
    }


    private byte[] createCompressedBytes(){
        double bitLength = 0;
        for(int i = 0; i < byteArray.length; i ++){
            bitLength += bitStringsTable[byteArray[i] & 0xff].bitsAmount;
        }

        int byteSize;
        if((bitLength/8) % 1 == 0){
            byteSize = (int) (bitLength/8);
        }else{
            byteSize = (int) (bitLength/8) + 1;
        }

        byte[] byteToBeSent = new byte[byteSize+1];

        int byteCounter = 0;
        int spaceLeft = 8;
        int lastByteCounter = 0;

        for(int i = 0; i < byteArray.length; i ++) {
            BitString bitString = bitStringsTable[byteArray[i] & 0xff];
            for (int b = bitString.bitsAmount - 1; b > -1; b--) {
                byte bitToAdd = (byte) ((bitString.bitString >> b) & 1);
                if (spaceLeft == 0) {
                    byteCounter++;
                    byteToBeSent[byteCounter] = bitToAdd;
                    spaceLeft = 8;
                } else {
                    byteToBeSent[byteCounter] = (byte) (byteToBeSent[byteCounter] << 1);
                    byteToBeSent[byteCounter] |= bitToAdd;
                }
                if(byteCounter == byteSize - 1){
                    lastByteCounter ++;
                }
                spaceLeft--;
            }
        }

        byteToBeSent[byteToBeSent.length-1] = (byte) lastByteCounter;
        return byteToBeSent;
    }

    private Heap createHuffmanTree(){
        Heap huffmanHeap = fillHeap();

        while(huffmanHeap.length != 1){
            Node node1 = huffmanHeap.getMin();
            Node node2 = huffmanHeap.getMin();
            Node newNode = new Node(0, node1.occurrences + node2.occurrences);
            newNode.left = node1;
            newNode.right = node2;
            huffmanHeap.addNode(newNode);
        }
        return huffmanHeap;
    }

    private void generateHuffmanTable(){
        Heap huffmanHeap = createHuffmanTree();
        generateHuffmanTable(huffmanHeap.node[0], "");
    }


    private void generateHuffmanTable(Node root, String s){
        if(root.left == null && root.right == null){
            System.out.println((char) root.character + ":" + s);
            bitStringsTable[root.character] = new BitString(s.length(), root.character, Long.parseLong(s, 2));
            return;
        }

        generateHuffmanTable(root.left, s + "0");
        generateHuffmanTable(root.right, s + "1");
    }


    private void readNewFile(String path) throws IOException {
        resetFrequencyArray();

        InputStream input = new FileInputStream(path);
        DataInputStream dataInputStream = new DataInputStream(input);
        int count = input.available();
        byteArray = new byte[count];

        dataInputStream.read(byteArray);
        for(byte b : byteArray){
            frequencyTable[b & 0xff]++;
        }
        printFrequencyArray();
    }

    private Heap fillHeap(){
        Heap heap = new Heap(characterAmount);
        int counter = 0;
        for(int i = 0; i < frequencyTable.length; i ++){
            if(frequencyTable[i] != 0){
                Node node = new Node(i, frequencyTable[i]);
                heap.node[counter] = node;
                counter++;
            }
        }
        heap.createHeap();
        return heap;
    }

    private int countCharacters(){
        int chars = 0;
        for(int i = 0; i < frequencyTable.length; i ++){
            if(frequencyTable[i] != 0){
                chars++;
            }
        }
        return chars;
    }

    public void printFrequencyArray(){
        System.out.format("%-8s%-8s%n", "Char", "Occurrences");
        for(int i = 0; i < frequencyTable.length; i ++){
            if(frequencyTable[i] != 0){
                System.out.format("%-8s%-8s%n", (char)i, frequencyTable[i]);
            }
        }
    }

    private void resetFrequencyArray(){
        frequencyTable = new int[256];
    }

}