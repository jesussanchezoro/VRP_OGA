package vrp.structure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class VRPInstance {

    private String name;
    private int n;
    private int trucks;
    private int[][] distance;
    private int[] demand;

    public VRPInstance(String path) {
        readInstance(path);
    }


    public int getN() {
        return n;
    }

    public int getTrucks() {
        return trucks;
    }

    public int getDemand(int v) {
        return demand[v];
    }

    public int distance(int i, int j) {
        return distance[i][j];
    }

    private void readInstance(String path) {
        try (BufferedReader bf = new BufferedReader(new FileReader(path))) {
            name = path.substring(path.lastIndexOf('/')+1);
            String line = bf.readLine();
            while (!line.startsWith("NAME")) {
                line = bf.readLine();
            }
            trucks = Integer.parseInt(line.split("k")[1]);
            while (!line.startsWith("DIMENSION")) {
                line = bf.readLine();
            }
            n = Integer.parseInt(line.split(" : ")[1]);
            while (!line.startsWith("NODE_COORD_SECTION")) {
                line = bf.readLine();
            }
            int[][] coords = new int[n][2];
            for (int i = 0; i < n; i++) {
                line = bf.readLine();
                String[] tokens = line.trim().split("\\s+");
                coords[i][0] = Integer.parseInt(tokens[1]);
                coords[i][1] = Integer.parseInt(tokens[2]);
            }
            evalDistances(coords);
            while (!line.startsWith("DEMAND_SECTION")) {
                line = bf.readLine();
            }
            demand = new int[n];
            for (int i = 0; i < n; i++) {
                demand[i] = Integer.parseInt(bf.readLine().trim().split("\\s+")[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void evalDistances(int[][] coords) {
        distance = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                int d = (int) Math.sqrt(Math.pow(coords[j][0]-coords[i][0],2) + Math.pow(coords[j][1]-coords[i][1],2));
                distance[i][j] = d;
                distance[j][i] = d;
            }
        }
    }
}
