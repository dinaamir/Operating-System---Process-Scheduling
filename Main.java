import java.util.Scanner;
import java.util.ArrayList;
public class Main {
  public static void main(String[] args) {
    ArrayList<ProcessDetails> process = new ArrayList<>();
    ArrayList<ProcessDetails> arrivalList = new ArrayList<>();
    Scanner input = new Scanner(System.in);

    int number = 3;
    int processNum, burst, arrival, priority, turnaroundTime, waitingTime;
    int counter = 0;
    Character selection;
    //p
    do {
      if (number < 3) {
        System.out.println("*Number too small! *\n");
      } else if (number > 10){
        System.out.println("*Number too large! *\n");
      } else {}
      System.out.print("Enter the number of processes range from 3 to 10: ");
      number = input.nextInt();
    } while (number < 3 || number > 10);

    System.out.println("\nNow enter the process number, burst time, arrival time and priority for each process.");
    System.out.println("(e.g. P0 6 0 3)\n");
    for (int i = 0; i < number; i++) {
      System.out.print("P");
      processNum = input.nextInt();
      burst = input.nextInt();
      arrival = input.nextInt();
      priority = input.nextInt();
      turnaroundTime = 0;
      waitingTime = 0;
      // process.add(new ProcessDetails(processNum, burst, arrival, priority));
      // arrivalList.add(new ProcessDetails(processNum, burst, arrival, priority));
      process.add(new ProcessDetails(processNum, burst, arrival, priority, turnaroundTime, waitingTime));       // add element in the ArrayList
      // arrivalList.add(new ProcessDetails(processNum, burst, arrival, priority, turnaroundTime, waitingTime));
    }

    System.out.println("\nSelect the type of process scheduling algorithm to perform: ");
    System.out.println("a) Round Robin with Quantum 3");
    System.out.println("b) Preemptive SJF");
    System.out.println("c) Non Preemptive SJF");
    System.out.println("d) Preemptive Priority");
    System.out.println("e) Non Preemptive Priority");
    System.out.print("> ");
    selection = input.next().charAt(0);

    printTable(number, process);

    switch(selection) {
      case 'a': SchedulingAlgorithm n = new SchedulingAlgorithm(process);
                System.out.println("\n\na) Round Robin with quantum 3");
                n.roundRobin();
                break;
      case 'b': SchedulingAlgorithm psjf = new SchedulingAlgorithm(process);
				System.out.println("\n\nb) Preemptive SJF");
				psjf.preemptiveSJF();
                break;
      case 'c': SchedulingAlgorithm npSJF = new SchedulingAlgorithm(process);
                System.out.println("\n\nc) Non Preemptive SJF");
                npSJF.nonPreemptiveSJF();
                break;
      case 'd': SchedulingAlgorithm pp = new SchedulingAlgorithm(process);
                System.out.println("\n\nd) Preemptive Priority");
                pp.preemptivePriority();
                break;
      case 'e': SchedulingAlgorithm npp = new SchedulingAlgorithm(process);
      // case 'e': SchedulingAlgorithm npp = new SchedulingAlgorithm(arrivalList);
                System.out.println("\n\ne) Non Preemptive Priority");
                npp.nonPreemptivePriority();
                break;
      default:
                break;
    }

    SchedulingAlgorithm calcProcess = new SchedulingAlgorithm(process);
    // SchedulingAlgorithm calcProcess = new SchedulingAlgorithm(arrivalList);
    System.out.println("\n\na) Turnaround time for each process");
    if (selection.equals('c') || selection.equals('e') || selection.equals('d')) {
      calcProcess.calcTurnaroundTimeNP();
    } else if(selection.equals('a')){
    // } else if(selection.equals('a') || selection.equals('d')){

      calcProcess.calcTurnaroundRound();
    }else {
      System.out.println("");
      // System.out.print("might need to make and call another function cause the one i made dk applicable for preemptive and round robin or not");
    }
    // calcProcess.calcTurnaroundTimeNP(); // if not this can change back to calcTurnaroundTime() without the np ana just call like this

    System.out.println("\n\nb) Total and Average Turnaround time for the entire processes");
    System.out.println("   Total Turnaround time = " + calcProcess.calcTotalTurnaround() + "ms");
    System.out.println("   Average Turnaround time = " + calcProcess.calcAvgTurnaround() + "ms");

    System.out.println("\nc) Average Waiting time for each process");
    // if (selection.equals('c') || selection.equals('e')) {
    if (selection.equals('c') || selection.equals('e') || selection.equals('d')) {
      calcProcess.calcWaitingTimeNP();
    } else if(selection.equals('a')){
    // } else if(selection.equals('a') || selection.equals('d')){
      calcProcess.calcWaitingTimeRR();
    }else {
      System.out.println("");
      // System.out.print("might need to make and call another function cause the one i made dk applicable for preemptive and round robin or not");
    }
    // calcProcess.calcWaitingTimeNP(); // if not this can change back to calcTurnaroundTime() without the np ana just call like this

    System.out.println("\n\nd) Total and Average Waiting time for the entire processes");
    System.out.println("   Total Waiting time = " + calcProcess.calcTotalWaiting() + "ms");
    System.out.println("   Average Waiting time = " + calcProcess.calcAvgWaiting() + "ms");
  }

  public static void printTable(int number, ArrayList<ProcessDetails> process) {
    String[] title = {"Process", "Burst Time", "Arrival Time", "Priority"};

    printTableBorder(title);
    System.out.print("\n|");
    for(int i = 0; i < title.length; i++) {
      System.out.print(title[i]);
      if(i == 0){
        for(int space = 0; space < 10; space++) {
          System.out.print(" ");
        }
      } else if (i == 1) {
        for(int space = 0; space < 7; space++) {
          System.out.print(" ");
        }
      } else if (i == 2) {
        for(int space = 0; space < 5; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 9; space++) {
          System.out.print(" ");
        }
        break;
      }
      System.out.print("|");
    }
    System.out.print("|");

    printTableBorder(title);
    for(int i = 0; i < process.size(); i++) {
      System.out.print("\n|");
      System.out.print("P" + process.get(i).getProcessNum());
      for(int space = 0; space < 15; space++) {
        System.out.print(" ");
      }
      System.out.print("|");
      System.out.print(process.get(i).getBurstTime());
      if(process.get(i).getBurstTime() < 10) {
        for(int space = 0; space < 16; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 15; space++) {
          System.out.print(" ");
        }
      }
      System.out.print("|");

      System.out.print(process.get(i).getArrivalTime());
      if(process.get(i).getArrivalTime() < 10) {
        for(int space = 0; space < 16; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 15; space++) {
          System.out.print(" ");
        }
      }
      System.out.print("|");

      System.out.print(process.get(i).getPriority());
      if(process.get(i).getPriority() < 10) {
        for(int space = 0; space < 16; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 15; space++) {
          System.out.print(" ");
        }
      }
      System.out.print("|");
      printTableBorder(title);
    }

  }

  public static void printTableBorder(String[] title) {
    System.out.print("\n+");
    for(int i = 0; i < title.length; i++) {
      for(int dash = 0; dash < 18; dash++) {
        if(dash == 17) {
          System.out.print("+");
        } else {
          System.out.print("-");
        }
      }
    }
  }

}
