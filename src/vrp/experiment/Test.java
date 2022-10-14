package vrp.experiment;

import vrp.structure.VRPInstance;
import vrp.structure.VRPSolution;

public class Test {

    public static void main(String[] args) {
        String path = "../instances/vrp/A-n32-k5.vrp";
        VRPInstance instance = new VRPInstance(path);
        VRPSolution sol = new VRPSolution(instance);
        sol.addNodeToRoute(0, 26);
        sol.addNodeToRoute(0, 30);
        sol.addNodeToRoute(0, 16);
        sol.addNodeToRoute(0, 12);
        sol.addNodeToRoute(0, 1);
        sol.addNodeToRoute(0, 23);
        System.out.println(sol);
    }
}
