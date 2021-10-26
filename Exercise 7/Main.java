import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        LempelZiv77 lempelZiv77 = new LempelZiv77();
        Huffman huffman = new Huffman();

        //Path til fil som skal komprimeres/dekomprimeres
        String fileName = "src/diverse.txt";


        //lempelZiv77.compressFile(fileName);
        //huffman.compressFile(fileName);


        //huffman.decompressFile(fileName);
        //lempelZiv77.decompressFile(fileName);

    }
}
