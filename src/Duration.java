public class Duration {
    
    private int seconds;

    public Duration(int s){
        seconds = s;
    }

    public int getMinutes() {
        return seconds/60;
    }

    public int getSeconds(){
        return seconds;
    }

    public String toString(){
        if(seconds/60 != 0) return this.getMinutes() + "m" + (seconds % 60) + "s";
        return seconds + "s";
    }

}
