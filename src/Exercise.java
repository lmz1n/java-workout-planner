public class Exercise implements Activity {
    
    private String name;
    private Set[] sets;
    private Duration restTime;
    private Duration setTime;
   
    public Exercise(String n, int numS, Duration r, Duration s){
        name = n;
        restTime = r;
        setTime = s;
        sets = new Set[numS];
    }

    public void setTimes(Duration r, Duration s){
        restTime = r;
        setTime = s;
    }


    public void setSet(int i, int reps, double weight){
        sets[i] = new Set(reps, weight);
    }

    public void setSet(int i, Set s){
        sets[i] = s;
    }

    public String getName(){
        return name;
    }

    public void setName(String n){
        name = n;
    }

    public int getSets(){
        return sets.length;
    }

    public Set getSet(int i){
        return sets[i];
    }

    public double getVolume(){
        double volume = 0;
        for (int i = 0; i < sets.length; i++){

            Set s = sets[i];
            volume += s.getWeight() * s.getReps();
            
            if(s instanceof DropSet){
                DropSet d = (DropSet)s;
                for(int j = 0; j < d.getDropCount(); j++){
                    Set drop = d.getDrop(j);
                    volume += drop.getWeight() * drop.getReps();
                }
            }

        }
        return volume;
    }

    public Duration getDuration(){
        return new Duration((restTime.getSeconds() + setTime.getSeconds()) * sets.length);
    }

    public Duration getRestDuration(){
        return new Duration(restTime.getSeconds());
    }

    public Duration getSetDuration(){
        return new Duration(setTime.getSeconds());
    }

    public void printActivity(){
        System.out.println("________________________________");
        System.out.println("\nExercise: "+ name);
        System.out.println("Rest time: " + restTime);
        System.out.println("Set time: " + setTime);
        System.out.println(sets.length + " sets:\n");
        for(int i = 0; i < sets.length; i++){
            if(sets[i] != null){
            System.out.println(" " + (i+1) + ": " + sets[i]);
            }
        }
        double volume = getVolume();
        System.out.println("\nTotal volume: " + volume + "lbs");
        System.out.println("________________________________");
    }
}