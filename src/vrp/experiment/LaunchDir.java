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

import java.io.File;

public class LaunchDir {

    public static void main(String[] args) {
        RandomManager.init(1309);

        Constructive c = new ConstGreedy();
        LocalSearch ls = new LocalSearchBalance();
//        Algorithm ils = new IteratedLocalSearch(c, ls, 0.25f, 5);
        Algorithm ils = new ParallelIteratedLocalSearch(c, ls, 0.2f, 10);

        String path = "../instances/vrp";
        String[] fileNames = new File(path).list((dir, name) -> name.endsWith(".vrp"));
        for (String fileName : fileNames) {
            VRPInstance instance = new VRPInstance(path+"/"+fileName);
            System.out.print(fileName+"\t");
            long tIni = System.currentTimeMillis();
            VRPSolution sol = ils.execute(instance);
            double secs = (System.currentTimeMillis() - tIni) / 1000.0;
            System.out.println(sol.getOF()+"\t"+secs);
        }
    }
}
