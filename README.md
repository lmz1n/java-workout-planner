# java-workout-planner
A Java-based console application that allows users to create, manage, and complete personalized workout plans. The application supports exercises, normal sets, drop sets, rest timers, workout timing, session logging, and persistent storage. It also demonstrates object-oriented programming concepts including inheritance, polymorphism, and interfaces.

# Features
- Create and manage exercises.
- Modify or delete existing exercises.
- Create personalized workouts from saved activities.
- Modify or delete existing workouts.
- Create normal sets and drop sets.
- Configure weights and repetitions.
- Configure rest durations between sets.
- Run workouts using a planned exercise mode.
- Use a manual exercise mode during a workout.
- Track the total duration of a workout.
- Record the start and end time of each workout.
- Save completed workout sessions to a history file.
- Persist activities and workouts between program executions.

# Exercises and Sets
Exercises can contain multiple sets. Each set stores information such as:

- Weight
- Number of repetitions

The program supports two types of sets.

**Normal Set**

A standard set contains a weight and number of repetitions.

Example:

- Weight: 135 lbs
- Reps: 10

**Drop Set**

A drop set begins with a main weight and repetition count and can include multiple additional drops with different weights and repetitions.

Example:

- Main Set: 135 lbs × 10 reps

- Drop 1: 115 lbs × 8 reps

- Drop 2: 95 lbs × 8 reps

The application uses polymorphism when handling normal sets and drop sets.
  
# Workout System
A workout consists of multiple activities.
When starting a workout, the application records:

- Workout date
- Start time
- End time
- Total workout duration
- Completed sets
- Actual weight used
- Actual repetitions completed

For exercises, users can either follow the planned workout or use manual exercise mode. Completed workout information is saved to a workout history file.

# Persistent Data
The program uses text files to preserve data between program executions.

**activities.txt** - Stores saved exercises, including:

- Exercise name
- Number of sets
- Rest duration
- Estimated set duration
- Set information
- Drop set information

**workouts.txt** - Stores saved workouts and the activities associated with each workout.

**workout_history.txt** - Stores completed workout sessions, including workout duration, dates, times, and recorded performance.

# Technologies Used
- Java
- Object-Oriented Programming
- Inheritance
- Polymorphism
- Interfaces
- Arrays
- ArrayList
- File I/O
- LocalDate
- LocalTime
- Console-based user interaction
