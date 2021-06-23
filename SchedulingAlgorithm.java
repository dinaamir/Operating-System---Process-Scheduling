/*Note:
  arraylist 1 = arrivalList (I sort them according to arrivalTime then priority)
                arrivalList sort by arrival time (and priority if necessary);
  arraylist 2 = waitingQueue (named it as queue but it's actually an arraylist which has the processes that reaches but need to wait to see who's priority is highest and etc.)
                waitingQueue is made up by processes that arrives as next but chosen based on shortest burst time or priority;
  arraylist 3 = tempQueue (has the copy of the readyQueue but in arraylist form for calculation purpose.)
  Queue = readyQueue (queue is a first in first out logic list, so the first element in the queue will be executed first. Same as the name, it is a queue that is ready for execution)
          readyQueue is the final queue that is ready to be executed
*/
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Collections;
// import java.util.Comparator;
//practice
public class SchedulingAlgorithm {
  private ArrayList<ProcessDetails> arrivalList;
  private int counter = 0;
  private int burst;
  private ArrayList<ProcessDetails> waitingQueue = new ArrayList<>();       // added into ArrayList based on arrival time
  private static ArrayList<ProcessDetails> tempQueue = new ArrayList<>();   // for calculation purposes (ultimate backup)
  private Queue<ProcessDetails> readyQueue = new LinkedList<>();            // added into queue based on priority
  private int turnaroundTime;
  private int waitingTime;
  private int totalTurn;
  private float avgTurn;
  private int totalWait;
  private float avgWait;
  private Queue<ProcessDetails> forChart = new LinkedList<>();
  private static ArrayList<ProcessDetails> tempQueue2 = new ArrayList<>();
  private static ArrayList<ProcessDetails> tempQueue3 = new ArrayList<>();

  public SchedulingAlgorithm(ArrayList<ProcessDetails> arrivalList) {
    this.arrivalList = arrivalList;
  }

  public void roundRobin() {
    ProcessDetails nextProcess;
    sortBasedOnArrival2(arrivalList);               // sort based on AT
    System.out.println();

    for(int i = 0; i < arrivalList.size(); i++){
      tempQueue2.add(arrivalList.get(i));
    }

    nextProcess = nextRounRobin(counter, arrivalList);
    forChart.add(nextProcess); //ready queue
    int b = 3;
  do
  {
    if(forChart.element().getBurstTime() > 3)
    {
      // if(arrivalList.size() == 0 && forChart.size() == 1)
      // {
      //   readyQueue.add(new ProcessDetails(forChart.element().getProcessNum(), forChart.element().getBurstTime(), forChart.element().getArrivalTime(), forChart.element().getPriority())); // this is for chart
      //   tempQueue.add(new ProcessDetails(forChart.element().getProcessNum(), forChart.element().getBurstTime(), forChart.element().getArrivalTime(), forChart.element().getPriority()));
      //   forChart.poll();
      // }else{
      readyQueue.add(new ProcessDetails(forChart.element().getProcessNum(), b, forChart.element().getArrivalTime(), forChart.element().getPriority())); // this is for chart
      tempQueue.add(new ProcessDetails(forChart.element().getProcessNum(), b, forChart.element().getArrivalTime(), forChart.element().getPriority()));  // same as readyQueue
      int burstForChart = forChart.element().getBurstTime() - 3;
      arrivalList.remove(forChart.element());
      burst = burst + 3;
      for(int i = 0; i < arrivalList.size();)
      {
        if(arrivalList.get(i).getArrivalTime() <= burst)
        {
        //forChart.add(new ProcessDetails(arrivalList.get(i).getProcessNum(), arrivalList.get(i).getBurstTime()));
        forChart.add(new ProcessDetails(arrivalList.get(i).getProcessNum(), arrivalList.get(i).getBurstTime(), arrivalList.get(i).getArrivalTime(), arrivalList.get(i).getPriority()));
        arrivalList.remove(arrivalList.get(i));
        }else
        {
          i++;
        }
      }
      //forChart.add(new ProcessDetails(forChart.element().getProcessNum(), burstForChart));
      forChart.add(new ProcessDetails(forChart.element().getProcessNum(), burstForChart, forChart.element().getArrivalTime(), forChart.element().getPriority()));
      forChart.poll();
    //}
    }else
    {
      readyQueue.add(forChart.element());
      tempQueue.add(forChart.element());
      burst = burst + readyQueue.element().getBurstTime();
      arrivalList.remove(forChart.element());
      for(int i = 0; i < arrivalList.size();)
      {
        if(arrivalList.get(i).getArrivalTime() <= burst)
        {
          //forChart.add(new ProcessDetails(arrivalList.get(i).getProcessNum(), arrivalList.get(i).getBurstTime()));
          forChart.add(new ProcessDetails(arrivalList.get(i).getProcessNum(), arrivalList.get(i).getBurstTime(), arrivalList.get(i).getArrivalTime(), arrivalList.get(i).getPriority()));
          arrivalList.remove(arrivalList.get(i));
        }
        else
        {
          i++;
        }
      }
      forChart.poll();
    }
  }while(!forChart.isEmpty());
    ganttChart(readyQueue, tempQueue);
  }

  public void preemptiveSJF() {
	ProcessDetails nextProcess;
	nextProcess = nextPreemptiveSJF(counter, arrivalList);

	//This is for checking only
	// System.out.println("arrivalList after sorting: " + arrivalList);
	// System.out.println("First element: " + arrivalList.get(0));

	//This is for the total burst which is needed
	int totalBurst = 0;

	//Adding arrivalList to tempQueue3
    for(int i = 0; i < arrivalList.size(); i++) {
      tempQueue3.add(arrivalList.get(i));
    }

	//Getting totalburst
    for(int i = 0; i < arrivalList.size(); i++) {
      burst = arrivalList.get(i).getBurstTime();
      totalBurst = totalBurst + burst;
    }

	//Just checking totalburst

    tempQueue2.add(nextProcess);
	arrivalList.remove(nextProcess);

	do
	{
		if (tempQueue2.isEmpty() == false)
		{
			nextProcess = nextPreemptiveSJFburst(counter, tempQueue2);
			arrivalList.remove(nextProcess);
		}

		//This is just for checking the status only



		for(int bT = burst; bT > 0; bT--)
		{
			// System.out.print(bT + " ");
			counter++;
		}

		for (int i = 0; i < arrivalList.size(); ++i)
		{
			if(arrivalList.get(i).getArrivalTime() <= counter)
			{

				if(arrivalList.get(i).getBurstTime() < nextProcess.getBurstTime())
				{
					waitingQueue.add(arrivalList.get(i));

					break;
				}

				else
				{
					waitingQueue.add(arrivalList.get(i));
					// System.out.println("waitingQueue " + i + ": " + waitingQueue);
				}
			}
		}

		//removal of process in wait queue from main arrivalList
		for (int i = 0; i < waitingQueue.size(); ++i)
		{
			arrivalList.remove(waitingQueue.get(i));
		}

		//This is when arrivalList is empty
		if(arrivalList.isEmpty() == true && waitingQueue.isEmpty() == true && tempQueue2.size() == 1)
		{
			// System.out.println("it's finally coming to an end lololololol");
			readyQueue.add(tempQueue2.get(tempQueue2.size()-1));
			tempQueue.add(tempQueue2.get(tempQueue2.size()-1));
			tempQueue2.remove(tempQueue2.get(tempQueue2.size()-1));
			// System.out.println("tempQueue: " + tempQueue);
			// System.out.println("tempQueue2: " + tempQueue2);
			// System.out.println("arrivalList: " + arrivalList);
			// System.out.println("waitingQueue: " + waitingQueue);

			// for (int i = 0; i < readyQueue.size(); i++) {
			//   tempQueue2.add(readyQueue.element());
			// }
			// System.out.println("tempQueue: " + tempQueue);
			// System.out.println("tempQueue2: " + tempQueue2);
			// System.out.println("readyQueue: " + readyQueue);

			ganttChart(readyQueue, tempQueue);
			break;
		}

		sortBasedOnBurstTime(waitingQueue);
		nextProcess = nextPreemptiveSJFburst(counter,waitingQueue);

		tempQueue2.add(nextProcess);
		waitingQueue.remove(nextProcess);

		arrivalList.remove(nextProcess); // remove first, then later if cannot finish executing BT, add back remaining to the arrivalList



		int newBurst, oldBurst;
		int count = 0;

		for (int i = 0; i < tempQueue2.size(); ++i)
		{
			if (tempQueue2.get(i).getBurstTime() > nextProcess.getBurstTime())
			{
				burst = tempQueue2.get(0).getBurstTime();
				// System.out.println("BT: " + burst);
				for(int bT = burst; bT > 0; bT--)
				{
					// System.out.println(bT + " ");
					counter--;
				}

				burst = tempQueue2.get(0).getBurstTime();
				// System.out.println("BT: " + burst);
				for (int bT = burst; bT > 0; bT--)
				{
					if(counter != nextProcess.getArrivalTime())
					{
						// System.out.println(bT + " ");
						counter++;
						count++;
					}
				}

				newBurst = tempQueue2.get(i).getBurstTime() - count;
				if(newBurst == 0)
				{
					// System.out.println("newBurst: " + newBurst + " = 0");
					readyQueue.add(tempQueue2.get(i));
					tempQueue.add(tempQueue2.get(i));
					tempQueue2.remove(i);
				}

				else
				{
					oldBurst = tempQueue2.get(i).getBurstTime() - newBurst;
					// System.out.println("newBurst: " + newBurst);
					// System.out.println("oldBurst: " + oldBurst);
					arrivalList.add(new ProcessDetails(tempQueue2.get(i).getProcessNum(), newBurst ,tempQueue2.get(i).getArrivalTime(),tempQueue2.get(i).getPriority()));
					readyQueue.add(new ProcessDetails(tempQueue2.get(i).getProcessNum(), oldBurst ,tempQueue2.get(i).getArrivalTime(),tempQueue2.get(i).getPriority()));
					tempQueue.add(new ProcessDetails(tempQueue2.get(i).getProcessNum(), oldBurst ,tempQueue2.get(i).getArrivalTime(),tempQueue2.get(i).getPriority()));
					tempQueue2.remove(i);
				}



			}

			else
			{
				readyQueue.add(tempQueue2.get(i));
				tempQueue.add(tempQueue2.get(i));
				tempQueue2.remove(i);

			}
		}

	}while(counter != totalBurst);
  }

  public void nonPreemptiveSJF() {
    ProcessDetails nextProcess;
    sortBasedOnArrivalBurstTime(arrivalList);               // sort based on BT

    nextProcess = nextProcessNPSJF(counter,arrivalList);    // make the first process as the nextProcess
    readyQueue.add(nextProcess);                            // add the nextProcess to the RQ
    tempQueue.add(nextProcess);
    arrivalList.remove(nextProcess);                        // remove the first process

    do {
      burst = nextProcess.getBurstTime();                   // get the BT for the 1st process in RQ

      for (int bT = burst; bT > 0; bT--) {                  // set the counter for the 1st process in RQ
        counter++;
      }

      for (int index = 0; index < arrivalList.size(); index++) {
        if (arrivalList.get(index).getArrivalTime() <= counter) {
          waitingQueue.add(arrivalList.get(index));         // add process(es) where AT is <= counter, to WQ
        }
      }

      for (int i = 0; i < waitingQueue.size(); i++) {
        arrivalList.remove(waitingQueue.get(i));            // remove the process(es) from the arrivalList that exist in WQ
      }

      nextProcess = nextProcessNPSJF(counter,waitingQueue); // get the nextProcess from the WQ

      if (arrivalList.isEmpty() == true) {                  // if arrivalList is empty, break the do-while loop
        break;
      }
      else {
        readyQueue.add(nextProcess);                        // else, add the nextProcess from the WQ to RQ
        tempQueue.add(nextProcess);
        waitingQueue.remove(nextProcess);                   // remove the nextProcess from the WQ
      }
    } while(arrivalList.isEmpty() == false);

    if(arrivalList.isEmpty() == true) {
      Collections.sort(waitingQueue, ProcessDetails.processBurst);
      for (int i = 0; i < waitingQueue.size(); i++) { // if 2 processes have the same Arrival Time, compare with the Burst Time
        for (int next = i + 1; next < waitingQueue.size(); next++) {
          if (waitingQueue.get(i).getArrivalTime() == (waitingQueue.get(next).getArrivalTime())) {
            if (waitingQueue.get(i).getBurstTime() >= waitingQueue.get(next).getBurstTime()) {
              Collections.swap(waitingQueue, i, next);
            }
          }
        }
      }

      for (int i = 0; i < waitingQueue.size(); i++) {
        burst = waitingQueue.get(i).getBurstTime();         // get the BT of process(es) from WQ for each index
        for (int bT = burst; bT > 0; bT--) {
          counter++;                                        // set the counter
        }
        readyQueue.add(waitingQueue.get(i));                // add the process(es) from WQ for each index to RQ
        tempQueue.add(waitingQueue.get(i));
      }
    }

    waitingQueue.clear();                                   // clear the WQ
    ganttChart(readyQueue, tempQueue);                      // print ganttChart for NP-SJF
  }

  public void preemptivePriority() {
    ProcessDetails nextProcess;
    int totalBurst = 0;
    // System.out.println("arrivalList: " + arrivalList);
    sortBasedOnArrivalPriority(arrivalList);
    for(int i = 0; i < arrivalList.size(); i++) {
      tempQueue3.add(arrivalList.get(i));
    }

    for(int i = 0; i < arrivalList.size(); i++) {
      burst = arrivalList.get(i).getBurstTime();
      totalBurst = totalBurst + burst;
    }
    // System.out.println("totalBurst = " + totalBurst);
    // System.out.println("arrivalList: " + arrivalList);
    // System.out.println("tempQueue3: " + tempQueue3);
    nextProcess = nextProcessNPP(counter,arrivalList);
    // System.out.println("nextProcess CHOSEN: " + nextProcess);

    // System.out.println("counter: " + counter + " \narrivalList: " + arrivalList);
    // System.out.println("tempQueue: " + tempQueue);
    // System.out.println("readyQueue: " + readyQueue);
    // System.out.println("tempQueue3: " + tempQueue3);
    // System.out.println("tempQueue2: " + tempQueue2);
    // tempQueue.add(nextProcess); // change to tempQueue2

    tempQueue2.add(nextProcess);
    arrivalList.remove(nextProcess); // remove first, then later if cannot finish executing BT, add back remaining to the arrivalList
    // System.out.println("tempQueue: " + tempQueue);

    do {
      // if (tempQueue.isEmpty() == false) {
      if (tempQueue2.isEmpty() == false) {
        // nextProcess = nextProcessNPP(counter,tempQueue);
        nextProcess = nextProcessNPP(counter,tempQueue2);
        // tempQueue.add(nextProcess);
        arrivalList.remove(nextProcess); // remove first, then later if cannot finish executing BT, add back remaining to the arrivalList
      }

      // System.out.println("---------------------------------------");
      // System.out.println("nextProcess CHOSEN: " + nextProcess);
      // System.out.println("arrivalList: " + arrivalList);
      // System.out.println("tempQueue2: " + tempQueue2);
      // System.out.println("readyQueue: " + readyQueue);
      // System.out.println("waitingQueue: " + waitingQueue);
      // System.out.println("counter: " + counter);
      // System.out.println("---------------------------------------");
      burst = nextProcess.getBurstTime();
      // System.out.println("BT: " + burst);
      for (int bT = burst; bT > 0; bT--) {
        // System.out.print(bT + " ");
        counter++;
      }
      System.out.println();
      for (int index = 0; index < arrivalList.size(); index++) {
        if (arrivalList.get(index).getArrivalTime() <= counter) {
          waitingQueue.add(arrivalList.get(index));
          // System.out.println("waitingQueue " + index + ": " + waitingQueue);
        // } else {
        //   // System.out.println("not in: " + arrivalList.get(index) + " ");
        }
      }

      for (int i = 0; i < waitingQueue.size(); i++) {
        arrivalList.remove(waitingQueue.get(i));
      }

      if(arrivalList.isEmpty() == true && waitingQueue.isEmpty() == true && tempQueue2.size() == 1) {
        readyQueue.add(tempQueue2.get(tempQueue2.size()-1));
        tempQueue.add(tempQueue2.get(tempQueue2.size()-1));
        tempQueue2.remove(tempQueue2.get(tempQueue2.size()-1));
        // System.out.println("tempQueue: " + tempQueue);
        // System.out.println("tempQueue2: " + tempQueue2);
        // System.out.println("arrivalList: " + arrivalList);
        // System.out.println("waitingQueue: " + waitingQueue);

        // for (int i = 0; i < readyQueue.size(); i++) {
        //   tempQueue2.add(readyQueue.element());
        // }
        // System.out.println("tempQueue: " + tempQueue);
        // System.out.println("tempQueue2: " + tempQueue2);
        // System.out.println("readyQueue: " + readyQueue);

        ganttChart(readyQueue, tempQueue);
        break;
      }

      nextProcess = nextProcessNPP(counter,waitingQueue);
      // System.out.println("nextProcess CHOSEN from waitingQueue: " + nextProcess);
      tempQueue2.add(nextProcess);
      waitingQueue.remove(nextProcess);
      arrivalList.remove(nextProcess); // remove first, then later if cannot finish executing BT, add back remaining to the arrivalList
      // System.out.println("tempQueue: " + tempQueue);
      // System.out.println("tempQueue2: " + tempQueue2);
      // System.out.println("arrivalList: " + arrivalList);
      // System.out.println("waitingQueue: " + waitingQueue);

      // for(int i = 0, i < )
      int newBurst, oldBurst;
      int count = 0;

      for (int i = 0; i < tempQueue2.size(); i++) {
        // System.out.println("tempQueue2.get(i).getBurstTime(): " + tempQueue2.get(i).getBurstTime());
        // System.out.println("nextProcess.getBurstTime(): " + nextProcess.getBurstTime());
        // System.out.println("tempQueue2.get(i).getPriority(): " + tempQueue2.get(i).getPriority());
        // System.out.println("nextProcess.getPriority(): " + nextProcess.getPriority());
        if ((tempQueue2.get(i).getBurstTime() > nextProcess.getBurstTime() && tempQueue2.get(i).getPriority() > nextProcess.getPriority()) ||
            (tempQueue2.get(i).getBurstTime() < nextProcess.getBurstTime() && tempQueue2.get(i).getPriority() == nextProcess.getPriority()) ||
            (tempQueue2.get(i).getBurstTime() < nextProcess.getBurstTime() && tempQueue2.get(i).getPriority() > nextProcess.getPriority())) {
          burst = tempQueue2.get(0).getBurstTime();
          // System.out.println("BT: " + burst);
          for (int bT = burst; bT > 0; bT--) {
            // System.out.print(bT + " ");
            counter--;
          }
          // System.out.println("counter after reverting: " + counter);

          // burst = nextProcess.getBurstTime();
          burst = tempQueue2.get(0).getBurstTime();
          // System.out.println("BT: " + burst);
          for (int bT = burst; bT > 0; bT--) {
            if(counter != nextProcess.getArrivalTime()){
              // System.out.print(bT + " ");
              counter++;
              count++;
            }
          }
          // System.out.println("count: " + count);
          // System.out.println("counter after matching with next arrival time: " + counter);

          // newBurst = tempQueue.get(i).getBurstTime() - nextProcess.getArrivalTime();
          newBurst = tempQueue2.get(i).getBurstTime() - count;
          if(newBurst == 0) {
            // System.out.println("newBurst: " + newBurst + " = 0");
            readyQueue.add(tempQueue2.get(i));
            tempQueue.add(tempQueue2.get(i));
            tempQueue2.remove(i);

          } else {
            oldBurst = tempQueue2.get(i).getBurstTime() - newBurst;
            // System.out.println("newBurst: " + newBurst);
            // System.out.println("oldBurst: " + oldBurst);
            arrivalList.add(new ProcessDetails(tempQueue2.get(i).getProcessNum(), newBurst ,tempQueue2.get(i).getArrivalTime(),tempQueue2.get(i).getPriority()));
            readyQueue.add(new ProcessDetails(tempQueue2.get(i).getProcessNum(), oldBurst ,tempQueue2.get(i).getArrivalTime(),tempQueue2.get(i).getPriority()));
            tempQueue.add(new ProcessDetails(tempQueue2.get(i).getProcessNum(), oldBurst ,tempQueue2.get(i).getArrivalTime(),tempQueue2.get(i).getPriority()));
            tempQueue2.remove(i);
          }
          // System.out.println("readyQueue: " + readyQueue);
          // // System.out.println("tempQueue: " + tempQueue);
          // System.out.println("tempQueue2: " + tempQueue2);
          // System.out.println("new arrivalList: " + arrivalList);
          // System.out.println("counter: " + counter);
        // } else if (tempQueue2.get(i).getBurstTime() > nextProcess.getBurstTime() && tempQueue2.get(i).getPriority() < nextProcess.getPriority() ||
      } else if (tempQueue2.get(i).getBurstTime() > nextProcess.getBurstTime() && tempQueue2.get(i).getPriority() < nextProcess.getPriority()) {
                  // (tempQueue2.get(i).getBurstTime() < nextProcess.getBurstTime() && tempQueue2.get(i).getPriority() == nextProcess.getPriority()) ||
                  // (tempQueue2.get(i).getBurstTime() > nextProcess.getBurstTime() && tempQueue2.get(i).getPriority() == nextProcess.getPriority())) {
          readyQueue.add(tempQueue2.get(i));
          tempQueue.add(tempQueue2.get(i));
          tempQueue2.remove(i);
          // System.out.println("second else if");
          // System.out.println("readyQueue: " + readyQueue);
          // // System.out.println("tempQueue: " + tempQueue);
          // System.out.println("tempQueue2: " + tempQueue2);
          // System.out.println("counter: " + counter);
        } else if (tempQueue2.get(i).getBurstTime() > nextProcess.getBurstTime() && tempQueue2.get(i).getPriority() == nextProcess.getPriority()) {
          readyQueue.add(nextProcess);
          tempQueue.add(nextProcess);
          tempQueue2.remove(nextProcess);
          // System.out.println("fourth else if");
          // System.out.println("readyQueue: " + readyQueue);
          // // System.out.println("tempQueue: " + tempQueue);
          // System.out.println("tempQueue2: " + tempQueue2);
          // System.out.println("counter: " + counter);
        } else {
          // System.out.println("dk do what ");
          readyQueue.add(tempQueue2.get(i));
          tempQueue.add(tempQueue2.get(i));
          tempQueue2.remove(i);
          // System.out.println("i'm here");
          // System.out.println("readyQueue: " + readyQueue);
          // System.out.println("tempQueue: " + tempQueue);
          // System.out.println("tempQueue2: " + tempQueue2);
          // System.out.println("counter: " + counter);
        }
      }
    } while(counter != totalBurst);
    // } while(counter == totalBurst);
    // ganttChart(readyQueue, tempQueue);
  }

  public void nonPreemptivePriority() {
    ProcessDetails nextProcess;
    // ArrayList<ProcessDetails> tempQueue = new ArrayList<>();
    // System.out.println("arrivalList before sorting: " + arrivalList); // before sorting (this is based on user input)
    sortBasedOnArrivalPriority(arrivalList);
    // System.out.println("arrivalList after sorting: " + arrivalList); // after sorting, all arrival time in ascending order

    // System.out.println("counter: " + counter + "\narrivalList: " + arrivalList);
    nextProcess = nextProcessNPP(counter,arrivalList);
    // System.out.println("nextProcess: " + nextProcess);
    // waitingQueue.add(nextProcess);
    readyQueue.add(nextProcess);
    tempQueue.add(nextProcess); // tempQueue is a backup queue for readyQueue for print gantt chart
    arrivalList.remove(nextProcess);
    // System.out.println("counter: " + counter + " \narrivalList: " + arrivalList);

    do {
      burst = nextProcess.getBurstTime();
      // System.out.println("BT: " + burst);
      for (int bT = burst; bT > 0; bT--) {
        // System.out.print(bT + " ");
        counter++;
      }
      // System.out.println();
      for (int index = 0; index < arrivalList.size(); index++) {
        if (arrivalList.get(index).getArrivalTime() <= counter) {
          waitingQueue.add(arrivalList.get(index));
          // System.out.println("waitingQueue " + index +": "+ waitingQueue);
        // } else {
        //   // System.out.println("not in: " + arrivalList.get(index) + " ");
        }
      }

      for (int i = 0; i < waitingQueue.size(); i++) {
        arrivalList.remove(waitingQueue.get(i));
      }

      // System.out.println("\ncounter: " + counter + "\narrivalList: " + arrivalList);
      // System.out.println("waitingQueue: " + waitingQueue);

      nextProcess = nextProcessNPP(counter,waitingQueue);
      // System.out.println("nextProcess CHOSEN: " + nextProcess);
      if (arrivalList.isEmpty() == true) {
        break;
      } else {
        readyQueue.add(nextProcess);
        tempQueue.add(nextProcess);
        waitingQueue.remove(nextProcess);
      }
      // System.out.println("counter: " + counter + "\nwaitingQueue: " + waitingQueue);
      // System.out.println("arrivalList: " + arrivalList);
      // System.out.println("waitingQueue after removal: " + waitingQueue);
      // System.out.println("readyQueue after addition: " + readyQueue);
    } while(arrivalList.isEmpty() == false);

    if(arrivalList.isEmpty() == true) {
      // System.out.println("i'm here");
      Collections.sort(waitingQueue, ProcessDetails.processPrior); // sorting based on arrival time
      for (int i = 0; i < waitingQueue.size(); i++) { // trying to make two same arrival time compare
        for (int next = i + 1; next < waitingQueue.size(); next++) {
          if (waitingQueue.get(i).getArrivalTime() == (waitingQueue.get(next).getArrivalTime())) {
            if (waitingQueue.get(i).getPriority() >= waitingQueue.get(next).getPriority()) {
              Collections.swap(waitingQueue, i, next);
            }
          }
        }
      }
      for (int i = 0; i < waitingQueue.size(); i++) {
        burst = waitingQueue.get(i).getBurstTime();
        for (int bT = burst; bT > 0; bT--) {
          counter++;
        }
        readyQueue.add(waitingQueue.get(i));
        tempQueue.add(waitingQueue.get(i));
      }
    }

    waitingQueue.clear();
    // System.out.println("\narrivalList: " + arrivalList);
    // System.out.println("waitingQueue: " + waitingQueue);
    // System.out.println("readyQueue: " + readyQueue);
    // System.out.println("tempQueue: " + tempQueue);
    // System.out.println("counter: " + counter);

    // int size = readyQueue.size();
    // for (int i = 1; i <= size; i++) {
    //   readyQueue.remove(); // this is execution, then remove head
    //   System.out.println("readyQueue after remove head: " + readyQueue);
    // }
    ganttChart(readyQueue, tempQueue);
    // System.out.println(tempQueue);
  }

  public ArrayList sortBasedOnBurstTime(ArrayList<ProcessDetails> arrivalList) {
    Collections.sort(arrivalList, ProcessDetails.processBurst);
	return arrivalList;
  }

  public ArrayList sortBasedOnArrivalBurstTime(ArrayList<ProcessDetails> arrivalList) {
    Collections.sort(arrivalList, ProcessDetails.processArr);

    for (int i = 0; i < arrivalList.size(); i++) {
      for (int next = i + 1; next < arrivalList.size(); next++) {
        if (arrivalList.get(i).getArrivalTime() == (arrivalList.get(next).getArrivalTime())) {
          if (arrivalList.get(i).getBurstTime() > arrivalList.get(next).getBurstTime()) {
            Collections.swap(arrivalList, i, next);
          }
          else if(arrivalList.get(i).getBurstTime() == arrivalList.get(next).getBurstTime())
            if (arrivalList.get(i).getPriority() >= arrivalList.get(next).getPriority()) {
              Collections.swap(arrivalList, i, next);
          }

        }
      }
    }
    return arrivalList;
  }

  // both arrival time and priority is taken in to consideration while sorting the list
  public ArrayList sortBasedOnArrivalPriority(ArrayList<ProcessDetails> arrivalList) {
    Collections.sort(arrivalList, ProcessDetails.processArr); // sorting based on arrival time

    for (int i = 0; i < arrivalList.size(); i++) { // trying to make two same arrival time compare
      for (int next = i + 1; next < arrivalList.size(); next++) {
        if (arrivalList.get(i).getArrivalTime() == (arrivalList.get(next).getArrivalTime())) {
          if (arrivalList.get(i).getPriority() >= arrivalList.get(next).getPriority()) {
            Collections.swap(arrivalList, i, next);
          }
        }
      }
    }
    return arrivalList;
  }

  // only arrival time is taken into consideration while sorting the list
  public ArrayList sortBasedOnArrival(ArrayList<ProcessDetails> arrivalList) {
    Collections.sort(arrivalList, ProcessDetails.processArr); // sorting based on arrival time

    for (int i = 0; i < arrivalList.size(); i++)
    { // trying to make two same arrival time compare
      for (int next = i + 1; next < arrivalList.size(); next++)
      {
        if (arrivalList.get(i).getProcessNum() >= (arrivalList.get(next).getProcessNum()))
        {
            Collections.swap(arrivalList, i, next);
        }
      }
    }

    return arrivalList;
  }

  public ArrayList sortBasedOnArrival2(ArrayList<ProcessDetails> arrivalList) {
    Collections.sort(arrivalList, ProcessDetails.processArr); // sorting based on arrival time

    for (int i = 0; i < arrivalList.size(); i++)
    { // trying to make two same arrival time compare
      for (int next = i + 1; next < arrivalList.size(); next++)
      {
        if (arrivalList.get(i).getArrivalTime() >= (arrivalList.get(next).getArrivalTime()))
        {
            Collections.swap(arrivalList, i, next);
        }
      }
    }

    return arrivalList;
  }

  public ProcessDetails nextRounRobin(int counter, ArrayList<ProcessDetails> list){
    ProcessDetails head = list.get(0);
    ProcessDetails winner = head;
    for(int i = 1; i < list.size(); i++){
      if(list.get(i).getArrivalTime() <= head.getArrivalTime()){
        if(list.get(i).getArrivalTime() == head.getArrivalTime()){
          if(list.get(i).getProcessNum() < head.getProcessNum()){
            winner = list.get(i);
          }else{
            winner = head;
          }
        }else{
          winner = head;
        }
      }else {
        winner = head;
      }
    }
    return winner;
  }

  public ProcessDetails nextProcessNPSJF(int counter, ArrayList<ProcessDetails> list) {
    ProcessDetails head = list.get(0);
    ProcessDetails winner;
    winner = head;

    if (head.getArrivalTime() == 0)
      return head;
    else if (list.size() == 1)        //if there's only one element in the ArrayList
      return head;
    else if (list.size() == 2) {      //if there's two elements in the ArrayList
        if (list.get(1).getArrivalTime() > head.getArrivalTime()) {
          if (list.get(1).getPriority() == head.getPriority() || list.get(1).getBurstTime() > head.getBurstTime())
            winner = head;
          else if (list.get(1).getBurstTime() < head.getBurstTime())
            winner = list.get(1);
        }
        else if (list.get(1).getArrivalTime() == head.getArrivalTime()) {
          if (list.get(1).getBurstTime() < head.getBurstTime())
            winner = list.get(1);
          else
            winner = head;
        }
        else
          winner = list.get(1);
        return winner;
    }
    else {                            //if there's more than two elements in the ArrayList
      for (int i = 1; i < list.size(); i++) {
        if (list.get(i).getArrivalTime() > head.getArrivalTime()) {
          if (list.get(i).getPriority() == head.getPriority() || list.get(i).getBurstTime() > head.getBurstTime())
            winner = head;
          else if (list.get(i).getBurstTime() < head.getBurstTime())
            winner = list.get(i);
        }
        else if (list.get(i).getArrivalTime() == head.getArrivalTime()) {
          if (list.get(i).getBurstTime() < head.getBurstTime())
            winner = list.get(i);
          else
            winner = head;
        }
        else
          winner = list.get(i); /// <----

        for (int next = i + 1; next < list.size(); next++) {
          if (list.get(next).getArrivalTime() > winner.getArrivalTime()) {
            if (list.get(next).getPriority() == winner.getPriority() || list.get(next).getBurstTime() > winner.getBurstTime())
              return winner;
            else if (list.get(next).getBurstTime() < winner.getBurstTime()) {
              winner = list.get(next);
              return winner;
            }
          }
          else if (list.get(next).getArrivalTime() == winner.getArrivalTime()) {
            if (list.get(next).getBurstTime() < winner.getBurstTime()) {
              winner = list.get(next);
              return winner;
            }
            else
              return winner;
          }
          else {
            winner = list.get(next); // <------
            return winner;
          }
        }
      }
    }
    return null;
  }

  public ProcessDetails nextProcessNPP(int counter, ArrayList<ProcessDetails> list) {
    ProcessDetails head = list.get(0);
    ProcessDetails winner;
    winner = head;

    if (head.getArrivalTime() == 0) {
      return head;
    } else if (list.size() == 1) {
      return head;
    } else if (list.size() == 2) {
        if (list.get(1).getArrivalTime() > head.getArrivalTime()) {
          if (list.get(1).getPriority() == head.getPriority() || list.get(1).getPriority() > head.getPriority()) {
            winner = head;
          } else if (list.get(1).getPriority() < head.getPriority()) {
            winner = list.get(1);
          }
        } else if (list.get(1).getArrivalTime() == head.getArrivalTime()) {
          if (list.get(1).getPriority() < head.getPriority()) {
            winner = list.get(1);
          } else {
            winner = head;
          }
        } else {
          winner = list.get(1); // this one still thinking what to do actually
        }
        return winner;

    } else {
      for (int i = 1; i < list.size(); i++) {
        if (list.get(i).getArrivalTime() > head.getArrivalTime()) {
          if (list.get(i).getPriority() == head.getPriority() || list.get(i).getPriority() > head.getPriority()) {
            winner = head;
          } else if (list.get(i).getPriority() < head.getPriority()) {
            winner = list.get(i);
          }
        } else if (list.get(i).getArrivalTime() == head.getArrivalTime()) {
          if (list.get(i).getPriority() < head.getPriority()) {
            winner = list.get(i);
          } else {
            winner = head;
          }
        } else {
          winner = list.get(i); // this one still thinking what to do actually
        }
        for (int next = i + 1; next < list.size(); next++) {
          if (list.get(next).getArrivalTime() > winner.getArrivalTime()) {
            if (list.get(next).getPriority() == winner.getPriority() || list.get(next).getPriority() > winner.getPriority()) {
              // return winner;
              winner = winner;
            } else if (list.get(next).getPriority() < winner.getPriority()) {
              winner = list.get(next);
              // return winner;
            }
          } else if (list.get(next).getArrivalTime() == winner.getArrivalTime()) {
            if (list.get(next).getPriority() < winner.getPriority()) {
              winner = list.get(next);
              // return winner;
            } else {
              // return winner;
              winner = winner;
            }
          } else if (list.get(next).getArrivalTime() < winner.getArrivalTime()) {
            if (list.get(next).getPriority() == winner.getPriority() || list.get(next).getPriority() > winner.getPriority()) {
              // return winner;
              winner = winner;
            } else if (list.get(next).getPriority() < winner.getPriority()) {
              winner = list.get(next);
              // return winner;
            }
          } else {
            winner = list.get(next); // this one still thinking what to do actually
            // return winner;
          }
        }
        return winner;
      }
    }
    return null;
  }

//This is for the use of choosing from waiting queue based on burst
  public ProcessDetails nextPreemptiveSJFburst(int counter, ArrayList<ProcessDetails> list){
	  ProcessDetails head = list.get(0);
	  ProcessDetails winner;
	  winner = head;

	  if (head.getArrivalTime() == 0)
		  return head;
	  //if the 1st arrivalTime is not 0 we check if the size of array is more than 1
	  else if (list.size() == 1)
		  return head;
	  else if(list.size() == 2)
	  {
		  if(list.get(1).getArrivalTime() == head.getBurstTime())
		  {
			  winner = head;
		  }
		  else if(head.getArrivalTime() == list.get(1).getBurstTime())
		  {
			  winner = list.get(1);
		  }
		  else if(list.get(1).getBurstTime() < head.getBurstTime())
			  winner = list.get(1);

		  //if both have same burstTime
		  else if (list.get(1).getBurstTime() == head.getBurstTime())
		  {
			  if(list.get(1).getPriority() < head.getPriority())
				  winner = list.get(1);
			  else
				  winner = head;
		  }
		  else
			  winner = head;
		  return winner;
	  }
	  else
	  {
		  for (int i = 1; i <list.size(); ++i)
		  {
			  if(list.get(1).getProcessNum() == head.getProcessNum())
				  list.remove(list.get(1));
			  if(list.get(i).getBurstTime() < head.getBurstTime())
				  winner = list.get(1);

			  //if both have same burstTime
			  else if (list.get(i).getBurstTime() == head.getBurstTime())
			  {
				  if(list.get(i).getPriority() < head.getPriority())
					  winner = list.get(i);
				  else
					  winner = head;
			  }

			  else
				  winner = head;

			  for (int next = i + 1; next < list.size(); ++next)
			  {
				  if(list.get(next).getProcessNum() == head.getProcessNum())
					  list.remove(list.get(next));
				  if(list.get(next).getBurstTime() < head.getBurstTime())
				  {
					  winner = list.get(next);
					  return winner;
				  }

				  //if both have same burstTime
				  else if (list.get(next).getBurstTime() == head.getBurstTime())
				  {
					  if(list.get(next).getPriority() < head.getPriority())
						  winner = list.get(next);
					  else
						  winner = head;
				  }

				  else
				  {
					  winner = head;
					  return winner;
				  }
			  }
		  }
	  }
	  return null;
  }

  public ProcessDetails nextPreemptiveSJF(int counter, ArrayList<ProcessDetails> list){
	  ProcessDetails head = list.get(0);
	  ProcessDetails winner;
	  winner = head;

	  if (head.getArrivalTime() == 0)
		  return head;
	  //if the 1st arrivalTime is not 0 we check if the size of array is more than 1
	  else if (list.size() == 1)
		  return head;
	  //if size of array is 2
	  else if (list.size() == 2)
	  {
		  if(list.get(1).getArrivalTime() == head.getBurstTime())
		  {
			  winner = head;
		  }
		  //if the arrivalTime of the 2nd is greater than the first
		  else if(list.get(1).getArrivalTime() > head.getArrivalTime())
		  {
			  if(list.get(1).getBurstTime() > head.getBurstTime() )
				  winner = head;
			  else if(list.get(1).getBurstTime() < head.getBurstTime())
				  winner = list.get(1);
		  }
		  //if both have same arrival time
		  else if(list.get(1).getArrivalTime() == head.getArrivalTime())
		  {
			  if(list.get(1).getBurstTime() < head.getBurstTime())
				  winner = list.get(1);
			  else
				  winner = head;
		  }
		  //if both have same burstTime
		  else if (list.get(1).getBurstTime() == head.getBurstTime())
		  {
			  if(list.get(1).getPriority() < head.getPriority())
				  winner = list.get(1);
			  else
				  winner = head;
		  }
		  else
			  winner = list.get(1);

		  return winner;
	  }

	  //This one is if there is more than 2 in the array
	  else
	  {
		  for (int i = 1; i < list.size(); ++i)
		  {
			  //if the arrivalTime of the 2nd is greater than the first
			  if(list.get(i).getArrivalTime() > head.getArrivalTime())
			  {
				  if(list.get(i).getBurstTime() > head.getBurstTime() )
						winner = head;
				  else if(list.get(i).getBurstTime() < head.getBurstTime())
						winner = list.get(i);
			  }
			  //if both have same arrival time
			  else if(list.get(i).getArrivalTime() == head.getArrivalTime())
			  {
				  if(list.get(i).getBurstTime() < head.getBurstTime())
					  winner = list.get(i);
				  else
					  winner = head;
			  }
			  //if both have same burstTime
			  else if (list.get(i).getBurstTime() == head.getBurstTime())
			  {
				  if(list.get(i).getPriority() < head.getPriority())
					  winner = list.get(i);
				  else
					  winner = head;
			  }
			  else
				  winner = list.get(i);

			  for (int next = i + 1; next < list.size(); next++)
			  {
				  //if the arrivalTime of the 2nd is greater than the first
				  if(list.get(next).getArrivalTime() > winner.getArrivalTime())
				  {
					  if(list.get(next).getBurstTime() > winner.getBurstTime() )
							return winner;
					  else if(list.get(next).getBurstTime() < winner.getBurstTime())
					  {
						  winner = list.get(next);
						  return winner;
					  }
				  }

				  //if both have same arrival time
				  else if(list.get(next).getArrivalTime() == winner.getArrivalTime())
				  {
					  if(list.get(next).getBurstTime() < winner.getBurstTime())
					  {
						  winner = list.get(next);
						  return winner;
					  }
					  else
						  return winner;
				  }

				  //if both have same burstTime
				  else if (list.get(next).getBurstTime() == winner.getBurstTime())
				  {
					  if(list.get(next).getPriority() < winner.getPriority())
						  winner = list.get(next);
					  else
						  return winner;
				  }
				  else
				  {
					  winner = list.get(next);
					  return winner;
				  }
			  }
		  }
	  }

	  return null;
  }

  // to print the result in GanttChart
  public void ganttChart(Queue<ProcessDetails> readyQueue, ArrayList<ProcessDetails> tempQueue) {
    burst = 0;
    counter = 0;
    int size = readyQueue.size();

    System.out.print("   +-------");
    for(int i = 1; i < size; i++) {
      System.out.print("+");
      for(int dash = 0; dash < 7; dash++) {
        System.out.print("-");
      }
    }
    System.out.print("+\n");

    System.out.print("   | P" + tempQueue.get(0).getProcessNum());
    for(int space = 0; space < 4; space++) { // here must make an if else for process with no. 10 print 3 only
      System.out.print(" ");
    }

    for(int i = 1; i < size; i++) {
      System.out.print("| P" + tempQueue.get(i).getProcessNum());
      for(int space = 0; space < 4; space++) { // here must make an if else for process with no. 10 print 3 only
        System.out.print(" ");
      }
    }
    System.out.print("|");

    System.out.println();

    System.out.print("   +-------");
    for(int i = 1; i < size; i++) {
      System.out.print("+");
      for(int dash = 0; dash < 7; dash++) {
        System.out.print("-");
      }
    }
    System.out.print("+\n");

    for(int i = 0; i < size; i++) {
      if(i == 0) {
        System.out.print("     " + counter);

      } else {
        counter = counter + readyQueue.element().getBurstTime();
        System.out.print(counter);
        readyQueue.remove(); // this is execution, then remove head
      }

      if (counter < 10 && counter + readyQueue.element().getBurstTime() > 10 ) {
        for(int space = 0; space < 7; space++) { // here must make an if else for process with no. 10 print 3 only
          System.out.print(" ");
        }
      } else if (counter < 10 && counter + readyQueue.element().getBurstTime() < 10) {
        for(int space = 0; space < 7; space++) { // here must make an if else for process with no. 10 print 3 only
          System.out.print(" ");
        }

      } else if (readyQueue.size() == 1){
        for(int space = 0; space < 2; space++) { // here must make an if else for process with no. 10 print 3 only
        // for(int space = 0; space < 4; space++) { // here must make an if else for process with no. 10 print 3 only
          System.out.print(" ");
        }

      } else {
        for(int space = 0; space < 6; space++) { // here must make an if else for process with no. 10 print 3 only
          System.out.print(" ");
        }
      }
      if (readyQueue.size() == 1) {
      counter = counter + readyQueue.element().getBurstTime();
      System.out.print(counter);
      readyQueue.remove(); // this is execution, then remove head
      }
    }
  }

  public void calcTurnaroundRound() {
    for(int i = 0; i < tempQueue.size(); i++) {
      burst = tempQueue.get(i).getBurstTime();
      for (int bT = burst; bT > 0; bT--) {
        counter++;
        turnaroundTime = counter - tempQueue.get(i).getArrivalTime();
        tempQueue.get(i).setTurnaroundTime(turnaroundTime);
      }
    }
    for(int i = 0; i < tempQueue.size(); i++)
    {
      for(int j = i+1; j < tempQueue.size(); j++)
      {
        if(tempQueue.get(i).getProcessNum() == tempQueue.get(j).getProcessNum())
        {
          tempQueue.remove(tempQueue.get(i));
          j--;
        }

      }
    }
    sortBasedOnArrival(tempQueue);
    for(int i = 0; i < tempQueue.size(); i++){
      if(tempQueue2.get(i).getBurstTime() != tempQueue.get(i).getBurstTime())
      {
        tempQueue.get(i).setBurstTime(tempQueue2.get(i).getBurstTime());
      }
    }
    sortBasedOnArrival(tempQueue);
    printTurnaroundTable(tempQueue);
  }

  // used to calculate turnaround time
  public void calcTurnaroundTimeNP() { // Turnaround Time = Finish Time - Arrival Time
    // finish time = counter
    for(int i = 0; i < tempQueue.size(); i++) {
      burst = tempQueue.get(i).getBurstTime();
      for (int bT = burst; bT > 0; bT--) {
        counter++;
        turnaroundTime = counter - tempQueue.get(i).getArrivalTime();
        tempQueue.get(i).setTurnaroundTime(turnaroundTime);
      }
    }
    // return tempQueue;
    printTurnaroundTable(tempQueue);
  }

  // used to calculate total turnaround time
  public int calcTotalTurnaround() {
    for(int i = 0; i < tempQueue.size(); i++) {
      totalTurn = totalTurn + tempQueue.get(i).getTurnaroundTime();
    }
    return totalTurn;
  }

  // used to calculate average turnaround time
  public float calcAvgTurnaround() {
    avgTurn = (float)totalTurn / tempQueue.size();
    return avgTurn;
  }

  // used to calculate waiting time
  public void calcWaitingTimeNP() { // Waiting Time = Turnaround Time - Burst Time
    for(int i = 0; i < tempQueue.size(); i++) {
      waitingTime = tempQueue.get(i).getTurnaroundTime() - tempQueue.get(i).getBurstTime();
      tempQueue.get(i).setWaitingTime(waitingTime);
    }
    // return tempQueue;
    printWaitingTable(tempQueue);
  }

  public void calcWaitingTimeRR() { // Waiting Time = Turnaround Time - Burst Time
    for(int i = 0; i < tempQueue.size(); i++) {
      waitingTime = tempQueue.get(i).getTurnaroundTime() - tempQueue.get(i).getBurstTime();
      tempQueue.get(i).setWaitingTime(waitingTime);
    }
    // return tempQueue;
    for(int i = 0; i < tempQueue.size(); i++){
      if(tempQueue.get(i).getWaitingTime() < 0)
      {
        tempQueue.get(i).setWaitingTime(0);
      }
    }
    printWaitingTable(tempQueue);
  }

  // used to calculate total waiting time
  public int calcTotalWaiting() {
    for(int i = 0; i < tempQueue.size(); i++) {
      totalWait = totalWait + tempQueue.get(i).getWaitingTime();
    }
    return totalWait;
  }

  // used to calculate average waiting time
  public float calcAvgWaiting() {
    avgWait = (float)totalWait / tempQueue.size();
    return avgWait;
  }

  // to display all turnaround time values in table form together with the 4 process details
  public void printTurnaroundTable(ArrayList<ProcessDetails> tempQueue) { // this function prints a table with turnaround time.
    String[] title = {"Process", "Burst Time", "Arrival Time", "Priority", "Turnaround Time"};

    printTableBorder(title);
    System.out.print("\n   |");
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
      } else if (i == 3) {
        for(int space = 0; space < 9; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 2; space++){
          System.out.print(" ");
        }
        break;
      }
      System.out.print("|");
    }
    System.out.print("|\n");

    printTableBorder(title);
    for(int i = 0; i < tempQueue.size(); i++) {
      System.out.print("\n   |");
      System.out.print("P" + tempQueue.get(i).getProcessNum());
      for(int space = 0; space < 15; space++) {
        System.out.print(" ");
      }
      System.out.print("|");

      System.out.print(tempQueue.get(i).getBurstTime());
      if(tempQueue.get(i).getBurstTime() < 10) {
        for(int space = 0; space < 16; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 15; space++) {
          System.out.print(" ");
        }
      }
      System.out.print("|");

      System.out.print(tempQueue.get(i).getArrivalTime());
      if(tempQueue.get(i).getArrivalTime() < 10) {
        for(int space = 0; space < 16; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 15; space++) {
          System.out.print(" ");
        }
      }
      System.out.print("|");

      System.out.print(tempQueue.get(i).getPriority());
      if(tempQueue.get(i).getPriority() < 10) {
        for(int space = 0; space < 16; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 15; space++) {
          System.out.print(" ");
        }
      }
      System.out.print("|");

      System.out.print(tempQueue.get(i).getTurnaroundTime());
      if(tempQueue.get(i).getTurnaroundTime() < 10) {
        for(int space = 0; space < 16; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 15; space++) {
          System.out.print(" ");
        }
      }
      System.out.print("|\n");

      printTableBorder(title);
    }
  }

  // to display all turnaround and waiting time values in table form together with the 4 process details
  public void printWaitingTable(ArrayList<ProcessDetails> tempQueue) { // this function prints a table with turnaround time and waiting time.
    String[] title = {"Process", "Burst Time", "Arrival Time", "Priority", "Turnaround Time", "Waiting Time"};

    printTableBorder(title);
    System.out.print("\n   |");
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
      } else if (i == 3) {
        for(int space = 0; space < 9; space++) {
          System.out.print(" ");
        }
      } else if (i == 4) {
        for(int space = 0; space < 2; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 5; space++){
          System.out.print(" ");
        }
        break;
      }
      System.out.print("|");
    }
    System.out.print("|\n");

    printTableBorder(title);
    for(int i = 0; i < tempQueue.size(); i++) {
      System.out.print("\n   |");
      System.out.print("P" + tempQueue.get(i).getProcessNum());
      for(int space = 0; space < 15; space++) {
        System.out.print(" ");
      }
      System.out.print("|");

      System.out.print(tempQueue.get(i).getBurstTime());
      if(tempQueue.get(i).getBurstTime() < 10) {
        for(int space = 0; space < 16; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 15; space++) {
          System.out.print(" ");
        }
      }
      System.out.print("|");

      System.out.print(tempQueue.get(i).getArrivalTime());
      if(tempQueue.get(i).getArrivalTime() < 10) {
        for(int space = 0; space < 16; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 15; space++) {
          System.out.print(" ");
        }
      }
      System.out.print("|");

      System.out.print(tempQueue.get(i).getPriority());
      if(tempQueue.get(i).getPriority() < 10) {
        for(int space = 0; space < 16; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 15; space++) {
          System.out.print(" ");
        }
      }
      System.out.print("|");

      System.out.print(tempQueue.get(i).getTurnaroundTime());
      if(tempQueue.get(i).getTurnaroundTime() < 10) {
        for(int space = 0; space < 16; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 15; space++) {
          System.out.print(" ");
        }
      }
      System.out.print("|");

      System.out.print(tempQueue.get(i).getWaitingTime());
      if(tempQueue.get(i).getWaitingTime() < 10) {
        for(int space = 0; space < 16; space++) {
          System.out.print(" ");
        }
      } else {
        for(int space = 0; space < 15; space++) {
          System.out.print(" ");
        }
      }
      System.out.print("|\n");

      printTableBorder(title);
    }
  }

  public void printTableBorder(String[] title) {
    System.out.print("   +");
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
