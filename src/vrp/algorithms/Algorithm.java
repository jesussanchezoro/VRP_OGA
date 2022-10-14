package vrp.algorithms;

import vrp.structure.VRPInstance;
import vrp.structure.VRPSolution;

public interface Algorithm {

    public VRPSolution execute(VRPInstance instance);
}
