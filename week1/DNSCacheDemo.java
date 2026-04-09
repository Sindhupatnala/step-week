import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long timestamp;
    long expiryTime;

    public DNSEntry(String domain, String ipAddress, long ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.timestamp = System.currentTimeMillis();
        this.expiryTime = this.timestamp + (ttlSeconds * 1000);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

class DNSCache {

    private final int maxSize;
    private long hits = 0;
    private long misses = 0;

    // LinkedHashMap for LRU (accessOrder = true)
    private final LinkedHashMap<String, DNSEntry> cache;

    public DNSCache(int maxSize) {
        this.maxSize = maxSize;

        this.cache = new LinkedHashMap<String, DNSEntry>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNSCache.this.maxSize;
            }
        };

        startCleanupThread();
    }

    // Resolve domain
    public synchronized String resolve(String domain) {

        long startTime = System.nanoTime();

        if (cache.containsKey(domain)) {
            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                long time = (System.nanoTime() - startTime) / 1_000_000;
                System.out.println("Cache HIT → " + entry.ipAddress + 
                                   " (Retrieved in " + time + " ms)");
                return entry.ipAddress;
            } else {
                System.out.println("Cache EXPIRED → Querying upstream...");
                cache.remove(domain);
            }
        }

        // Cache miss
        misses++;
        System.out.println("Cache MISS → Querying upstream...");

        String newIP = queryUpstreamDNS(domain);
        DNSEntry newEntry = new DNSEntry(domain, newIP, 10); // TTL 10 sec for demo
        cache.put(domain, newEntry);

        return newIP;
    }

    // Simulate upstream DNS query (100ms delay)
    private String queryUpstreamDNS(String domain) {
        try {
            Thread.sleep(100); // simulate network delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Fake IP generator
        return "172.217.14." + new Random().nextInt(255);
    }

    // Background cleanup thread
    private void startCleanupThread() {
        Thread cleaner = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000); // run every 5 seconds
                    synchronized (this) {
                        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<String, DNSEntry> entry = it.next();
                            if (entry.getValue().isExpired()) {
                                it.remove();
                                System.out.println("Removed expired entry: " + entry.getKey());
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        cleaner.setDaemon(true);
        cleaner.start();
    }

    // Cache statistics
    public void getCacheStats() {
        long total = hits + misses;
        double hitRate = total == 0 ? 0 : (hits * 100.0) / total;

        System.out.println("Cache Stats:");
        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Rate: " + String.format("%.2f", hitRate) + "%");
    }
}

public class DNSCacheDemo {

    public static void main(String[] args) throws InterruptedException {

        DNSCache cache = new DNSCache(3);

        cache.resolve("google.com");   // MISS
        cache.resolve("google.com");   // HIT

        Thread.sleep(11000);           // wait for TTL to expire

        cache.resolve("google.com");   // EXPIRED → MISS

        cache.resolve("facebook.com");
        cache.resolve("amazon.com");
        cache.resolve("openai.com");   // LRU eviction if size > 3

        cache.getCacheStats();
    }
}