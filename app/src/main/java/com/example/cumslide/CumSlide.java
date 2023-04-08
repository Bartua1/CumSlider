package com.example.cumslide;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class CumSlide {
    public static List<List<Integer>> matrix = new ArrayList<List<Integer>>();
    Integer numRows = 60;
    Integer numColumns = 4;
    Pair player = new Pair(0,0); // Player position: X=row Y=column
    Integer goal = 0; // Column of the goal
    public static Map<Integer, Bridge> positions = new HashMap<>();
    Integer numBridges = 10;

    public CumSlide(){
        matrix = new ArrayList<List<Integer>>();
        player = new Pair(0,0);
        goal = 0;
        positions = new HashMap<>();
    }
    public CumSlide(Integer size) {
        positions = new HashMap<>();
        // Inicializar la matriz con valor 0
        for (int i = 0; i < numRows; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < numColumns; j++) {
                row.add(0);
            }
            matrix.add(row);
        }
        List<Integer> rows = new ArrayList<>();
        for (int i = 3; i < numRows/3; i+=3) {
            rows.add(i);
        }
        int bridgeValue = 1;
        while (rows.size() > 0) {
            addHorizontalB(matrix, bridgeValue, rows.get(0));
            rows.remove(0);
            bridgeValue++;
        }
        // System.out.println(this.checkPairs());
    }

    public Boolean checkPairs(){
        // Make a dict with the values of the matrix
        HashMap<Integer, Integer> dict = new HashMap<Integer, Integer>();
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                if (matrix.get(i).get(j) != 0) {
                    if (dict.containsKey(matrix.get(i).get(j))) {
                        dict.put(matrix.get(i).get(j), dict.get(matrix.get(i).get(j)) + 1);
                    } else {
                        dict.put(matrix.get(i).get(j), 1);
                    }
                }
            }
        }
        List<Integer> values = new ArrayList<Integer>(dict.values());
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) % 2 != 0) {
                return false;
            }
        }
        return true;

    }

    // Función para agregar un puente entre dos columnas adyacentes
    /*
    public void addBridge(List<List<Integer>> matrix, int bridgeValue) {
        int bridgeRow = new Random().nextInt(7) + 3; // seleccionar una fila al azar empezando por la tercera
        //int bridgeRow2 = new Random().nextInt(numRows); // seleccionar otra fila al azar
        int bridgeRow2 = new Random().nextInt(10)!=0
                ? bridgeRow
                : new Random().nextInt(5) + 3;
        int bridgeColumn = new Random().nextInt(numColumns-1); // seleccionar una columna al azar

        // Comprobar si ya hay un puente en las celdas seleccionadas
        if(matrix.get(bridgeRow).get(bridgeColumn) != 0 || matrix.get(bridgeRow).get(bridgeColumn + 1) != 0) {
            int randomNumber;
            do {
                randomNumber = new Random().nextInt(5) + 3;
                if (randomNumber >= 7) {
                    randomNumber++;
                }
            } while (randomNumber == bridgeRow);
            bridgeRow = randomNumber; // seleccionar una fila al azar empezando por la tercera
            bridgeRow2 = new Random().nextInt(10)!=0
                    ? bridgeRow
                    : new Random().nextInt(5) + 3;
            bridgeColumn = new Random().nextInt(numColumns-1); // seleccionar otra columna al azar
        }

        // agregar el puente a la matriz
        matrix.get(bridgeRow).set(bridgeColumn, bridgeValue);
        //System.out.println("row: " + matrix.get(bridgeRow));
        //System.out.println("bridgeColumn: " + bridgeColumn + "    ; bridgeValue: " + bridgeValue);
        matrix.get(bridgeRow2).set(bridgeColumn + 1, bridgeValue);

        // Agregar al diccionario positions, una lista con las parejas del puente
        positions.put(bridgeValue, new Bridge(bridgeColumn, bridgeRow, bridgeColumn+1, bridgeRow2));
        System.out.print("Se ha añadido un cuarteto -> Bridge: " + bridgeColumn + ", " + bridgeRow + ", " + bridgeColumn+1 + ", " + bridgeRow2 + "\n");
    }
     */

    public Integer addHorizontalB(List<List<Integer>> matrix, int i, int bridgeRow){
        int bridgeColumn = new Random().nextInt(3); // seleccionar una columna al azar

        // agregar el puente a la matriz
        matrix.get(bridgeRow).set(bridgeColumn, i);
        matrix.get(bridgeRow).set(bridgeColumn + 1, i);

        // Agregar al diccionario positions, una lista con las parejas del puente
        positions.put(i, new Bridge(bridgeColumn, bridgeRow, bridgeColumn+1, bridgeRow));
        System.out.print("Se ha añadido un cuarteto");
        return bridgeRow;
    }


    // Función para agregar un puente entre dos columnas adyacentes
    public void addBridge(List<List<Integer>> matrix, int i) {
        int bridgeColumn = new Random().nextInt(3); // seleccionar una columna al azar
        int bridgeRow = new Random().nextInt(numRows/4)+3; // seleccionar una fila al azar
        //int bridgeRow2 = new Random().nextInt(20); // seleccionar otra fila al azar
        int bridgeRow2 = bridgeRow==3 ? bridgeRow-1 : bridgeRow; // seleccionar otra fila al azar

        int bridgeValue = i; // el valor del puente es i

        // Comprobar si ya hay un puente en las celdas seleccionadas
        if(matrix.get(bridgeRow).get(bridgeColumn) != 0 || matrix.get(bridgeRow).get(bridgeColumn + 1) != 0) {
            int randomNumber;
            do {
                randomNumber = new Random().nextInt(numRows/4)+3;
            } while (randomNumber == bridgeRow);
            bridgeRow = randomNumber; // seleccionar una fila al azar empezando por la tercera
            bridgeRow2 = bridgeRow;
            bridgeColumn = new Random().nextInt(numColumns-1); // seleccionar otra columna al azar
        }

        // agregar el puente a la matriz
        matrix.get(bridgeRow).set(bridgeColumn, bridgeValue);
        matrix.get(bridgeRow2).set(bridgeColumn + 1, bridgeValue);

        // Agregar al diccionario positions, una lista con las parejas del puente
        positions.put(i, new Bridge(bridgeColumn, bridgeRow, bridgeColumn+1, bridgeRow2));
        System.out.print("Se ha añadido un cuarteto");
    }

    public Integer printRow(){
        String row = "";
        for (int i = 0; i < matrix.get(player.getX()).size(); i++) {
            if (i == player.getY()) {
                row += "P ";
            } else {
                row += matrix.get(player.getX()).get(i) + ((matrix.get(player.getX()).size()-1)==i?"":" ");
            }
        }
        System.out.println( "[" + row + "], " + (player.getX()+1));
        return player.getX();
    }

    /*
    public void movePlayer(){
        Integer row = player.getX();
        Integer column = player.getY();
        Integer value = matrix.get(row).get(column);

        if (value!=0){
            // Get the indexes of the matrix where the value = value
            List<Pair> indexes = findIndexes(value);
            // Get the index with the highest X value
            Pair index = indexes.get(0);
            for (int i = 0; i < indexes.size(); i++) {
                if (indexes.get(i).getX() > index.getX()) {
                    index = indexes.get(i);
                }
            }
            // Set the player position to the index
            player.setX(index.getX());
            player.setY(index.getY());
        }
        player.setX(player.getX()+1);
    }
    */

    public List<Pair> findIndexes(int value) {
        List<Pair> result = new ArrayList<>();
        for (int i = 0; i < matrix.size(); i++) {
            List<Integer> sublist = matrix.get(i);
            for (int j = 0; j < sublist.size(); j++) {
                if (sublist.get(j) == value) {
                    Pair index = new Pair(i, j);
                    result.add(index);
                }
            }
        }
        return result;
    }

}
