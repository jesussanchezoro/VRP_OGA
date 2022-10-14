package vrp.localsearch;

import vrp.structure.VRPInstance;
import vrp.structure.VRPSolution;
import vrp.utils.RandomManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalSearchBalance implements LocalSearch {

    @Override
    public void improve(VRPSolution sol) {
        boolean improve = true;
        while (improve) {
            improve = tryImprove(sol);
        }
    }

    private boolean tryImprove(VRPSolution sol) {
        VRPInstance instance = sol.getInstance();
        int wr = sol.worstRoute();
        List<Integer> candidates = sol.findNodesInRoute(wr);
        int t = sol.getInstance().getTrucks();
        List<Integer> candRoutes = new ArrayList<>(t);
        for (int i = 0; i < t; i++) {
            if (wr != i) {
                candRoutes.add(i);
            }
        }
        Collections.shuffle(candidates, RandomManager.getRnd());
        Collections.shuffle(candRoutes, RandomManager.getRnd());
        for (int cv : candidates) {
            for (int cr : candRoutes) {
                if (sol.getDemand(cr) + instance.getDemand(cv) < sol.getDemand(wr)) {
                    sol.removeNodeFromRoute(wr, cv);
                    int bestPrev = sol.findBestPosition(cr, cv);
                    sol.addAfterNode(cr, cv, bestPrev);
                    return true;
                }
            }
        }
        return false;
    }
}
