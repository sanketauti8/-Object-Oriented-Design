✅ 1. Requirements Gathering (Clarify Requirements)
Functional Requirements:
Distribute incoming requests across multiple servers.

Support various load balancing algorithms (Round Robin, Least Connections, Random, etc.).

Handle health checks of servers to detect failures.

Provide logging and monitoring of traffic.

Support scaling by adding or removing servers dynamically.

Non-Functional Requirements:
Scalability: Should handle increasing traffic smoothly.

Reliability: Should detect unhealthy servers and avoid routing traffic to them.

High Availability: Should ensure uptime even during failures.

Performance: Low latency in routing requests.

✅ 2. Identifying Classes (Components)
Core Classes:
LoadBalancer

Server

Request

HealthChecker

Logger

✅ 3. Define Class Responsibilities
📌 LoadBalancer
Attributes:

servers: List[Server]

algorithm: LoadBalancingAlgorithm

Methods:

add_server(server: Server)

remove_server(server_id: str)

route_request(request: Request) -> Server

check_health()

log_request(request: Request, server: Server)

📌 Server
Attributes:

id: str

ip_address: str

status: ServerStatus (e.g., ACTIVE, INACTIVE)

connections: int

Methods:

handle_request(request: Request)

update_status(status: ServerStatus)

get_connections() -> int

📌 Request
Attributes:

id: str

data: Any

Methods:

get_request_data()

📌 HealthChecker
Attributes:

check_interval: int

Methods:

perform_health_check(servers: List[Server])

📌 Logger
Methods:

log(message: str)

📌 LoadBalancingAlgorithm (Interface)
Methods:

select_server(servers: List[Server]) -> Server

📌 RoundRobinAlgorithm (Implementation of LoadBalancingAlgorithm)
Attributes:

index: int

Methods:

select_server(servers: List[Server]) -> Server

📌 LeastConnectionsAlgorithm (Implementation of LoadBalancingAlgorithm)
Methods:

select_server(servers: List[Server]) -> Server

✅ 4. Define Relationships (UML Diagram Explanation)
LoadBalancer manages multiple Server instances.

LoadBalancer uses a strategy pattern via LoadBalancingAlgorithm (Round Robin, Least Connections, etc.).

HealthChecker monitors the health of all servers periodically.

Logger logs all incoming requests and their routing history.

Request is sent to the LoadBalancer and routed to a Server.




import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// --------------------- Server Class ---------------------
class Server {
    private String id;
    private String ipAddress;
    private int connections;
    private boolean active;

    public Server(String id, String ipAddress) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.connections = 0;
        this.active = true;
    }

    public String getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getConnections() {
        return connections;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void handleRequest() {
        connections++;
        System.out.println("Request handled by server: " + id);
    }
}

// --------------------- LoadBalancingAlgorithm Interface ---------------------
interface LoadBalancingAlgorithm {
    Server selectServer(List<Server> servers);
}

// --------------------- RoundRobinAlgorithm Class ---------------------
class RoundRobinAlgorithm implements LoadBalancingAlgorithm {
    private int index = 0;

    @Override
    public Server selectServer(List<Server> servers) {
        if (servers.isEmpty()) {
            throw new IllegalStateException("No available servers.");
        }
        
        Server server = servers.get(index % servers.size());
        index = (index + 1) % servers.size();
        return server;
    }
}

// --------------------- LeastConnectionsAlgorithm Class ---------------------
class LeastConnectionsAlgorithm implements LoadBalancingAlgorithm {

    @Override
    public Server selectServer(List<Server> servers) {
        if (servers.isEmpty()) {
            throw new IllegalStateException("No available servers.");
        }

        Server leastConnectedServer = servers.get(0);
        for (Server server : servers) {
            if (server.getConnections() < leastConnectedServer.getConnections()) {
                leastConnectedServer = server;
            }
        }
        return leastConnectedServer;
    }
}

// --------------------- LoadBalancer Class ---------------------
class LoadBalancer {
    private List<Server> servers;
    private LoadBalancingAlgorithm algorithm;

    public LoadBalancer(LoadBalancingAlgorithm algorithm) {
        this.servers = new ArrayList<>();
        this.algorithm = algorithm;
    }

    public void addServer(Server server) {
        servers.add(server);
    }

    public void removeServer(String serverId) {
        servers.removeIf(server -> server.getId().equals(serverId));
    }

    public void routeRequest() {
        List<Server> activeServers = new ArrayList<>();
        for (Server server : servers) {
            if (server.isActive()) {
                activeServers.add(server);
            }
        }

        if (activeServers.isEmpty()) {
            throw new IllegalStateException("No active servers available.");
        }

        Server selectedServer = algorithm.selectServer(activeServers);
        selectedServer.handleRequest();
    }
}

// --------------------- Main Class ---------------------
public class LoadBalancerDemo {
    public static void main(String[] args) {
        Server server1 = new Server("1", "192.168.1.1");
        Server server2 = new Server("2", "192.168.1.2");
        Server server3 = new Server("3", "192.168.1.3");

        LoadBalancer loadBalancer = new LoadBalancer(new RoundRobinAlgorithm());
        loadBalancer.addServer(server1);
        loadBalancer.addServer(server2);
        loadBalancer.addServer(server3);

        // Simulate requests
        for (int i = 0; i < 10; i++) {
            loadBalancer.routeRequest();
        }

        System.out.println("\nSwitching to Least Connections Algorithm\n");

        LoadBalancer leastConnectionLoadBalancer = new LoadBalancer(new LeastConnectionsAlgorithm());
        leastConnectionLoadBalancer.addServer(server1);
        leastConnectionLoadBalancer.addServer(server2);
        leastConnectionLoadBalancer.addServer(server3);

        // Simulate requests
        for (int i = 0; i < 10; i++) {
            leastConnectionLoadBalancer.routeRequest();
        }
    }
}
