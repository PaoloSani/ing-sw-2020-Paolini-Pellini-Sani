package it.polimi.ingsw.model;

public class Table {
    private final static int level1 = 22;
    private final static int level2 = 18;
    private final static int level3 = 14;
    private final static int dome = 18;
    private Space[][] matrix;
    private Model model;

    public Table(Model model) {
        this.setUpMatrix();
        this.model = model;
    }

    public void setUpMatrix(){
        for (int i=0; i<5; i++){

            for (int j=0; j<5; j++){

                this.matrix[i][j] = new Space(i,j);

            }
        }
    }

}
