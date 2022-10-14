package vrp.constructives;

import vrp.structure.VRPInstance;
import vrp.structure.VRPSolution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConstGreedy implements Constructive {

    @Override
    public VRPSolution construct(VRPInstance instance) {
        VRPSolution sol = new VRPSolution(instance);
        Set<Integer> used = new HashSet<>();
        used.add(0);
        int n = instance.getN();
        while (used.size() < n) {
            addNextNode(sol, used);
        }
        return sol;
    }

    private void addNextNode(VRPSolution sol, Set<Integer> used) {
        VRPInstance instance = sol.getInstance();
        int n = instance.getN();
        int t = instance.getTrucks();
        int selNode = -1;
        int selRoute = -1;
        int selCost = 0x3f3f3f;
        for (int i = 1; i < n; i++) {
            if (used.contains(i)) continue;
            int minCost = 0x3f3f3f;
            int bestRoute = -1;
            for (int r = 0; r < t; r++) {
                int last = sol.getLastNodeInRoute(r);
                int cost = sol.getDistance(r) * sol.getDemand(r) + (instance.distance(last, i) + instance.distance(i, 0) - instance.distance(last, 0)) * instance.getDemand(i);
                if (cost < minCost) {
                    minCost = cost;
                    bestRoute = r;
                }
            }
            if (minCost < selCost) {
                selCost = minCost;
                selRoute = bestRoute;
                selNode  = i;
            }
        }
        used.add(selNode);
        sol.addNodeToRoute(selRoute, selNode);
    }

}
