import java.util.Scanner;

public class DLL {
    int size;
    Node head;
    Node tail;

    public class Node {
        int number;
        Node next;
        Node previous;

        Node(int n, Node next, Node previous) {
            this.number = n;
            this.next = next;
            this.previous = previous;
            if(number >= 10)
                throw new IllegalArgumentException("Each Node can only contain a single digit. ");
        }
    }

    void insertionBeginning(DLL list, int newNumber) {
        Node newNode = new Node(newNumber, list.head, null);
        list.head = newNode;

        if(list.tail == null) {
            list.tail = newNode;
        }
        list.size++;
    }

    void insertionLast(DLL list, int newNumber) {
        Node newNode = new Node(newNumber, null, list.tail);

        if(list.tail != null) {
            list.tail.next = newNode;
        } else {
            list.head = newNode;
        }
        list.tail = newNode;
        list.size++;
    }

    void addTwoLists(DLL list1, DLL list2, DLL result) {
        Node first = list1.tail;
        Node second = list2.tail;
        int carry = 0;
        int sum = 0;

        while(first != null || second != null) {
            sum = carry + (first != null ? first.number : 0) + (second != null ? second.number : 0);
            carry = (sum >= 10 ? 1 : 0);
            sum = sum % 10;

            insertionBeginning(result, sum);

            if(first != null) {
                first = first.previous;
            }
            if(second != null) {
                second = second.previous;
            }
        }
        if(carry > 0) {
            insertionBeginning(result, carry);
        }
    }

    void subtractTwoLists(DLL list1, DLL list2, DLL result) {
        Node first = list1.tail;
        Node second = list2.tail;
        int sum = 0;
        int carry = 0;

        while(first != null || second != null) {

            if(first != null && second != null) {
                if(first.number + carry >= second.number) {
                    sum = first.number + carry - second.number;
                    carry = 0;
                } else {
                    sum = first.number + carry + 10 - second.number;
                    carry = -1;
                }
                first = first.previous;
                second = second.previous;
            }
            else if (first != null && second == null) {
                if(first.number >= 1) {
                    sum = (first.number) + carry;
                    carry = 0;
                } else {
                    if(carry != 0) {
                        sum = first.number;
                    }
                }
                first = first.previous;
            } else if (first == null && second != null) {
                System.out.println("ERROR: CAN NOT CALCULATE SUM WITH NEGATIVE ANSWER!");
            }
            insertionBeginning(result, sum);
        }
    }

    //Method for printing out the list in order from first to last node
    void printlist(DLL list) {
        Node node = list.head;
        while(node != null) {
            System.out.print(node.number);
            node = node.next;
        }
    }

    //Simple main method for putting values in each node for two separate list
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int counter = 0;

        DLL list = new DLL();
        DLL list2 = new DLL();
        DLL res = new DLL();

        System.out.println("\nEnter the digits to be added to the list: ");

        while(counter < 20) {

            list.insertionLast(list, sc.nextInt());
            counter++;
        }

        System.out.println("The first number: ");
        list.printlist(list);
        counter = 0;

        System.out.println("\nEnter the digits to be added to the second list: ");

        while(counter < 20) {

            list2.insertionLast(list2, sc.nextInt());
            counter++;
        }

        counter = 0;

        System.out.println("The second number: ");
        list2.printlist(list2);

        /*
        list.addTwoLists(list, list2, res);
        System.out.println("\nThe result from the addition: ");
        list.printlist(res);
         */

        list.subtractTwoLists(list, list2, res);
        System.out.println("\nThe result from the subtraction: ");
        list.printlist(res);
    }
}
