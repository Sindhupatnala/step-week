import java.util.*;

public class AccountIDLookup {

    public static void linearSearch(String[] logs, String target) {
        int first = -1, last = -1;
        int comparisons = 0;

        long start = System.nanoTime();

        for (int i = 0; i < logs.length; i++) {
            comparisons++;
            if (logs[i].equals(target)) {
                if (first == -1) first = i;
                last = i;
            }
        }

        long end = System.nanoTime();

        if (first == -1) {
            System.out.println("Linear Search: Not found");
        } else {
            System.out.println("Linear Search: First = " + first + ", Last = " + last);
        }

        System.out.println("Comparisons: " + comparisons);
        System.out.println("Time (ns): " + (end - start));
        System.out.println();
    }


    public static int binarySearch(String[] logs, String target) {
        int low = 0, high = logs.length - 1;
        int comparisons = 0;

        long start = System.nanoTime();

        while (low <= high) {
            comparisons++;
            int mid = (low + high) / 2;

            int cmp = logs[mid].compareTo(target);

            if (cmp == 0) {
                long end = System.nanoTime();
                System.out.println("Binary Search: Found at index " + mid);
                System.out.println("Comparisons: " + comparisons);
                System.out.println("Time (ns): " + (end - start));
                System.out.println();
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        long end = System.nanoTime();

        System.out.println("Binary Search: Not found");
        System.out.println("Comparisons: " + comparisons);
        System.out.println("Time (ns): " + (end - start));
        System.out.println();

        return -1;
    }

    public static int findFirst(String[] logs, String target) {
        int low = 0, high = logs.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (logs[mid].equals(target)) {
                result = mid;
                high = mid - 1; // move left
            } else if (logs[mid].compareTo(target) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return result;
    }

    public static int findLast(String[] logs, String target) {
        int low = 0, high = logs.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (logs[mid].equals(target)) {
                result = mid;
                low = mid + 1; // move right
            } else if (logs[mid].compareTo(target) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return result;
    }


    public static int countOccurrences(String[] logs, String target) {
        int first = findFirst(logs, target);
        if (first == -1) return 0;

        int last = findLast(logs, target);
        return last - first + 1;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);


        String[] logs = {"accB", "accA", "accB", "accC"};

        System.out.println("Original Logs: " + Arrays.toString(logs));

        System.out.print("Enter account ID to search: ");
        String target = sc.nextLine();

        linearSearch(logs, target);

        Arrays.sort(logs); 
        System.out.println("Sorted Logs: " + Arrays.toString(logs));
        System.out.println();

        int index = binarySearch(logs, target);

        int first = findFirst(logs, target);
        int last = findLast(logs, target);
        int count = countOccurrences(logs, target);

        if (first != -1) {
            System.out.println("First Occurrence (Binary): " + first);
            System.out.println("Last Occurrence (Binary): " + last);
            System.out.println("Total Count: " + count);
        } else {
            System.out.println("No occurrences found.");
        }

        sc.close();
    }
}