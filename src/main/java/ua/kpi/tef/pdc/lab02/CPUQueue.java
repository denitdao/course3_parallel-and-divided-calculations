package ua.kpi.tef.pdc.lab02;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

// очередь для процессов
public class CPUQueue {
    private Queue<CPUProcess> queue; // очередь из процессов
    private int maxLength = 0; // максимальный размер очереди за все время

    public CPUQueue(){
        queue = new ConcurrentLinkedQueue<>();
    }

    public synchronized void add(CPUProcess process) {
        maxLength = Math.max(maxLength, queue.size());
        queue.add(process);
    }

    public synchronized CPUProcess remove(){
        return queue.remove();
    }

    public int getMaxLength() {
        return maxLength;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public int getSize() {
        return queue.size();
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}
