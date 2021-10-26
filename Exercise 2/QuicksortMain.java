import java.util.Random;

public class QuicksortMain {


    static int[] generateTable(int j){
        Random r = new Random();
        int[] table = new int[j];
        for(int i = 0; i < table.length; i++) {
            table[i] = r.nextInt(1000000);
        }
        return table;
    }

    static int[] generateDuplicatesTable(int j){
        Random r = new Random();
        int[] table = new int[j];
        for(int i = 0; i < table.length; i++) {
            if(i % 2 == 0) {
                table[i] = 62;
            } else
                table[i] = r.nextInt(1000000);
        }
        return table;
    }

    public static void main(String[] args) {
        int[] arr = generateDuplicatesTable(100000000);
        int sumBefore = 0;
        int sumAfter = 0;
        boolean sorted = false;
        Long start;
        Long end;
        Quicksort quicksort = new Quicksort();
        DualPivotQuicksort dualPivotQuicksort = new DualPivotQuicksort();

        //Regular quicksort:
        for(int i = 0; i < arr.length; i++) {
            sumBefore += arr[i];
        }

        int[] single = arr.clone();

        //For-loop for filling the established array with sorted numbers from 1 to the size of the array and finding the sum
        /*
        for(int i = 0; i < single.length; i++) {
            single[i] = i + 1;
            sumBefore += single[i];
        }

         */

        System.out.println("Sum of unsorted list: " + sumBefore);
        sumBefore = 0;

        start = System.nanoTime();
        quicksort.quicksort(single, 0, arr.length - 1);
        end = System.nanoTime();

        System.out.println("Time in milliseconds: " + (double) (end-start) / 1000000 + "ms");

        for(int i = 0; i < single.length; i++) {
            sumAfter += single[i];
        }

        System.out.println("Sum of sorted list: " + sumAfter);
        sumAfter = 0;

        //For-loop for printing out the array, commented out for the bigger arrays
        /*System.out.print("Sorted array: ");
        for (int i = 0; i < single.length; i++)
            System.out.print(single[i] + " ");

         */

        for(int i = 0; i < single.length - 2; i++) {
            if(single[i] <= single[i + 1]) {
                sorted = true;
            }
            else sorted = false;
        }
        if(sorted) {
            System.out.println("\nThe table is sorted correctly. ");
        } else System.out.println("\nThe table is not sorted correctly. ");

        //Dual-pivot quicksort:
        for(int i = 0; i < arr.length; i++) {
            sumBefore += arr[i];
        }

        int[] dual = arr.clone();

        //For-loop for filling the established array with sorted numbers from 1 to the size of the array and finding the sum
        /*
        for(int i = 0; i < dual.length; i++) {
            dual[i] = i + 1;
            sumBefore += dual[i];
        }

         */
        System.out.println("\nSum of unsorted list: " + sumBefore);
        sumBefore = 0;

        start = System.nanoTime();
        dualPivotQuicksort.dualPivotQuickSort(dual, 0, dual.length - 1);
        end = System.nanoTime();
        System.out.println("Time in milliseconds: " + (double) (end-start) / 1000000 + "ms");

        for(int i = 0; i < dual.length; i++) {
            sumAfter += dual[i];
        }

        System.out.println("Sum of sorted list: " + sumAfter);
        sumAfter = 0;

        //For-loop for printing out the array, commented out for the bigger arrays
        /*System.out.print("Sorted array: ");
        for (int i = 0; i < dual.length; i++)
            System.out.print(dual[i] + " ");

         */

        for(int i = 0; i < dual.length - 2; i++) {
            if(dual[i] <= dual[i + 1]) {
                sorted = true;
            }
            else sorted = false;
        }
        if(sorted) {
            System.out.println("\nThe table is sorted correctly. ");
        } else System.out.println("\nThe table is not sorted correctly. ");
    }
}

