package ua.kpi.tef.pdc.lab03;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class ProducerConsumerSemaphore {

    static Queue<Integer> buffer = new ConcurrentLinkedQueue<>();
    static Semaphore full = new Semaphore(5);
    static Semaphore empty = new Semaphore(0);
    static Semaphore max_operations = new Semaphore(10);
    static Random random = new Random();

    static class Producer implements Runnable {
        private static int counter = 0;
        private final int id = ++counter;
        Semaphore access = new Semaphore(1);

        @Override
        public void run() {
            int tmp;
            while(max_operations.availablePermits() != 0) {
                full.acquireUninterruptibly();
                access.acquireUninterruptibly();
                max_operations.acquireUninterruptibly();
                tmp = random.nextInt(1000);
                System.out.println(id + " -  produced: " + tmp);
                buffer.add(tmp);
                access.release();
                empty.release();
            }
        }
    }

    static class Consumer implements Runnable {
        private static int counter = 0;
        private final int id = ++counter;
        Semaphore access = new Semaphore(1);

        @Override
        public void run() {
            while(max_operations.availablePermits() != 0 || !buffer.isEmpty()) {
                empty.acquireUninterruptibly();
                access.acquireUninterruptibly();
                System.out.println(id + " - consumed:  " + buffer.remove());
                access.release();
                full.release();
            }
        }
    }

    public static void main(String[] args) {
        Producer p1 = new Producer();
        Producer p2 = new Producer();
        Consumer c1 = new Consumer();
        Consumer c2 = new Consumer();

        Thread t1 = new Thread(p1);
        Thread t2 = new Thread(p2);
        Thread t3 = new Thread(c1);
        Thread t4 = new Thread(c2);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
