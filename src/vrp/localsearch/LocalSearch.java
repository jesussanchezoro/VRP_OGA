package vrp.localsearch;

import vrp.structure.VRPSolution;

public interface LocalSearch {

    public void improve(VRPSolution sol);
}
