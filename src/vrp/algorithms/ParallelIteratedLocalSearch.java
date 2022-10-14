package vrp.algorithms;

import vrp.constructives.Constructive;
import vrp.localsearch.LocalSearch;
import vrp.structure.VRPInstance;
import vrp.structure.VRPSolution;
import vrp.utils.RandomManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class ParallelIteratedLocalSearch implements Algorithm {

    private static final int NTHREADS = 8;

    private Constructive constructive;
    private LocalSearch localSearch;
    private float perturbationSize;
    private int maxItersWithoutImprove;

    public ParallelIteratedLocalSearch(Constructive constructive, LocalSearch localSearch, float perturbationSize, int maxItersWithoutImprove) {
        this.constructive = constructive;
        this.localSearch = localSearch;
        this.perturbationSize = perturbationSize;
        this.maxItersWithoutImprove = maxItersWithoutImprove;
    }

    @Override
    public VRPSolution execute(VRPInstance instance) {
        ExecutorService pool = Executors.newCachedThreadPool();
        VRPSolution sol = constructive.construct(instance);
        localSearch.improve(sol);
        int itersWithoutImprove = 0;
        while (itersWithoutImprove < maxItersWithoutImprove) {
            List<Future<VRPSolution>> sols = new ArrayList<>();
            for (int i = 0; i < NTHREADS; i++) {
                VRPSolution localSol = new VRPSolution(sol);
                Future<VRPSolution> futSol = pool.submit(() -> {
                    perturbate(localSol);
                    localSearch.improve(localSol);
                    return localSol;
                });
                sols.add(futSol);
            }
            boolean improve = false;
            for (Future<VRPSolution> futSol : sols) {
                try {
                    VRPSolution localSol = futSol.get();
                    if (localSol.getOF() < sol.getOF()) {
                        sol.copy(localSol);
                        improve = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (improve) {
                itersWithoutImprove = 0;
            } else {
                itersWithoutImprove++;
            }
        }
        pool.shutdown();
        try {
            pool.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
