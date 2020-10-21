package ua.kpi.tef.pdc.lab03;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SleepingBarber {

    public static final int MAX_SEATS = 5;
    static AtomicInteger numberOfCustomers = new AtomicInteger(0);

    public static WaitingRoom waitingRoom = new WaitingRoom();

    public static Lock barber = new ReentrantLock(); // lock to access barber
    public static Condition barberAvailable = barber.newCondition();

    public static Lock seats = new ReentrantLock(); // lock to increment seats counter


    static class Customer implements Runnable {
        private static int counter = 0;
        private final int id = ++counter;
        boolean leaving = false;

        @Override
        public void run() {
            while (!leaving) { // as long as the customer is not cut
                seats.lock(); // getting access to the numberOfFreeSeats
                if (numberOfCustomers.get() < MAX_SEATS) { // if there are any free seats
                    System.out.println("Customer " + this.id + " took a seat.");
                    numberOfCustomers.incrementAndGet();
                    seats.unlock();
                    waitingRoom.notifyHaveCustomer(); // notify the barber that there is a customer

                    barber.lock();
                    try {
                        barberAvailable.await();  // wait until barber is available
                        // barber is free
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        barber.unlock();
                    }
                    leaving = true;
                } else { // there are no free seats
                    System.out.println("No free seats. Customer " + this.id + " has left.");
                    seats.unlock();
                    leaving = true;
                }
            }
        }

    }

    static class Barber implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    waitingRoom.readyToInviteCustomer();
                    seats.lock();
                    numberOfCustomers.decrementAndGet(); // one chair gets free
                    seats.unlock();

                    barber.lock();
                    try {
                        barberAvailable.signal(); // the barber is ready to cut
                        makeHaircut();
                    } finally {
                        barber.unlock();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void makeHaircut() {
            System.out.println("Barber is cutting hair");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class WaitingRoom {

        private final Lock lock = new ReentrantLock();
        private final Condition customerArrived = lock.newCondition();

        public void readyToInviteCustomer() throws InterruptedException {
            lock.lock();
            try {
                while (numberOfCustomers.get() <= 0)
                    customerArrived.await();
            } finally {
                lock.unlock();
            }
        }

        public void notifyHaveCustomer() {
            lock.lock();
            try {
                if(numberOfCustomers.get() > 0)
                    customerArrived.signal();
                else
                    return;
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String args[]) {
        Barber barber = new Barber();
        Thread thread = new Thread(barber);
        thread.start();

        Customer [] customers = new Customer[25];
        Thread [] threads = new Thread[25];
        for (int i = 1; i < 25; i++) {
            customers[i] = new Customer();
            threads[i] = new Thread(customers[i]);

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threads[i].start();
        }
    }
}
