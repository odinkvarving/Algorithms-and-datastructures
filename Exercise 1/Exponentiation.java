public class Exponentiation {

    public static void main(String[] args) {
        Long start;
        Long end;

        int n = 6000;

        //2.1-1 Testcode
        System.out.println(exponentiate(2, 10));
        System.out.println(exponentiate(3, 14));

        //2.1-1 Tests with different n:
        start = System.nanoTime();
        exponentiate(1.001, n);
        end = System.nanoTime();
        System.out.println("Time in milliseconds: " + (double) (end-start)/1000000 + "ms\n");

        //2.2-3 Testcode
        System.out.println(exponentiate2(2, 10));
        System.out.println(exponentiate2(3, 14));

        //2.2-3 Tests with different n:
        start = System.nanoTime();
        exponentiate2(1.001, n);
        end = System.nanoTime();
        System.out.println("Time in milliseconds: " + (double) (end-start)/1000000 + "ms\n");

        //Testcode with Java method Math.pow():
        System.out.println(Math.pow(2, 10));
        System.out.println(Math.pow(3, 14));

        //Math.pow with different n:
        start = System.nanoTime();
        Math.pow(1.001, n);
        end = System.nanoTime();
        System.out.println("Time in milliseconds: " + (double) (end-start)/1000000 + "ms\n");



    }
    public static double exponentiate(double x, int n){
        if(n == 0) {
            return 1.0;
        } else {
            return x * exponentiate(x, n-1);
        }
    }

    public static double exponentiate2(double x, int n){
        if(n == 0) {
            return 1.0;
        } else if (n % 2 == 0) {
            return exponentiate(x*x, n/2);
        } else {
            return x * exponentiate(x*x, (n-1)/2);
        }


    }
}
