import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Semaphore;


class Baboon implements Runnable
{
    private static final int BaboonCount = 50;
    private static int killCount = 0;
    private static final Semaphore[] rope = new Semaphore[BaboonCount];
    private static Random rand = new Random();
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    private static ArrayList crossing = new ArrayList();
    private static ArrayList onEast = new ArrayList();
    private static ArrayList onWest = new ArrayList();
    private static ArrayList priority = new ArrayList();

    private int id;
    private int age;
    private boolean side = false; //    True - on East  False - on West

    public Baboon(int id) {
        this.id = id;
        this.age = 0;
        rope[id] = new Semaphore(1);
        if (id % 2 == 0){
            this.side = true;
            onEast.add(this);
        } // Baboon on East.
        else{
            onWest.add(this);
        }

    }

    private void nap() {			//Make Baboon thread sleep for a while (e.g. to simulate thinking or eating for a short period)
        try {
            Thread.sleep(rand.nextInt(10) * 1000);
        } catch (InterruptedException e) {
            System.err.println("One baboon thread died :(");
            System.exit(1);
        }
    }

    private void eatBanana() {
        this.nap();
    }

    private void takeRope(){
        try {
            crossing.add(this);
            rope[id].acquire();

        } catch (InterruptedException e) {
            System.out.println(" A baboon thread died. ");
        }
    }

    private synchronized void cross() {

        System.out.println(this + " wants to cross.");

        while(true){
            if (priority.isEmpty() || priority.contains(this)) {
                if(crossing.isEmpty()){
                    takeRope();
                    break;
                }
                else {
                    try {
                        if (side){
                            crossing.removeAll(Collections.singleton(null));
                            if (Collections.disjoint(crossing, onWest)){ // if no westward baboons
                               takeRope();
                               break;
                            }
                        }
                        if (!side){
                            if (Collections.disjoint(crossing, onEast)){ // if no eastward baboons
                                takeRope();
                                break;
                            }
                        }
                    } catch (Exception e) {

                    }
                    this.nap();
                }
            }
            this.age++;
            if (this.age == 5){
                priority.add(this);
                System.out.println(ANSI_RED + this + " has priority!" + ANSI_RESET);}
        }

        System.out.println(ANSI_GREEN + this + " is crossing." + ANSI_RESET);
        try {
            System.out.println(ANSI_YELLOW + crossing + ANSI_RESET);
        } catch (Exception e) {

        }
        this.age = 0;
        if (priority.contains(this)){priority.remove(this);}
        this.nap();			//Baboon is crossing for a while

        rope[id].release();
        crossing.remove(this);

        System.out.println(this + " has finished crossing.");
        this.swapSides();
    }

    private synchronized void swapSides() {
        this.side = !this.side;
        if (this.side){onWest.remove(this); onEast.add(this);}
        if(!this.side){onEast.remove(this); onWest.add(this);}

        if (this.id % 2 == 0 && this.side){
            kill();
        }
        else if (!(this.id % 2 == 0) && !this.side){
            kill();
        }
    }

    private void kill() {
        System.out.println(this + " has crossed twice. ");
        killCount++;
        if (killCount == BaboonCount){
            System.out.println(ANSI_YELLOW + " All baboons successfully crossed twice" + ANSI_RESET);
            System.exit(0);
        }
        while (true) {
            try {
                this.wait(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String toString() {
        String stringSide = "(West)";
        if(this.side){stringSide = "(East)";}
        return "[Baboon " + id + " " + stringSide + "]";
    }

    public void run() {
        System.out.println(this + " approaches the rope.");
        for (int i = 0; i < 5 ; i++) {
            eatBanana();
            cross();
        }
    }

    public static void main(String [] args) {

        Thread baboons[] = new Thread[BaboonCount];
        System.out.println("Start");

        for (int i = 0; i < BaboonCount; i++) {
            baboons[i] = new Thread(new Baboon(i));
        }


        for (int i = 0; i < BaboonCount; i++) {
            baboons[i].start();
        }

        try {
            for (int i = 0; i < BaboonCount; i++)
                baboons[i].join();
        }
        catch (InterruptedException e) {
            System.err.println("One baboon thread died :(");
            System.exit(1);
        }
        System.out.println("End");
    }
}
