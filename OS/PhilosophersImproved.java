import java.util.Random;
import java.util.concurrent.Semaphore;


class PhilosophersImproved implements Runnable
{
	private static final intB_PHILOSOPHERS = 5;
	private static final Semaphore[] forks = new Semaphore[NB_PHILOSOPHERS];
	private static Random rand = new Random();

	private int id;

	public PhilosophersImproved(int id) {
		this.id = id;
		forks[id] = new Semaphore(1);
	}
	
	private void nap() {			//Make philosopher thread sleep for a while (e.g. to simulate thinking or eating for a short period)
		try {
			Thread.sleep(rand.nextInt(10) * 1000);
		} catch (InterruptedException e) {
			System.err.println("One philosopher thread died :(");
			System.exit(1);
		};
	}

	private void think() {
		this.nap();
	}

	private void eat() {

		System.out.println(this + " is hungry.");

		while(true){
			// Try to take fork on the left
			try{
				forks[id].acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(this + " Holding left fork");
			try {
				Thread.sleep(6000);				//Philosopher is pausing before going for the other fork
			} catch (InterruptedException e) {
				System.err.println("One philosopher thread died :(");
				System.exit(1);
			};			
			
			// Try to take fork on the right
			if(!forks[(id + 1) % NB_PHILOSOPHERS].tryAcquire()){
				System.out.println(this + " could not take right fork");
				forks[id].release();
				think();
			}
			else{
				System.out.println(this + " Holding right fork");
				break;
			}
		}

		System.out.println(this + " is eating.");
		this.nap();			//Philosopher is eating for a while
		
		forks[(id + 1) % NB_PHILOSOPHERS].release();
		forks[id].release();

		System.out.println(this + " has finished eating.");
	}

	public String toString() {
		return "[Philosopher " + id + "]";
	}

	public void run() {
		System.out.println(this + " enters the dining room.");			
		for (int i = 0; i < 5 ; i++) {
			think();
			eat();
		}
	}

	public static void main(String [] args) {
		Thread philosophers[] = new Thread[NB_PHILOSOPHERS];
		System.out.println("Start");

		for (int i = 0 ; i < NB_PHILOSOPHERS ; i++) {
			philosophers[i] = new Thread(new PhilosophersImproved(i)); 
		}
		
		for (int i = 0 ; i < NB_PHILOSOPHERS ; i++) {
			philosophers[i].start();
		}		

		try {
			for (int i = 0 ; i < NB_PHILOSOPHERS ; i++)
				philosophers[i].join();
		} 
		catch (InterruptedException e) {
			System.err.println("One philosopher thread died :(");
			System.exit(1);
		} 
		System.out.println("End");
	}
}	
