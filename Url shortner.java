✅ 1. Requirements Gathering
📌 Functional Requirements:
Shorten a URL: Convert a long URL to a short, unique URL.

Redirect to Original URL: Given a short URL, redirect to the original URL.

Custom URL (Optional): Allow users to provide a custom short URL.

URL Expiry (Optional): Provide expiration dates for short URLs.

Analytics (Optional): Track click counts, user location, etc.

📌 Non-Functional Requirements:
Scalability: Support high read/write operations (e.g., millions of URLs per day).

Availability: High uptime, low latency.

Security: Prevent malicious URLs, handle DDoS attacks.

Consistency: Ensure URLs are correctly mapped.

✅ 2. Identifying Core Components
📦 Classes
URLShortener

URLMapping

Database

Encoder/Decoder (Hashing)

Logger

Analytics (Optional)

✅ 3. Define Class Responsibilities
📌 URLShortener (Controller)
Manages URL creation and retrieval.

Uses Encoder/Decoder for generating short URLs.

Interfaces with the database.

java
Copy
Edit
public class URLShortener {
    private static final String DOMAIN = "https://short.ly/";
    private static final int URL_LENGTH = 6;
    private EncoderDecoder encoderDecoder;
    private Database database;

    public URLShortener() {
        this.encoderDecoder = new EncoderDecoder();
        this.database = new InMemoryDatabase();
    }

    public String shortenURL(String longURL) {
        String shortURL = encoderDecoder.encode(longURL);
        database.saveURLMapping(shortURL, longURL);
        return DOMAIN + shortURL;
    }

    public String getOriginalURL(String shortURL) {
        String key = shortURL.replace(DOMAIN, "");
        return database.getLongURL(key);
    }
}
📌 URLMapping (Model)
Represents a URL mapping between short URL and long URL.

java
Copy
Edit
public class URLMapping {
    private String shortURL;
    private String longURL;

    public URLMapping(String shortURL, String longURL) {
        this.shortURL = shortURL;
        this.longURL = longURL;
    }

    public String getShortURL() {
        return shortURL;
    }

    public String getLongURL() {
        return longURL;
    }
}
📌 Database (Storage Layer)
In-memory Database (for simplicity)

Uses HashMap to store URL mappings.


import java.util.HashMap;
import java.util.Map;

public class InMemoryDatabase implements Database {
    private final Map<String, String> urlMap = new HashMap<>();

    @Override
    public void saveURLMapping(String shortURL, String longURL) {
        urlMap.put(shortURL, longURL);
    }

    @Override
    public String getLongURL(String shortURL) {
        return urlMap.get(shortURL);
    }
}
📌 Database Interface (for extensibility)
java
Copy
Edit
public interface Database {
    void saveURLMapping(String shortURL, String longURL);
    String getLongURL(String shortURL);
}
📌 Encoder/Decoder (Hashing/Encoding URL)
Generates short URLs using Base62 Encoding.


public class EncoderDecoder {
    public String encode(String longURL) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(longURL.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getUrlEncoder().encodeToString(hash);
            return encoded.substring(0, 6); // Return first 6 characters
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}

public class URLShortenerDemo {
    public static void main(String[] args) {
        URLShortener urlShortener = new URLShortener();
        
        String longURL = "https://www.example.com/articles/design-url-shortener-java";
        
        // Shorten URL
        String shortURL = urlShortener.shortenURL(longURL);
        System.out.println("Short URL: " + shortURL);

        // Retrieve original URL
        String retrievedURL = urlShortener.getOriginalURL(shortURL);
        System.out.println("Retrieved URL: " + retrievedURL);
    }
}
 4. Key Considerations
📌 Scalability
Use Distributed Databases (e.g., Cassandra, DynamoDB) instead of a single in-memory database.

Partition data by hashing URLs and distributing across multiple nodes.

📌 Performance
Caching: Frequently accessed URLs should be cached for faster retrieval (e.g., Redis, Memcached).

Load Balancing: Handle high request rates by using load balancers (Nginx, Apigee).

📌 Security
Rate Limiting: Prevent abuse by limiting URL shortening requests.

Blacklist Filtering: Prevent malicious URLs from being shortened.

Authorization: Restrict URL shortening to authenticated users (if required).

📌 Unique URL Generation
Instead of hashing, you can use:

Counter-based approach: Increment a counter and convert it to Base62 (e.g., 000001, 000002, ...).

UUIDs: Generate unique IDs using UUID algorithms.

📌 Database Schema (If Using SQL)
sql
Copy
Edit
CREATE TABLE url_mapping (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    short_url VARCHAR(10) UNIQUE,
    long_url TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
 5. Key Points to Discuss in an Interview
Scalability: How to handle billions of URLs efficiently.

Data Persistence: Storing data across distributed databases.

Fault Tolerance: Handling failures and retries.

URL Uniqueness: How to ensure URLs are unique.

Analytics: How to track URL clicks, user info, etc.

Caching & Optimization: Improving performance using in-memory databases like Redis.
