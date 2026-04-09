import java.util.Random;

class Asset {
    String name;
    double returnRate;
    double volatility;

    Asset(String name, double returnRate, double volatility) {
        this.name = name;
        this.returnRate = returnRate;
        this.volatility = volatility;
    }

    @Override
    public String toString() {
        return name + ":" + returnRate + "% (Vol:" + volatility + ")";
    }
}

public class PortfolioSorting {

    // 🔸 Merge Sort (Stable, Ascending by returnRate)
    public static void mergeSort(Asset[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            merge(arr, left, mid, right);
        }
    }

    private static void merge(Asset[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Asset[] L = new Asset[n1];
        Asset[] R = new Asset[n2];

        for (int i = 0; i < n1; i++) L[i] = arr[left + i];
        for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            // Stable condition
            if (L[i].returnRate <= R[j].returnRate) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }

        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    // 🔸 Quick Sort (DESC returnRate, ASC volatility)
    public static void quickSort(Asset[] arr, int low, int high) {
        if (high - low < 10) { // Hybrid optimization
            insertionSort(arr, low, high);
            return;
        }

        if (low < high) {
            int pivotIndex = medianOfThree(arr, low, high);
            swap(arr, pivotIndex, high);

            int pi = partition(arr, low, high);

            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    // 🔸 Partition
    private static int partition(Asset[] arr, int low, int high) {
        Asset pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (compare(arr[j], pivot)) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }

    // 🔸 Comparison logic
    private static boolean compare(Asset a, Asset b) {
        if (a.returnRate > b.returnRate) return true;
        if (a.returnRate == b.returnRate)
            return a.volatility < b.volatility;
        return false;
    }

    // 🔸 Median-of-3 Pivot
    private static int medianOfThree(Asset[] arr, int low, int high) {
        int mid = (low + high) / 2;

        if (arr[low].returnRate > arr[mid].returnRate) swap(arr, low, mid);
        if (arr[low].returnRate > arr[high].returnRate) swap(arr, low, high);
        if (arr[mid].returnRate > arr[high].returnRate) swap(arr, mid, high);

        return mid;
    }

    // 🔸 Random Pivot (alternative)
    private static int randomPivot(int low, int high) {
        return new Random().nextInt(high - low + 1) + low;
    }

    // 🔸 Insertion Sort (for small partitions)
    private static void insertionSort(Asset[] arr, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            Asset key = arr[i];
            int j = i - 1;

            while (j >= low && compare(key, arr[j])) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }
    }

    // 🔸 Swap
    private static void swap(Asset[] arr, int i, int j) {
        Asset temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // 🔸 Utility
    public static void printArray(Asset[] arr) {
        for (Asset a : arr) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    // 🔸 Main
    public static void main(String[] args) {
        Asset[] assets = {
            new Asset("AAPL", 12, 5),
            new Asset("TSLA", 8, 9),
            new Asset("GOOG", 15, 4)
        };

        Asset[] mergeArr = assets.clone();
        Asset[] quickArr = assets.clone();

        // Merge Sort
        mergeSort(mergeArr, 0, mergeArr.length - 1);
        System.out.println("Merge Sort (Ascending):");
        printArray(mergeArr);

        // Quick Sort
        quickSort(quickArr, 0, quickArr.length - 1);
        System.out.println("\nQuick Sort (Descending):");
        printArray(quickArr);
    }
}