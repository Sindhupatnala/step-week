import java.util.*;

class FlashSaleManager {

    // productId -> stock
    private HashMap<String, Integer> inventory;

    // productId -> waiting list (FIFO)
    private HashMap<String, Queue<Integer>> waitingList;

    public FlashSaleManager() {
        inventory = new HashMap<>();
        waitingList = new HashMap<>();
    }

    // Add product with stock
    public void addProduct(String productId, int stock) {
        inventory.put(productId, stock);
        waitingList.put(productId, new LinkedList<>());
    }

    // Check stock instantly (O1)
    public synchronized String checkStock(String productId) {
        int stock = inventory.getOrDefault(productId, 0);
        return stock + " units available";
    }

    // Purchase item safely
    public synchronized String purchaseItem(String productId, int userId) {

        if (!inventory.containsKey(productId)) {
            return "Product not found";
        }

        int stock = inventory.get(productId);

        // If stock available
        if (stock > 0) {
            inventory.put(productId, stock - 1);
            return "Success, " + (stock - 1) + " units remaining";
        }

        // Add to waiting list
        Queue<Integer> queue = waitingList.get(productId);
        queue.add(userId);

        return "Added to waiting list, position #" + queue.size();
    }

    // Show waiting list
    public void showWaitingList(String productId) {
        Queue<Integer> queue = waitingList.get(productId);
        System.out.println("Waiting List: " + queue);
    }

    // Demo
    public static void main(String[] args) {

        FlashSaleManager manager = new FlashSaleManager();

        manager.addProduct("IPHONE15_256GB", 3);

        System.out.println(manager.checkStock("IPHONE15_256GB"));

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 11111));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 22222)); // waiting

        manager.showWaitingList("IPHONE15_256GB");
    }
}