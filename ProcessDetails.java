import java.util.ArrayList;
// import java.util.Collections;
import java.util.Comparator;
public class ProcessDetails {
  private int processNum;
  private int burst;
  private int arrival;
  private int priority;
  private int timeQT;
  // private int counter = 0;

  private int turnaroundTime;
  private int waitingTime;

  // public ProcessDetails(int processNum, int burst, int arrival, int priority) {
  //   this.processNum = processNum;
  //   this.burst = burst;
  //   this.arrival = arrival;
  //   this.priority = priority;
  // }
  //pra
  public ProcessDetails(int processNum, int burst, int arrival, int priority, int turnaroundTime, int waitingTime) {
    this.processNum = processNum;
    this.burst = burst;
    this.arrival = arrival;
    this.priority = priority;
    this.turnaroundTime = turnaroundTime;
    this.waitingTime = waitingTime;
  }

  public ProcessDetails(int processNum, int burst, int arrival, int priority){
    this.processNum = processNum;
    this.burst = burst;
    this.arrival = arrival;
    this.priority = priority;
  }

  public int getProcessNum() {
    return processNum;
  }

  public void setProcessNum(int processNum) {
    this.processNum = processNum;
  }

  public int getBurstTime() {
    return burst;
  }

  public void setBurstTime(int burst) {
    this.burst = burst;
  }

  public int getArrivalTime() {
    return arrival;
  }

  public void setArrivalTime(int arrival) {
    this.arrival = arrival;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public int getTurnaroundTime() {
    return turnaroundTime;
  }

  public void setTurnaroundTime(int turnaroundTime) {
    this.turnaroundTime = turnaroundTime;
  }
  
  public int getWaitingTime() {
    return waitingTime;
  }

  public void setWaitingTime(int waitingTime) {
    this.waitingTime = waitingTime;
  }

  public static Comparator<ProcessDetails> processArr = new Comparator<ProcessDetails>() {
    public int compare(ProcessDetails p1, ProcessDetails p2) {
    int arrival1 = p1.getArrivalTime();
    int arrival2 = p2.getArrivalTime();
    return arrival1 - arrival2;
  }};

  public static Comparator<ProcessDetails> processBurst = new Comparator<ProcessDetails>() {
    public int compare(ProcessDetails p1, ProcessDetails p2) {
    int burst1 = p1.getBurstTime();
    int burst2 = p2.getBurstTime();
    return burst1 - burst2;
  }};

  public static Comparator<ProcessDetails> processPrior = new Comparator<ProcessDetails>() {
    public int compare(ProcessDetails p1, ProcessDetails p2) {
    int priority1 = p1.getPriority();
    int priority2 = p2.getPriority();
    return priority1 - priority2;
  }};

  public String toString() {
     return Integer.toString(processNum) + Integer.toString(burst) +
            Integer.toString(arrival) + Integer.toString(priority) +
            Integer.toString(turnaroundTime) + Integer.toString(waitingTime);
  }
}
