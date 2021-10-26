public class Token {

    int offset;
    int length;
    int nextChar;

    public Token(int offset, int length, int nextChar) {
        this.offset = offset;
        this.length = length;
        this.nextChar = nextChar;
    }
}
