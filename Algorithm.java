import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Algorithm {
    public static List<XYPair> pairs = new ArrayList<>();
    private static final Random r = new Random();

    public static void main(String[] args) throws IOException {
        RunSDO.createFile("datapairsSDO.txt");
        RunSDO.loadFile("datapairsSDO.txt");

        algorithmKMeans(3, 50);
        algorithmKMeans(5, 50);
        algorithmKMeans(7, 50);
        algorithmKMeans(9, 50);
        algorithmKMeans(11, 50);
        algorithmKMeans(13, 50);
    }

    private static void algorithmKMeans(int M, int runs) throws IOException {
        System.out.println("\nK-means algorithm => M = " + M);

        double bestError = Double.MAX_VALUE;
        double[][] bestCenters = new double[M][2];

        for (int i = 0; i < runs; i++) {
            double[][] centers = initializeCenters(M);
            double error = kMEANS(centers);

            System.out.printf("Run %d => SSE = %f\n", i + 1, error);

            if (error < bestError) {
                bestError = error;
                copyCenters(centers, bestCenters);
            }
        }

        // Αποθήκευση καλύτερων κέντρων
        try (FileWriter fw = new FileWriter("best_centers_" + M + ".txt")) {
            for (double[] c : bestCenters) {
                fw.write(c[0] + " " + c[1] + "\n");
            }
        }

        // Αποθήκευση καλύτερου σφάλματος
        try (FileWriter fw = new FileWriter("best_error_" + M + ".txt")) {
            fw.write(String.format("Best SSE = %f\n", bestError));
        }

        System.out.printf("Best SSE => (M = %d) : %f\n", M, bestError);
    }

    private static double[][] initializeCenters(int M) {
        double[][] centers = new double[M][2];
        Set<Integer> picks = new HashSet<>();
        for (int i = 0; i < M; i++) {
            int idx;
            do {
                idx = r.nextInt(pairs.size());
            } while (picks.contains(idx));

            picks.add(idx);
            centers[i][0] = pairs.get(idx).x;
            centers[i][1] = pairs.get(idx).y;
        }
        return centers;
    }

    private static double kMEANS(double[][] centers) {
        int M = centers.length, N = pairs.size();

        int[] assignedCluster = new int[N];
        Arrays.fill(assignedCluster, -1);

        boolean changed;
        do {//Looped execution of algorithm
            changed = false;

            // Assign points to closest center
            for (int i = 0; i < N; i++) {
                XYPair p = pairs.get(i);
                int bestCluster = 0;
                double bestDist = distance(p, centers[0]);

                for (int k = 1; k < M; k++) {
                    double d = distance(p, centers[k]);
                    if (d < bestDist) {
                        bestDist = d;
                        bestCluster = k;
                    }
                }

                if (assignedCluster[i] != bestCluster) {
                    assignedCluster[i] = bestCluster;
                    changed = true;
                }
            }

            // Compute new centers
            double[][] sum = new double[M][2];
            int[] count = new int[M];

            for (int i = 0; i < N; i++) {
                int k = assignedCluster[i];
                XYPair p = pairs.get(i);
                sum[k][0] += p.x;
                sum[k][1] += p.y;
                count[k]++;
            }

            // If empty cluster, choose a random point as new center
            for (int k = 0; k < M; k++) {
                if (count[k] == 0) {
                    XYPair p = pairs.get(r.nextInt(N));
                    centers[k][0] = p.x;
                    centers[k][1] = p.y;
                    count[k] = 1;
                    sum[k][0] = p.x;
                    sum[k][1] = p.y;
                    changed = true;
                }
            }

            // Update centers
            for (int k = 0; k < M; k++) {
                centers[k][0] = sum[k][0] / count[k];
                centers[k][1] = sum[k][1] / count[k];
            }

        } while (changed);

        // Calculate SSE
        double sse = 0;
        for (int i = 0; i < N; i++) {
            XYPair p = pairs.get(i);
            int k = assignedCluster[i];

            double dx = p.x - centers[k][0];
            double dy = p.y - centers[k][1];
            sse += dx*dx + dy*dy;
        }
        return sse;
    }

    private static double distance(XYPair p, double[] c) {
        double deltaX = p.x - c[0];
        double deltaY = p.y - c[1];

        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    }

    private static void copyCenters(double[][] sourceCenters, double[][] copiedCenters) {
        for (int i = 0; i < sourceCenters.length; i++) {
            copiedCenters[i][0] = sourceCenters[i][0];
            copiedCenters[i][1] = sourceCenters[i][1];
        }
    }

    public static class XYPair {
        public double x, y;

        public XYPair(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class RunSDO {
        private static final Random rand = new Random();
        private static final double[][] pairsArray = new double[1200][2];

        public static void createFile(String filename) throws IOException {
            int index = 0;
            index = runData(index, 150, 0.75, 1.25, 0.75, 1.25);
            index = runData(index, 150, 0.0, 0.5, 0.0, 0.5);
            index = runData(index, 150, 0.0, 0.5, 1.5, 2.0);
            index = runData(index, 150, 1.5, 2.0, 0.0, 0.5);
            index = runData(index, 150, 1.5, 2.0, 1.5, 2.0);
            index = runData(index, 75, 0.6, 0.8, 0.0, 0.4);
            index = runData(index, 75, 0.6, 0.8, 1.6, 2.0);
            index = runData(index, 75, 1.2, 1.4, 0.0, 0.4);
            index = runData(index, 75, 1.2, 1.4, 1.6, 2.0);
            index = runData(index, 150, 0.0, 2.0, 0.0, 2.0);

            try (FileWriter fw = new FileWriter(filename)) {
                for (double[] p : pairsArray)
                    fw.write(p[0] + " " + p[1] + "\n");
            }

            System.out.println(filename + " created successfully in " + System.getProperty("user.dir"));
        }

        private static int runData(int index, int num, double x1, double x2, double y1, double y2) {
            for (int i = 0; i < num; i++) {
                double x = x1 + rand.nextDouble() * (x2 - x1);
                double y = y1 + rand.nextDouble() * (y2 - y1);

                pairsArray[index][0] = x;
                pairsArray[index][1] = y;
                index++;
            }
            return index;
        }

        private static void loadFile(String filename) {
            try {
                List<String> rows = Files.readAllLines(Paths.get(filename));

                for (String row : rows) {
                    String[] tokens = row.trim().split("\\s+");
                    if (tokens.length == 2) {
                        double x = Double.parseDouble(tokens[0]);
                        double y = Double.parseDouble(tokens[1]);

                        pairs.add(new XYPair(x, y));
                    }
                }
                System.out.println("Loaded " + pairs.size() + " pairs.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
