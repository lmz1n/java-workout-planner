public class WorkoutTimer{

    private long startTime;

    public void start(){
        startTime = System.currentTimeMillis();
    }

    public Duration stop(){
        long endTime = System.currentTimeMillis();
        long elapsed = (endTime - startTime) / 1000;
        return new Duration((int)elapsed);
    }

}