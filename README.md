## Problem Statement
A very common use of parallel computing is the dividing up of a large number of similar computations and spreading them across multiple processors to decrease run time. For this problem specifically, the goal is to utilize multi threading in order to compute the roots of a given number of quadratic equations. This project demonstrates parallel computing by distributing a large number of quadratic equation root calculations across multiple threads, significantly reducing overall computation time.

> **Note**: This project was originally part of a private GitLab repository and has been migrated to GitHub as an independent repository. As a result, the commit history starts from the migration point.

## Concepts Demonstrated
- Multithreading and Concurrency
- ExecutorService and Thread Pools
- CountDownLatch Synchronization
- Shared Resource Management (Buffer)
- Performance Statistics Collection
- Synchronized Methods for Thread Safety
- Command-line Interface (CLI) Interaction
- Exception Handling

## Developer Documentation
The MasterThread class is the main driver of the program. It manages the buffer and the slave threads, and provides statistics about the computation of individual threads. The use of the inner ThreadStats class made it easy to keep track of how many equations each thread processed and the thread's average time it took to process one. The MasterThread uses an ExecutorService with a fixed number of threads to manage the slave threads. It generates random coefficients for quadratic equations and adds them to the equation buffer. The slave threads are then launched to solve the quadratic equations concurrently, as they are being generated. During the process of figuring this part out, I accidently created all the equations before actually executing the slave threads. This of course, was not how this program was intended to function. I was able to fix this by executing the slave threads before creating the equations, thus allowing for equations to be processed as they got generated. The MasterThread waits for all slave threads to finish their processing using a CountDownLatch with a count equal to the number of threads being used (10 in this case) and measures the total time taken to solve all equations. It then shuts down the executor service, allowing time for threads to complete their tasks.
<br>
The SlaveThread class represents the worker threads that solve the quadratic equations. Each SlaveThread retrieves quadratic equations from a shared buffer, solves them, and stores the resulting roots back into the buffer. Thread-specific statistics are updated, and the CountDownLatch is used for synchronization to signal the completion of equation processing. Each SlaveThread continues running until all equations in the shared buffer are processed.
<br>
The Buffer class represents a buffer that holds quadratic equations and their corresponding roots. It provides methods for adding equations and roots to the buffer, as well as retrieving them in a synchronized manner. The buffer uses a circular array data structure to store the equations and roots, and uses the 'synchronized' keyword to ensure thread safety.
<br>
During the development process, I experienced issues with the executor service shutting down prematurely and as a result not all of the intended number of equations were being processed. This was a troubling issue and took time in order to figure out. The use of CountDownLatch was able to solve this, luckily. Inside MasterThread, the latch ensures that the MasterThread waits until all SlaveThread instances get finished before it proceeded to shutdown the ExecutorService. The use of latch.await() made it a lot easier to ensure that I would not run into these issues I was facing before. <br>
In the SlaveThread class, the CountDownLatch is passed as a parameter to the constructor and stored as an instance variable in order for it to be used effectively inside the class.
After a SlaveThread finishes processing an equation, it calls latch.countDown(). This reduces the count of the CountDownLatch by one.
When all SlaveThread instances have finished processing and each has called latch.countDown(), the count of the CountDownLatch will reach zero. This releases the latch and allows the MasterThread to continue execution. 


![image](https://github.com/user-attachments/assets/9f74f363-1288-4359-9bb7-f043d7661cf4)


## JavaDocs
Java Documents are visible using a local server on the machine. You must have at least the doc folder on your local machine. To access them:
1. Open a terminal
2. Navigate to the `doc/` directory
   ```cd path\to\the\project\doc```
3. Start a local server:
   ```python -m http.server 8000```
4. Open your browser and visit:
   ```http://localhost:8000/package-summary.html```
 
[Java Docs can be accessed here!](https://github.com/jarkin0513/Java-Multithreading/blob/main/doc/package-summary.html) 
 

## User Documentation

<dl>
 
 <dt> Launching the Program </dt>
  <dd> 
To start the program navigate to the Main.java file and click the run button from your IDE.   
The run button should look like a green arrow, or something similar to that.
  </dd>

 <dt> Using the program </dt>
 <dd>
  Once the program is running, you will be presented with a menu inside the command-line window that offers the following: <br>
Enter 1 to generate and solve roots for 30 randomly generated sets of coefficients. <br>
Enter 2 to generate and solve roots for 3000 randomly generated sets of coefficients. <br>
To select an option, enter '1' or '2' inside the console and press enter. Attempting to enter anything else will result in the console printing a message to try again. <br>
If you select '1', the program will print the roots for each of the 30 equations it generated. <br>
If you select '2', the program will print out statistics about each of the 10 threads used to process the equations. These statistics include how many equations each of the threads processed as well as the average time it took each of them to process an equation.
 </dd>

<dt> Exiting the Program </dt>
 <dd> 
The program will automatically terminate after displaying the results. 
 </dd>
 
</dl>


 

## Source Code
[Click here to view the source code](https://github.com/jarkin0513/Java-Multithreading/tree/main/src)
