
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class Workout {

    private String name;
    private ArrayList<String> sessionLog = new ArrayList<>();
    private LocalDate workoutDate;  
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
    private int activityCount;
    private Activity[] activities;
    private Duration workoutDuration;
    private LocalTime startTime;
    private LocalTime endTime;

    public Workout(String n, int c){
        name = n;
        activities = new Activity[c];
        activityCount = 0;
    }

    public String getName(){
        return name;
    }

    public String getActivityName(int i){
        return activities[i].getName();
    }

    public int getActivityCount(){
        return activityCount;
    }

    public void setName(String n){
        name = n;
    }

    public void addActivity(Activity a){

        if(activityCount == activities.length){
            Activity[] newArr = new Activity[activities.length + 5];
            for(int i = 0; i < activities.length; i++){
                newArr[i] = activities[i];
            }
            activities = newArr;
        }

        if(activityCount < activities.length){
            activities[activityCount] = a;
            activityCount++;
        }
    }

    public void startWorkout(){

        WorkoutTimer workoutTimer = new WorkoutTimer();
        workoutTimer.start();
        workoutDate = LocalDate.now();
        startTime = LocalTime.now();

        for(int i = 0; i < activityCount; i++){

            Activity curr = activities[i];
            System.out.println("___________________________________________________");
            System.out.println("\nStarting: "+ curr.getName());

            System.out.println("1. Follow planned exercise.");
            System.out.println("2. Manual exercise mode.");
            int choice = mainApp.getInt();

            if(choice == 1 && curr instanceof Exercise)
                runPlanned((Exercise)curr);
            else if(choice == 1){
                 System.out.println("This activity cannot be run as an exercise.");
                 i--;
                 continue;
            }
            else if(choice == 2) runManual();

            
            System.out.println("Activity finished.");
            if (i < activityCount - 1) {
                System.out.println("Press ENTER to start next activity.");
                mainApp.stdin.nextLine(); }
        }
        endTime = LocalTime.now();
        workoutDuration = workoutTimer.stop();
        saveWorkout();

        System.out.println("\nWorkout complete!");
        System.out.println(" - Workout duration: " + workoutDuration);
        System.out.println(" - Started at: " + startTime.format(format));
        System.out.println(" - Ended at: " + endTime.format(format));
    }

    public void printWorkout(){

        System.out.println("\nWorkout: " + name);
        System.out.println("Activities:");

        for(int i = 0; i < activityCount; i++){
            System.out.println(" " + (i+1) + ". " + activities[i].getName());
        }

    }

    public void setActivity(int i, Activity a){
        if(i >= 0 && i < activityCount) activities[i] = a;
    }
    
    public void saveWorkout(){
        try{
            FileWriter writer = new FileWriter("workout_history.txt" , true);

            String day = workoutDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US);
            writer.write("_______________________________________________\n");
            writer.write("\nWorkout: " + name + "\n");
            writer.write(day + " - " + workoutDate + "\n");
            writer.write("Workout duration: " + workoutDuration + "\n");
            writer.write("Start: " + startTime.format(format) + "\n");
            writer.write("End: " + endTime.format(format) + "\n\n");

            for(String s : sessionLog) writer.write(s + "\n");

            writer.write("_______________________________________________\n");
            
            writer.close();

        } catch(IOException e) {
            System.out.println("Error saving workout.");
        }
    }

    public void runPlanned(Exercise curr){
        if(curr instanceof Exercise){
            Exercise e = (Exercise)curr;

            sessionLog.add("\n" + curr.getName() + "\n");

            System.out.println("Rest between sets: " + e.getRestDuration());

            for(int j = 0; j < e.getSets(); j++){

                Set s = e.getSet(j);
            
                System.out.println("\n - Set " + (j+1));
                System.out.println("\nPerform set " + (j+1));
                System.out.println("Press enter when finished.");
                mainApp.stdin.nextLine();

                System.out.println("Actual weight used:");
                double weight = mainApp.getDouble();
                System.out.println("Actual reps completed:");
                int reps = mainApp.getInt();
                sessionLog.add(" - Set " + (j+1) + ": " + weight + "lbs x " + reps);

                if(s instanceof DropSet){

                    DropSet d = (DropSet)s;

                    for(int k = 0; k < d.getDropCount(); k++){
                        System.out.println("\nPerform drop " + (k+1));
                        System.out.println("Press enter when finished.");
                        mainApp.stdin.nextLine();

                        System.out.println("Actual drop weight:");
                        double dw = mainApp.getDouble();
                        
                        System.out.println("Actual drop reps:");
                        int dr = mainApp.getInt();
                        
                        sessionLog.add("  -> Drop "+ (k+1) + ": " 
                        + dw + "lbs x " + dr);
                            
                    }
                }

                if( j < e.getSets() - 1){
                    System.out.println("\nRest starting...");
                    Timer timer = new Timer(e.getRestDuration());
                    timer.start();
                }
            }
        } 
        else {    
            Timer timer = new Timer(curr.getDuration());
            timer.start();
        }
    }

    public void runManual(){
        System.out.println("New exercise name:");
        String name = mainApp.stdin.nextLine();

        sessionLog.add("\n" + name + "\n");

        System.out.println("Number of sets:");
        int setCount = mainApp.getInt();
        System.out.println("Rest time (seconds):");
        Duration rest = new Duration(mainApp.getInt());

        for(int j = 0; j < setCount; j++){
            
            System.out.println("\nSet " + (j+1));
            System.out.println("Press ENTER when finished.");
            mainApp.stdin.nextLine();

            System.out.println("Weight:");
            double weight = mainApp.getDouble();
            System.out.println("Reps:");
            int reps = mainApp.getInt();

            sessionLog.add(" - Set " + (j+1) + ": " + weight + "lbs x " + reps);

            System.out.println("Was this a drop set? (y/n)");
            String drop = mainApp.stdin.next();
            mainApp.stdin.nextLine();
            if(drop.equalsIgnoreCase("y")){
                
                int dropNum = 1;

                while(true){
                    System.out.println("\nDrop " + dropNum);
                    System.out.println(" - Weight:");
                    double dw = mainApp.getDouble();
                    System.out.println( " - Reps:");
                    int dr = mainApp.getInt();

                    sessionLog.add("  -> Drop " + dropNum + ": " + dw + "lbs x " + dr);

                    System.out.println("\nAnother drop? (y/n)");
                    String ans = mainApp.stdin.next();
                    mainApp.stdin.nextLine();
                    if(!ans.equalsIgnoreCase("y")) break;
                    mainApp.stdin.nextLine();

                    dropNum ++;
                }
                if(j < setCount - 1){
                    System.out.println("\nRest starting...");
                    Timer timer = new Timer(rest);
                    timer.start();
                }
            }
        }
    }
}
