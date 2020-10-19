package ua.kpi.tef.pdc.lab01;

import java.util.Arrays;

public class NormSolution {

    static final int T = 10; // number of threads
    static final int N = 10000000; // vector length
    static final int step = N / T;
    static int[] vector = new int[N];

    public static class LInfNorm extends Thread {
        private int vector[], local_norm = 0, lower, upper;

        public LInfNorm(int[] vector, int lower, int upper){
            this.lower = lower;
            this.upper = upper;
            this.vector = vector;
        }

        public void run() {
            calculateNorm();
        }

        public void calculateNorm() {
            for (int i = lower; i < upper; i++) {
                int item = vector[i];
                local_norm = Math.max(Math.abs(item), local_norm);
            }
        }

        public int getLocal_norm() {
            return local_norm;
        }
    }

    static public void MultithreadedSolution(){
        long start = System.nanoTime();
        LInfNorm[] calc_threads = new LInfNorm[T];
        for (int i = 0; i < T; i++) { // create threads
            calc_threads[i] = new LInfNorm(vector, i * step, (i + 1) * step);
        }
        for (LInfNorm calc_thread : calc_threads) { // run local norm calculations
            calc_thread.start();
        }
        int norm = 0;
        for (int i = 0; i < T; i++) { // find global norm
            try {
                calc_threads[i].join();
                norm = Math.max(calc_threads[i].getLocal_norm(), norm);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.nanoTime();
        System.out.println("Multithreaded Norm:   max|xi| = " + norm + ", in " + (end - start) / 1000000f + " ms."); // + ", in " + (end - start) / 1000000f + " ms."
    }

    static public void SingleThreadedSolution(){
        long start = System.nanoTime();
        LInfNorm calc_linear = new LInfNorm(vector, 0, N);
        calc_linear.calculateNorm();
        long end = System.nanoTime();
        System.out.println("Single Threaded Norm: max|xi| = " + calc_linear.getLocal_norm() + ", in " + (end - start) / 1000000f + " ms.");
    }

    static public void PreHeat(){
        LInfNorm calc_linear = new LInfNorm(vector, 0, N);
        calc_linear.calculateNorm();
    }

    public static void main(String[] args) {
        // initialise variables
        Arrays.setAll(vector, i -> (int) (Math.random() * 401) - 200);

        PreHeat();
        PreHeat();

        MultithreadedSolution();

        SingleThreadedSolution();
    }
}
