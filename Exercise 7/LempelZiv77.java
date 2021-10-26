import java.io.*;
import java.nio.charset.StandardCharsets;

public class LempelZiv77 {

    int lookAheadBuffer = 20;
    int searchBuffer = 32000;
    int windowSize = lookAheadBuffer + searchBuffer;

    String fileInput;

    Token[] tokens;

    public LempelZiv77(){

    }


    private void readUncompressedFile(String path) throws IOException {
        InputStream input = new FileInputStream(path);
        DataInputStream inputStream = new DataInputStream(input);
        int count = input.available();
        tokens = new Token[count];

        byte[] inputInBytes = new byte[count+1];
        inputStream.read(inputInBytes);
        fileInput = new String(inputInBytes, StandardCharsets.UTF_8);
    }

    private void createTokens(){
        boolean allTokensCreated = false;

        int tokenIndex = 0;
        int lookAheadBufferIndex = 0;
        int searchBufferIndex = lookAheadBufferIndex - searchBuffer;
        while(!allTokensCreated){
            int currentBestMatchOffset = 0;
            int currentBestLength = 0;

            for(int i = lookAheadBufferIndex-1; i > searchBufferIndex-1; i --){
                if(!(i < 0)) {
                    if (fileInput.charAt(lookAheadBufferIndex) == (fileInput.charAt(i))) {
                        int length = 1;
                        int offset = lookAheadBufferIndex - i;
                        boolean isNoMoreMatches = false;
                        while (!isNoMoreMatches) {
                            if (fileInput.charAt(lookAheadBufferIndex + length) == (fileInput.charAt(i + length))) {
                                length++;
                            } else {
                                if (currentBestLength < length) {
                                    currentBestMatchOffset = offset;
                                    currentBestLength = length;
                                }
                                isNoMoreMatches = true;
                            }
                        }
                    }
                }else{
                    break;
                }
            }
            lookAheadBufferIndex += currentBestLength + 1;
            searchBufferIndex += currentBestLength + 1;
            Token token = new Token(currentBestMatchOffset, currentBestLength, fileInput.charAt(lookAheadBufferIndex-1));
            tokens[tokenIndex] = token;
            tokenIndex++;
            if(lookAheadBufferIndex >= fileInput.length()-1){
                allTokensCreated = true;
            }
        }
    }

    public void compressFile(String path) throws IOException {
        readUncompressedFile(path);
        createTokens();

        FileOutputStream file = new FileOutputStream(path);
        DataOutputStream dataOutputStream = new DataOutputStream(file);

        for(int i = 0; i < tokens.length; i ++){
            if(tokens[i] != null) {
                dataOutputStream.writeShort(tokens[i].offset);
                dataOutputStream.writeShort(tokens[i].length);
                dataOutputStream.writeShort(tokens[i].nextChar);
            }
        }
        dataOutputStream.flush();
        dataOutputStream.close();
    }

    private void readCompressedFile(String path) throws IOException {
        InputStream input = new FileInputStream(path);
        DataInputStream dataInputStream = new DataInputStream(input);
        int count = input.available();
        System.out.println(count);
        tokens = new Token[(count/2)/3];
        for(int i = 0; i < (count/2)/3; i ++){
            int offset = dataInputStream.readShort();
            int length = dataInputStream.readShort();
            int character = dataInputStream.readShort();
            tokens[i] = new Token(offset, length, character);
        }
    }

    public void decompressFile(String path) throws IOException {
        readCompressedFile(path);

        String test = "";
        for(int i = 0; i < tokens.length; i ++){
            if(tokens[i] != null) {
                if (tokens[i].length == 0 && tokens[i].offset == 0) {
                    test += String.valueOf((char) tokens[i].nextChar);
                }else{
                    for(int j = 0; j < tokens[i].length; j ++){
                        test += test.charAt(test.length()-tokens[i].offset);
                    }
                    test += String.valueOf((char)tokens[i].nextChar);
                }
            }
        }

        FileWriter myFileWriter = new FileWriter(path);
        myFileWriter.write(test.substring(0, test.length()-1));
        myFileWriter.close();
    }
}
