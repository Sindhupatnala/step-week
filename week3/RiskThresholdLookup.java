import java.util.*;

public class RiskThresholdLookup {

    
    public static void linearSearch(int[] arr, int target) {
        int comparisons = 0;
        boolean found = false;

        long start = System.nanoTime();

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i] == target) {
                System.out.println("Linear Search: Found at index " + i);
                found = true;
                break;
            }
        }

        long end = System.nanoTime();

        if (!found) {
            System.out.println("Linear Search: Not found");
        }

        System.out.println("Comparisons: " + comparisons);
        System.out.println("Time (ns): " + (end - start));
        System.out.println();
    }


    public static int binarySearch(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        int comparisons = 0;

        long start = System.nanoTime();

        while (low <= high) {
            comparisons++;
            int mid = (low + high) / 2;

            if (arr[mid] == target) {
                long end = System.nanoTime();
                System.out.println("Binary Search: Found at index " + mid);
                System.out.println("Comparisons: " + comparisons);
                System.out.println("Time (ns): " + (end - start));
                System.out.println();
                return mid;
            } else if (arr[mid] < target) {
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

    // 🔹 Lower Bound (Insertion Point)
    public static int lowerBound(int[] arr, int target) {
        int low = 0, high = arr.length;

        while (low < high) {
            int mid = (low + high) / 2;

            if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        return low;
    }

    // 🔹 Floor (largest ≤ target)
    public static int findFloor(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        int floor = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid] == target) return arr[mid];

            if (arr[mid] < target) {
                floor = arr[mid];
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return floor;
    }

    // 🔹 Ceiling (smallest ≥ target)
    public static int findCeiling(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        int ceil = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid] == target) return arr[mid];

            if (arr[mid] > target) {
                ceil = arr[mid];
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return ceil;
    }

    // 🔹 Main Method
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Sample Input
        int[] risks = {10, 25, 50, 100};

        System.out.println("Original Risk Bands: " + Arrays.toString(risks));

        System.out.print("Enter target risk value: ");
        int target = sc.nextInt();

        // 🔸 Linear Search (unsorted)
        linearSearch(risks, target);

        // 🔸 Sort array for binary operations
        Arrays.sort(risks);
        System.out.println("Sorted Risk Bands: " + Arrays.toString(risks));
        System.out.println();

        // 🔸 Binary Search
        int index = binarySearch(risks, target);

        // 🔸 Lower Bound (Insertion Point)
        int insertionPoint = lowerBound(risks, target);
        System.out.println("Insertion Point (Lower Bound Index): " + insertionPoint);

        // 🔸 Floor & Ceiling
        int floor = findFloor(risks, target);
        int ceil = findCeiling(risks, target);

        System.out.println("Floor (≤ target): " + floor);
        System.out.println("Ceiling (≥ target): " + ceil);

        sc.close();
    }
}