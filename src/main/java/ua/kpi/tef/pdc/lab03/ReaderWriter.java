package ua.kpi.tef.pdc.lab03;

import java.util.concurrent.atomic.AtomicInteger;

public class ReaderWriter {
    static AtomicInteger readCount = new AtomicInteger(0);
    static volatile boolean accessible = true;

    static class Reader implements Runnable {
        @Override
        public void run() {
            try {
                while (true) { // when reading, only readers can access storage
                    if(accessible) {
                        if(readCount.incrementAndGet() == 1)
                            accessible = false;
                        accessStorage("reading");
                        if(readCount.decrementAndGet() == 0)
                            accessible = true;
                    }
                }

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static class Writer implements Runnable {
        @Override
        public void run() { // when writing, nobody can access storage
            try {
                while (true) {
                    if(accessible) {
                        accessible = false;
                        accessStorage("writing");
                        accessible = true;
                    }
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void accessStorage(String message) throws InterruptedException{
        System.out.println(message);
        Thread.sleep(100);
    }

    public static void main(String[] args) {
        Reader reader1 = new Reader();
        Reader reader2 = new Reader();
        Reader reader3 = new Reader();
        Writer writer1 = new Writer();

        Thread t1 = new Thread(reader1);
        Thread t2 = new Thread(reader2);
        Thread t3 = new Thread(reader3);
        Thread t4 = new Thread(writer1);

        t1.start();
        t3.start();
        t2.start();
        t4.start();
    }

}
