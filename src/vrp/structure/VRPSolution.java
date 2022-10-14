package vrp.structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VRPSolution {

    private VRPInstance instance;
    private int[][][] routes;
    private int[] demand;
    private int[] distance;
    private int[] routeForNode;

    public VRPSolution(VRPInstance instance) {
        this.instance = instance;
        int t = instance.getTrucks();
        int n = instance.getN();
        this.routes = new int[t][n][2];
        for (int i = 0; i < t; i++) {
            for (int j = 1; j < n; j++) {
                this.routes[i][j][0] = -1;
                this.routes[i][j][1] = -1;
            }
        }
        this.demand = new int[t];
        this.distance = new int[t];
        routeForNode = new int[n];
        Arrays.fill(routeForNode, -1);
    }

    public VRPSolution(VRPSolution sol) {
        copy(sol);
    }

    public void copy(VRPSolution sol) {
        this.instance = sol.instance;
        int t = instance.getTrucks();
        int n = instance.getN();
        this.routes = new int[t][n][2];
        for (int r = 0; r < t; r++) {
            for (int v = 0; v < n; v++) {
                this.routes[r][v][0] = sol.routes[r][v][0];
                this.routes[r][v][1] = sol.routes[r][v][1];
            }
        }
        demand = sol.demand.clone();
        distance = sol.distance.clone();
        routeForNode = sol.routeForNode.clone();
    }

    public VRPInstance getInstance() {
        return instance;
    }

    public void addNodeToRoute(int route, int node) {
        int last = this.routes[route][0][0]; // 0 - Prev, 1 - Next
        distance[route] = distance[route] - instance.distance(last,0);
        this.routes[route][last][1] = node;
        this.routes[route][node][0] = last;
        this.routes[route][node][1] = 0;
        this.routes[route][0][0] = node;
        this.demand[route] += instance.getDemand(node);
        distance[route] = distance[route] + (instance.distance(last, node) + instance.distance(node, 0));
        routeForNode[node] = route;
    }

    public int getLastNodeInRoute(int route) {
        return this.routes[route][0][0]; // Node before depot
    }

    public int getDistance(int route) {
        return distance[route];
    }

    public int getDemand(int route) {
        return demand[route];
    }

    public int getOF() {
        int max = 0;
        int min = 0x3f3f3f;
        for (int j : demand) {
            max = Math.max(j, max);
            min = Math.min(j, min);
        }
        return max-min;
    }

    public int worstRoute() {
        int worst = -1;
        int max = 0;
        for (int i = 0; i < demand.length; i++) {
            if (demand[i] > max) {
                max = demand[i];
                worst = i;
            }
        }
        return worst;
    }

    public List<Integer> findNodesInRoute(int r) {
        List<Integer> nodes = new ArrayList<>(instance.getN());
        int next = routes[r][0][1];
        while (next != 0) {
            nodes.add(next);
            next = routes[r][next][1];
        }
        return nodes;
    }

    public int findBestPosition(int r, int v) {
        int bestPrev = 0;
        int next = routes[r][0][1];
        int prev = 0;
        int minDist = instance.distance(0, v) + instance.distance(v, next) - instance.distance(0, next);
        while (next != 0) {
            int dist = instance.distance(prev, v) + instance.distance(v, next) - instance.distance(prev, next);
            if (dist < minDist) {
                minDist = dist;
                bestPrev = prev;
            }
            prev = next;
            next = routes[r][next][1];
        }
        return bestPrev;
    }

    public void addAfterNode(int r, int v, int prev) {
        int next = routes[r][prev][1];
        routes[r][v][0] = prev;
        routes[r][v][1] = next;
        routes[r][prev][1] = v;
        routes[r][next][0] = v;
        distance[r] = distance[r] - instance.distance(prev, next) + instance.distance(prev, v) + instance.distance(v, next);
        demand[r] += instance.getDemand(v);
        routeForNode[v] = r;
    }

    public int getRouteForNode(int v) {
        return routeForNode[v];
    }

    public void removeNodeFromRoute(int route, int node) {
        int prev = this.routes[route][node][0];
        int next = this.routes[route][node][1];
        this.routes[route][prev][1] = next;
        this.routes[route][next][0] = prev;
        this.routes[route][node][0] = -1;
        this.routes[route][node][1] = -1;
        distance[route] = distance[route] - (instance.distance(prev, node) + instance.distance(node, next)) + instance.distance(prev, next);
        demand[route] -= instance.getDemand(node);
        routeForNode[node] = -1;
    }

    public int getNext(int route, int node) {
        return this.routes[route][node][1];
    }

    public int getPrev(int route, int node) {
        return this.routes[route][node][0];
    }

    public boolean isFeasible() {
        for (int i = 1; i < routeForNode.length; i++) {
            if (routeForNode[i] < 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        int totalDist = 0;
        for (int r = 0; r < routes.length; r++) {
            int next = routes[r][0][1];
            stb.append("0");
            while (next != 0) {
                stb.append(" -> ").append(next);
                next = routes[r][next][1];
            }
            stb.append("\n");
            stb.append("Demand: ").append(demand[r]).append("\n");
            stb.append("Distance: ").append(distance[r]).append("\n");
            totalDist += distance[r];
        }
        stb.append("Total distance: ").append(totalDist).append("\n");
        stb.append("Balance: ").append(getOF()).append("\n");
        return stb.toString();
    }
}
