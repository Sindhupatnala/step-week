import java.util.*;

class PlagiarismDetector {

    private int N; // n-gram size
    private Map<String, Set<String>> ngramIndex; // ngram -> documentIds
    private Map<String, List<String>> documentNgrams; // docId -> list of ngrams

    public PlagiarismDetector(int n) {
        this.N = n;
        this.ngramIndex = new HashMap<>();
        this.documentNgrams = new HashMap<>();
    }

    // Add document to database
    public void addDocument(String docId, String content) {
        List<String> ngrams = generateNgrams(content);
        documentNgrams.put(docId, ngrams);

        for (String ngram : ngrams) {
            ngramIndex
                .computeIfAbsent(ngram, k -> new HashSet<>())
                .add(docId);
        }
    }

    // Analyze new document
    public void analyzeDocument(String docId, String content) {

        List<String> newNgrams = generateNgrams(content);
        Map<String, Integer> matchCount = new HashMap<>();

        for (String ngram : newNgrams) {
            if (ngramIndex.containsKey(ngram)) {
                for (String existingDoc : ngramIndex.get(ngram)) {
                    matchCount.put(existingDoc,
                            matchCount.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + newNgrams.size() + " n-grams");

        for (String doc : matchCount.keySet()) {
            int matches = matchCount.get(doc);
            double similarity =
                    (matches * 100.0) / newNgrams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with \"" + doc + "\"");

            System.out.printf("Similarity: %.2f%%", similarity);

            if (similarity > 60) {
                System.out.println("  (PLAGIARISM DETECTED)");
            } else if (similarity > 15) {
                System.out.println("  (Suspicious)");
            } else {
                System.out.println();
            }
        }
    }

    // Generate n-grams
    private List<String> generateNgrams(String content) {
        String[] words = content.toLowerCase().split("\\s+");
        List<String> ngrams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }
            ngrams.add(sb.toString().trim());
        }

        return ngrams;
    }
}

public class PlagiarismSystemDemo {

    public static void main(String[] args) {

        PlagiarismDetector detector = new PlagiarismDetector(5);

        // Add existing essays
        detector.addDocument("essay_089.txt",
                "Data structures and algorithms are important in computer science education");

        detector.addDocument("essay_092.txt",
                "Machine learning and artificial intelligence are transforming technology");

        // Analyze new submission
        detector.analyzeDocument("essay_123.txt",
                "Machine learning and artificial intelligence are transforming technology rapidly");
    }
}