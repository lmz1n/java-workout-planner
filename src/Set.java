public class Set {
    
    protected int reps;
    protected double weight;

    public Set(int r, double w){
        reps = r;
        weight = w;
    }

    public void setSet(int r, double w){
        reps = r;
        weight = w;
    }

    public int getReps(){
        return reps;
    }

    public double getWeight(){
        return weight;
    }

    public String toString(){
        return weight + "lbs x " + reps;
    }

}
