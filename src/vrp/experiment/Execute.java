package vrp.experiment;

import vrp.algorithms.Algorithm;
import vrp.algorithms.IteratedLocalSearch;
import vrp.algorithms.ParallelIteratedLocalSearch;
import vrp.constructives.ConstGreedy;
import vrp.constructives.Constructive;
import vrp.localsearch.LocalSearch;
import vrp.localsearch.LocalSearchBalance;
import vrp.structure.VRPInstance;
import vrp.structure.VRPSolution;
import vrp.utils.RandomManager;

import java.util.Random;

public class Execute {

    public static void main(String[] args) {
        RandomManager.init(1309);
        String path = "../instances/vrp/A-n80-k10.vrp"; //A-n80-k10.vrp
        VRPInstance instance = new VRPInstance(path);
        Constructive c = new ConstGreedy();
        VRPSolution sol = c.construct(instance);
        System.out.println("CONSTRUCTIVE");
        System.out.println(sol);
        System.out.println("FEASIBLE: "+sol.isFeasible());

        System.out.println("LOCAL SEARCH");
        LocalSearch ls = new LocalSearchBalance();
        ls.improve(sol);
        System.out.println(sol);
        System.out.println("FEASIBLE: "+sol.isFeasible());

        System.out.println("ILS");
        Algorithm ils = new IteratedLocalSearch(c, ls, 0.2f, 10);
//        Algorithm ils = new ParallelIteratedLocalSearch(c, ls, 0.2f, 10);
        VRPSolution ilsSol = ils.execute(instance);
        System.out.println(ilsSol);

    }
}
