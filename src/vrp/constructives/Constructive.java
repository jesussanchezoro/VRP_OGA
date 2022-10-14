package vrp.constructives;

import vrp.structure.VRPInstance;
import vrp.structure.VRPSolution;

public interface Constructive {

    public VRPSolution construct(VRPInstance instance);
}
