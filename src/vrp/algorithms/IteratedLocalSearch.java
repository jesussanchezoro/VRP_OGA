package vrp.algorithms;

import vrp.constructives.Constructive;
import vrp.localsearch.LocalSearch;
import vrp.structure.VRPInstance;
import vrp.structure.VRPSolution;
import vrp.utils.RandomManager;

import java.util.Random;

public class IteratedLocalSearch implements Algorithm {

    private Constructive constructive;
    private LocalSearch localSearch;
    private float perturbationSize;
    private int maxItersWithoutImprove;

    public IteratedLocalSearch(Constructive constructive, LocalSearch localSearch, float perturbationSize, int maxItersWithoutImprove) {
        this.constructive = constructive;
        this.localSearch = localSearch;
        this.perturbationSize = perturbationSize;
        this.maxItersWithoutImprove = maxItersWithoutImprove;
    }

    @Override
    public VRPSolution execute(VRPInstance instance) {
        VRPSolution sol = constructive.construct(instance);
        localSearch.improve(sol);
        int itersWithoutImprove = 0;
        VRPSolution localSol = new VRPSolution(sol);
        while (itersWithoutImprove < maxItersWithoutImprove) {
            perturbate(localSol);
            localSearch.improve(localSol);
            if (localSol.getOF() < sol.getOF()) {
                sol.copy(localSol);
                itersWithoutImprove = 0;
            } else {
                localSol.copy(sol);
                itersWithoutImprove++;
            }
        }
        return sol;
    }

    private void perturbate(VRPSolution sol) {
        int n = sol.getInstance().getN();
        int t = sol.getInstance().getTrucks();
        int nNodesToMove = (int) Math.ceil(perturbationSize * n);
        Random rnd = RandomManager.getRnd();
        for (int i = 0; i < nNodesToMove; i++) {
            int v = 1+rnd.nextInt(n-1);
            int r = rnd.nextInt(t);
            while (r == sol.getRouteForNode(v)) {
                r = rnd.nextInt(t);
            }
            sol.removeNodeFromRoute(sol.getRouteForNode(v), v);
            int bestPrev = sol.findBestPosition(r, v);
            sol.addAfterNode(r, v, bestPrev);
        }
    }
}
