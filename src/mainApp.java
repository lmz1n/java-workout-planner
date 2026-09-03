import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*; 

public class mainApp {

    public static Scanner stdin;
    public static Activity[] activities = new Activity[50];
    public static int activityCount = 0;
    public static Workout[] workouts = new Workout[50];
    public static int workoutCount = 0;
    public static void main(String[] args) {

        stdin = new Scanner(System.in);


        System.out.println("Welcome to Workout Planner!\n");
        loadActivities();
        loadWorkouts();
        menu(); 

        int choice = getChoice();

        while ( choice != 11 ){
            if (choice == 1){
                addActivity();
            }
            else if (choice == 2){
                modifyActivity();
            }
            else if (choice == 3){
                viewActivities();
            }
            else if (choice == 4){
                deleteActivity();
            }
            else if (choice == 5){
                planWorkout();
            }
            else if (choice == 6){
                modifyWorkout();
            }
            else if (choice == 7){
                viewWorkouts();
            }
            else if (choice == 8){
                deleteWorkout();
            }
            else if (choice == 9){
                viewPastWorkouts();
            }
            else if (choice == 10){
                startWorkout();
            }

            menu();
            choice = getChoice();

        }
        System.out.println("Quitting...\n");

    }


    public static int getChoice(){
        System.out.println("\nChoose an option between 1 and 11:");
        int choice = getInt();
        while (choice < 1 || choice > 11){ 
            System.out.println("Invalid choice. Try again.");
            choice = getInt();
        }
        return choice;
    }

    public static void menu(){
        System.out.println("\n\t1. Add a new activity.");
        System.out.println("\t2. Modify an activity.");
        System.out.println("\t3. View activities.");
        System.out.println("\t4. Delete an activity.");
        System.out.println("\t5. Plan a new workout.");
        System.out.println("\t6. Modify a workout.");
        System.out.println("\t7. View workouts.");
        System.out.println("\t8. Delete a workout.");
        System.out.println("\t9. View past workouts.");
        System.out.println("\t10. Start a workout.");
        System.out.println("\t11. Quit.");
    }

    public static Set createSet(int i){
        System.out.println("\nSet " + (i+1));
        System.out.println("1. Normal Set");
        System.out.println("2. Drop Set");

        int type = getInt();

        System.out.println("Main Weight (lbs): ");
        double weight = getDouble();

        System.out.println("Main Reps: ");
        int reps = getInt();

        if(type == 1){
            return new Set(reps, weight);
        }

        else if(type == 2){

            System.out.println("How many drops?");
            int drops = getInt();

            DropSet d = new DropSet(reps, weight, drops);

            for(int j = 0; j < drops; j++){

                System.out.println("Drop " + (j+1) + " weight:");
                double dropWeight = getDouble();

                System.out.println("Drop " + (j+1) + " reps:");
                int dropReps = getInt();

                d.addDrop(dropReps, dropWeight);
            }

            return d;
         } else{
            System.out.println("Invalid set type. Try again.");
            return createSet(i);
        }
    }

    public static void saveActivities(){
        try{
            FileWriter writer = new FileWriter("activities.txt");
            for(int i = 0; i < activityCount; i++){
                if(!(activities[i] instanceof Exercise)) continue;

                Exercise e = (Exercise)activities[i];

                writer.write(e.getName() + "\n");
                writer.write(e.getSets() + "\n");
                writer.write(e.getRestDuration().getSeconds() + "\n");
                writer.write(e.getSetDuration().getSeconds() + "\n");
                
                for(int j = 0; j < e.getSets(); j++){
                    Set s = e.getSet(j);
                    if(s instanceof DropSet){
                        
                        DropSet d = (DropSet)s;

                        writer.write("D," + d.getWeight() + "," + d.getReps() + "," + d.getDropCount());
                        for(int k = 0; k < d.getDropCount(); k++){
                            Set drop = d.getDrop(k);
                            writer.write("," + drop.getWeight() + "," + drop.getReps());
                        }
                        writer.write("\n");

                    } else writer.write("N," + s.getWeight() + "," + s.getReps() + "\n");
                }
                writer.write("\n");
            }

            writer.close();
            System.out.println("\nActivities saved.");

        } catch(IOException e){
            System.out.println("\nError saving activities.");
        }
    }

    public static void loadActivities(){
        try{
            Scanner file = new Scanner(new File("activities.txt"));
            while(file.hasNextLine()){

                String name = file.nextLine();
                if(name.isEmpty()) continue;

                int numSets = Integer.parseInt(file.nextLine());
                int rest = Integer.parseInt(file.nextLine());
                int setTime = Integer.parseInt(file.nextLine());

                Exercise e = new Exercise(name, numSets, new Duration(rest), new Duration (setTime));
    
                for(int i = 0; i < numSets; i++){
                    String line = file.nextLine();
                    String[] parts = line.split(",");

                    if(parts[0].equals("N")){
                        double weight = Double.parseDouble(parts[1]);
                        int reps = Integer.parseInt(parts[2]);

                        e.setSet(i, reps, weight);

                    } else if (parts[0].equals("D")){

                        double weight = Double.parseDouble(parts[1]);
                        int reps = Integer.parseInt(parts[2]);
                        int dropCount = Integer.parseInt(parts[3]);

                        DropSet d = new DropSet(reps, weight, dropCount);
                        int index = 4;
                        for(int k = 0; k < dropCount; k++){
                            double dw = Double.parseDouble(parts[index++]);
                            int dr = Integer.parseInt(parts[index++]);
                            d.addDrop(dr, dw);
                        }

                        e.setSet(i, d);

                    }
                }

                activities[activityCount++] = e;
                if(file.hasNextLine()) file.nextLine();
            }

            file.close();
            System.out.println("Activities loaded.");

        } catch(Exception e){
            System.out.println("No saved activities found.");
        }
    }

    public static void addActivity(){

        if(activityCount == activities.length){
            System.out.println("Activity storage full.");
            return;
        }
    
        System.out.println("\nNew exercise name:");
        String name = stdin.nextLine();

        for(int i = 0; i < activityCount; i++){
            if(name.equalsIgnoreCase(activities[i].getName())){
                System.out.println("\nActivity names have to be different.");
                System.out.println(name + " already exists.");
                return;
            }
        }

        System.out.println("Number of sets:");
        int numSets = getInt();
        System.out.print("Rest time between sets (seconds): ");
        Duration rest = new Duration(getInt());
        System.out.print("Estimated set duration (seconds): ");
        Duration set = new Duration(getInt());
        Exercise e = new Exercise(name, numSets, rest, set);

        for(int i = 0; i < numSets; i++){
            Set s = createSet(i);
            e.setSet(i, s);
        }
        activities[activityCount] = e;
        activityCount++;

        saveActivities();
        System.out.println("Activity added successfully!");
    }

    public static void modifyActivity(){
        if(noActivities()) return;

        quickViewActivities();

        int activityIndex = findActivity();
        if(activityIndex == -1) return;

        Activity a = activities[activityIndex];
        a.printActivity();
        if(a instanceof Exercise){
            modifyExercise((Exercise)a);
        }

        saveActivities();

    }

    public static void modifyExercise(Exercise e){
        if(noActivities()) return;
        System.out.println("\nModify Exercise:");
        System.out.println(" 1. Change name");
        System.out.println(" 2. Change times");
        System.out.println(" 3. Modify sets");

        int choice = getInt();
        if (choice == 1){
    
            System.out.println("\nNew name:");
            String newName = stdin.nextLine();
            e.setName(newName);
        } else if (choice == 2){
            System.out.println("\nNew rest time:");
            Duration newRest = new Duration(getInt());
            System.out.println("\nNew set time:");
            Duration newSet = new Duration(getInt());
            e.setTimes(newRest, newSet);
        } else if (choice == 3) {
            for(int i = 0; i < e.getSets(); i++){
                Set s = createSet(i);
                e.setSet(i, s);
            }
        } else {
            System.out.println("Not a valid choice. Try again.");
            return;
        }
    }

    public static void viewActivities(){
        if(noActivities()) return;
        for(int i = 0; i < activityCount; i++){
            System.out.println("\nActivity #" + (i+1));
            activities[i].printActivity();
        }
    }

    public static void quickViewActivities(){
        System.out.println("\nAll created activity names:\n");
        for(int i = 0; i < activityCount; i++)
            System.out.println(" - " + activities[i].getName());
    }

    public static boolean noActivities(){
        if(activityCount == 0) {
            System.out.println("\nNo activities available.");
            return true;
        }
        return false;
    }

    public static int findActivity(){

        System.out.println("\nActivity name:");
        String name = stdin.nextLine();
        for(int i = 0; i< activityCount; i++){
            if(name.equalsIgnoreCase(activities[i].getName())){
                return i;
            }
        }
        System.out.println("Activity does not exist.");
        return -1;

    }

    public static void deleteActivity(){
 
        if(noActivities()) return;

        quickViewActivities();

        int activityIndex = findActivity();
        if(activityIndex < 0) return;
        activities[activityIndex].printActivity();
        System.out.println("\nDelete this activity? (y/n)");
        String answer = stdin.next();
        stdin.nextLine();

        if(!answer.equalsIgnoreCase("y")) return;
        for(int i = activityIndex; i < activityCount - 1; i++){
            activities[i] = activities[i + 1];
        }
        activities[activityCount - 1] = null; 
        activityCount--;

        saveActivities();
        System.out.println("Activity deleted successfully.");
    }

    public static void saveWorkouts(){
        try{
            FileWriter writer = new FileWriter("workouts.txt");
            for(int i = 0; i < workoutCount; i++){
                Workout w = workouts[i];

                writer.write(w.getName() + "\n");
                writer.write(w.getActivityCount() + "\n");

                for(int j = 0; j < w.getActivityCount(); j++){
                    writer.write(w.getActivityName(j) + "\n");
                }
                writer.write("\n");
            }
            writer.close();
            System.out.println("\nWorkouts saved.");

        } catch(IOException e){
            System.out.println("\nError saving workouts.");
        }
    }

    public static void loadWorkouts(){
        try{

            Scanner file = new Scanner(new File("workouts.txt"));

            while(file.hasNextLine()){

                String name = file.nextLine();
                if(name.isEmpty()) continue;

                int c = Integer.parseInt(file.nextLine());

                Workout w = new Workout(name, c);

                for(int i = 0; i < c; i++){

                    String activityName = file.nextLine();
                    int activityIndex = -1;

                    for(int j = 0; j < activityCount; j++){
                        if(activityName.equalsIgnoreCase(activities[j].getName())) {
                            activityIndex = j;
                            break;
                        }
                    }

                    if(activityIndex != -1)
                        w.addActivity(activities[activityIndex]);
                    else System.out.println("Warning: Activity '" + activityName + "' not found.");
                }


                if(workoutCount < workouts.length){
                    workouts[workoutCount++] = w;
                }
                if(file.hasNextLine()) file.nextLine();
            }

            file.close();
            System.out.println("Workouts loaded.");

        } catch(Exception e){
            System.out.println("No saved workouts found.");
        }
    }

    public static void planWorkout(){
        if(noActivities()) return;

        System.out.println("\nWorkout name:");
        String name = stdin.nextLine();

        for(int i = 0; i < workoutCount; i++){
            if(name.equalsIgnoreCase(workouts[i].getName())){
                System.out.println("\nWorkout names have to be different.");
                System.out.println("" + name + " already exists.");
                return;
            }
        }

        System.out.println("How many activities in the workout?");
        int numActivities = getInt();
        
        if (numActivities > activityCount){
            System.out.println("You can't add more activities than what you have already created.");
            return;
        }   
        Workout w = new Workout(name, numActivities);
       
        quickViewActivities();

        for(int i = 0; i < numActivities; i++){
            System.out.println("\nCreating activity #" + (i + 1));
            int activityIndex = findActivity();
            if(activityIndex == -1){
                i--;
                continue;
            }
            w.addActivity(activities[activityIndex]);
        }

        if(workoutCount == workouts.length){
            System.out.println("Workout storage full.");
            return;
        }
        workouts[workoutCount] = w;
        workoutCount++;
        saveWorkouts();
        System.out.println("\nWorkout created successfully!");
    }


    public static void modifyWorkout(){
        if(noWorkouts()) return;
        viewWorkouts();
        int workoutIndex = findWorkout();
        if(workoutIndex < 0) return;
        Workout w = workouts[workoutIndex];
        System.out.println("\nModify Workout:");
        System.out.println(" 1. Change name");
        System.out.println(" 2. Change an activity");
        System.out.println(" 3. Add activity to end of workout");
        int choice = getInt();
        if(choice == 1){
            
            System.out.println("\nNew name:");
            String newName = stdin.nextLine();
            w.setName(newName);        
        } else if (choice == 2){
            if(noActivities()) return;
            w.printWorkout();
            System.out.println("\nWhich activity number do you want to change?");
            int i = getInt() - 1;
            if (i < 0 || i >= w.getActivityCount()){
                System.out.println("Invalid activity.");
                return;
            }
            
            quickViewActivities();
            System.out.println("\nSelect the new activity #:");
            int newActivityIndex = getInt() - 1;
            if(newActivityIndex < 0 || newActivityIndex >= activityCount){
                System.out.println("Invalid activity");
                return;
            }
            w.setActivity(i, activities[newActivityIndex]);
        } else if (choice == 3){

            quickViewActivities();
            int activityIndex = findActivity();
            if (activityIndex < 0) return;
            Activity a = activities[activityIndex];
            w.addActivity(a);
            
        } else {
            System.out.println("Invalid choice");
            return;
        }
        saveWorkouts();
        System.out.println("Activity updated successfully.");

    }

    public static boolean noWorkouts(){
        if (workoutCount == 0){
            System.out.println("\nNo workouts available.");
            return true;
        }
        return false;
    }

    public static int findWorkout(){

        System.out.println("\nWorkout name:");
        String name = stdin.nextLine();
        for(int i = 0; i< workoutCount; i++){
            if(name.equalsIgnoreCase(workouts[i].getName())){
                return i;
            }
        }
        System.out.println("Workout does not exist. Try again.");
        return -1;

    }

    public static void viewWorkouts(){
        if(noWorkouts()) return;
        System.out.println("______________________________________");
        for(int i = 0; i < workoutCount; i++){
            System.out.println("\n - Workout #" + (i+1));
            workouts[i].printWorkout();
        }
        System.out.println("______________________________________");
    }

    public static void deleteWorkout(){
        if(noWorkouts()) return;
        int workoutIndex = findWorkout();
        if (workoutIndex < 0) return;
        workouts[workoutIndex].printWorkout();
        System.out.println("\nDelete this workout? (y/n)");
        String answer = stdin.next();
        stdin.nextLine();

        if(!answer.equalsIgnoreCase("y")) return;
        for(int i = workoutIndex; i < workoutCount - 1; i++){
            workouts[i] = workouts[i + 1];
        }
        workouts[workoutCount - 1] = null; 
        workoutCount--;
        saveWorkouts();
        System.out.println("Workout deleted successfully.");
    }

    public static void viewPastWorkouts(){
        try{
            Scanner file = new Scanner(new File("workout_history.txt"));
            if(!file.hasNextLine()) System.out.println("\nNo past workouts recorded.");
            while(file.hasNextLine()) System.out.println(file.nextLine());
            file.close();
        } catch (Exception e){
            System.out.println("\nNo past workouts recorded.");
        }
    }

    public static void startWorkout(){
        if (noWorkouts()) return;

        System.out.println("\nAll created workout names:\n");
        for(int i = 0; i < workoutCount; i++){
            System.out.println(" - " + workouts[i].getName());
        }
        int i = findWorkout();
        if(i < 0) return;
        workouts[i].startWorkout();
    }

    public static int getInt(){
        while(true){
            if(stdin.hasNextInt()){
                int val = stdin.nextInt();
                stdin.nextLine();
                return val;
            } else {
                System.out.println("Invalid input. Please enter a integer:");
                stdin.nextLine(); 
            }
        }
    }

    public static double getDouble(){
        while(true){
            if(stdin.hasNextDouble()){
                double val = stdin.nextDouble();
                stdin.nextLine();
                return val;
            } else {
                System.out.println("Invalid input. Please enter a double:");
                stdin.nextLine(); 
            }
        }
    }

}

