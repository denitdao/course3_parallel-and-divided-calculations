package ua.kpi.tef.pdc.lab03;

public class DiningPhilosophers {

    static class Philosopher implements Runnable {
        private static int counter = 0;
        private final int id = ++counter;

        private Object leftFork;
        private Object rightFork;

        public Philosopher(Object leftFork, Object rightFork) {
            this.leftFork = leftFork;
            this.rightFork = rightFork;
        }

        private void doAction (String action) throws InterruptedException {
            System.out.println("Philosopher " + id + " " + action);
            Thread.sleep(50);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    doAction("thinking");
                    synchronized (leftFork) {
                        doAction("took left fork");
                        synchronized (rightFork) {
                            doAction("took right fork. Starts eating");
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Philosopher[] philosophers = new Philosopher[5];
        Object[] forks = new Object[5];

        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Object();
        }

        for (int i = 0; i < philosophers.length; i++) {
            Object leftFork = forks[i];
            Object rightFork = forks[(i + 1) % forks.length];

            if (i == philosophers.length - 1) {
                // The last philosopher picks up the right fork first
                philosophers[i] = new Philosopher(rightFork, leftFork);
            } else {
                philosophers[i] = new Philosopher(leftFork, rightFork);
            }

            Thread t = new Thread(philosophers[i], "Philosopher " + (i + 1));
            t.start();
        }
    }
}
