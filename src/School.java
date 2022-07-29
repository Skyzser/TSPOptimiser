public class School {
    private final int id;
    private final double cord1;
    private final double cord2;

    public School(int id, double cord1, double cord2) {
        this.id = id;
        this.cord1 = cord1;
        this.cord2 = cord2;
    }

    public int getId() {
        return id;
    }

    public double getCord1() {
        return cord1;
    }

    public double getCord2() {
        return cord2;
    }
}