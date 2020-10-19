package ua.kpi.tef.pdc.lab02;

public class Solution {
// process generation 20 - 100 ms
// process handling 100 - 200 ms
// process amount 50
    public static void main(String[] args) {
        ProcessFlow processFlow1 = new ProcessFlow(50, 50); // поток процессов
        ProcessFlow processFlow2 = new ProcessFlow(50, 50);
        CPU cpu1 = new CPU(100, 200, processFlow1); // процессор
        CPU cpu2 = new CPU(100, 200, processFlow2);
        CPU cpu3 = new CPU(100, 200, processFlow1, processFlow2);

        Thread p1 = new Thread(processFlow1);
        Thread p2 = new Thread(processFlow2);
        Thread c1 = new Thread(cpu1);
        Thread c2 = new Thread(cpu2);
        Thread c3 = new Thread(cpu3);

        p1.start();
        p2.start();
        c1.start();
        c2.start();
        c3.start();

        try {
            p1.join();
            p2.join();
            c1.join();
            c2.join();
            c3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(cpu1.toString() + " " + cpu1.getHandledAmount(0));
        System.out.println(cpu2.toString() + " " + cpu2.getHandledAmount(0));
        System.out.println(cpu3.toString() + " " + cpu3.getHandledAmount(0) + " " + cpu3.getHandledAmount(1));
        System.out.println("flow 1 had: full " + processFlow1.getProcessAmount() + " processes, " + processFlow1.getCPUQueue().getMaxLength() + " max");
        System.out.println("flow 2 had: full " + processFlow2.getProcessAmount() + " processes, " + processFlow2.getCPUQueue().getMaxLength() + " max");
        System.out.println(cpu3.toString() + " of flow 1: " + 100. * cpu3.getHandledAmount(0) / processFlow1.getProcessAmount() + "%");
        System.out.println(cpu3.toString() + " of flow 2: " + 100. * cpu3.getHandledAmount(1) / processFlow2.getProcessAmount() + "%");
    }
}


/*

Программа моделирует обслуживание двух потоков процессов с
    разными параметрами,
    тремя центральными процессорами и
    двумя очередями.

Каждый процесс поступает в свою очередь и обслуживается своим процессором.
Третий процессор берет запрос на обслуживание сначала из первой очереди или (если первая очередь пустая) из второй.
Определите максимальные длины очередей и проценты процессов первого и второго потока, обслуженные третьим процессором.

*/