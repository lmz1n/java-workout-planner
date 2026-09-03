public class Timer {

    private Duration duration;

    public Timer(Duration d){
        duration = d;
    }

    public void start(){

        int remaining = duration.getSeconds();

        while(remaining > 0){

            int m = remaining / 60;
            int s = remaining % 60;

            System.out.println(m + ":" + (s < 10 ? "0" : "") + s);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            remaining --;
        }
        System.out.println("Rest finished!");
    }

}
