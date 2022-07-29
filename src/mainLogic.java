import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class mainLogic {
    public static void main(String[] args) throws IOException {
        double[][] distanceMatrix = getDistanceMatrix(gettingSchoolArray());
        double bestShort = Integer.MAX_VALUE;
        while(true) {  // For randomness
            ArrayList<Integer> path = getPath(distanceMatrix);
            double finalDistance = getFinalDistance(path, distanceMatrix);
            if(finalDistance < bestShort) {
                bestShort = finalDistance;
                System.out.println(path);
                System.out.println(bestShort + " KM");
            }
        }
    }

    private static School[] gettingSchoolArray() throws IOException {
        String s = String.join(" ", Files.readAllLines(Paths.get("TextFile\\120schools.txt")));
        String[] fileContent = s.split(",|\\s");

        School[] arr = new School[121];  // To store schools in an array of size 121
        int i = 0, counter = 0;  // i for actual iteration over loop, counter for deciding on which index of array goes for id, cord1 or cord2
        while(i < arr.length) {
            int firstIndex = counter, secondIndex = counter + 1, thirdIndex = counter + 2;  // Since file split by comma and space, the id, cord1 and cord2 all on separate lines
            arr[i] = new School(Integer.parseInt(fileContent[firstIndex]), Double.parseDouble(fileContent[secondIndex]), Double.parseDouble(fileContent[thirdIndex]));
            if((thirdIndex) != arr.length) {
                counter = thirdIndex + 1;  // Move on to the next line (set of id, cord1, cord2)
            }
            i++;
        }
        return arr;
    }

    private static double[][] getDistanceMatrix(School[] schools) {
        double[][] array = new double[121][121];  // To store the distances between each school as a 2D matrix
        for(int i = 0; i < schools.length; i++) {
            for(int j = 0; j < schools.length; j++) {
                array[i][j] = haversine(schools[i].getCord1(), schools[i].getCord2(), schools[j].getCord1(), schools[j].getCord2());
                //System.out.println(schools[i].getId() + " + " + schools[j].getId() + ": " + array[i][j] + " KM");  // ***** Testing *****
            }
        }
        return array;
    }

    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth
        double latDistance = (lat2-lat1) * Math.PI / 180;
        double lonDistance = (lon2-lon1) * Math.PI / 180;
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos((lat1) * Math.PI / 180) * Math.cos((lat2) * Math.PI / 180) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;
        return distance;
    }

    private static ArrayList<Integer> getPath(double[][] distanceMatrix) {  // Greedy algorithm
        ArrayList<Integer> path = new ArrayList<>();  // To mark the pathway
        Set<Integer> visitedSchool = new HashSet<>();  // To mark the schools that are visited to avoid repetition
        Random r = new Random();

        int currentSchool = 0;  // To keep track of the school to add to path
        int row = currentSchool;  // To essentially be the row index (to avoid overwrite with the currentSchool)
        path.add(0);
        visitedSchool.add(0);
        while(path.size() < 121) {
            double shortest = Integer.MAX_VALUE;
            for(int j = 0; j < 121; j++) {
                double currentDistance = distanceMatrix[row][j] * r.nextDouble(0.7, 1.3);  // For random
                //double currentDistance = distanceMatrix[row][j];  // For single use
                if(!visitedSchool.contains(j) && currentDistance < shortest && currentDistance != j) {
                    shortest = currentDistance;
                    currentSchool = j;
                }
            }
            path.add(currentSchool);
            visitedSchool.add(currentSchool);
            row = currentSchool;
        }
        path.add(0);
        return path;
    }

    private static double getFinalDistance(ArrayList<Integer> list, double[][] matrix) {
        double finalDistance = 0.0;
        for(int i = 0; i < list.size() - 1; i++) {
            finalDistance += matrix[list.get(i)][list.get(i + 1)];
        }
        return finalDistance;
    }
}