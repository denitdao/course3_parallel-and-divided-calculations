package ua.kpi.tef.pdc.lab02;

// наполнение очереди процессами
public class ProcessFlow implements Runnable{

    private CPUQueue cpuQueue;
    private final int processAmount; // количество процессов для добавления в очередь
    private boolean filling = true; // наполнилась ли очередь

    public ProcessFlow(int min, int max) {
        cpuQueue = new CPUQueue();
        processAmount = randomize(min, max);
    }

    @Override
    public void run() { // генерируем процессы, добавляя их в очередь
        CPUProcess process = null;
        for (int i = 0; i < processAmount; i++) {
            process = new CPUProcess(20, 100); // создаем процесс и генерируем для него временной промежуток
            cpuQueue.add(process);
            try {
                Thread.sleep(process.getInterval()); // ожидаем перед генерацией следующего процесса
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        filling = false; // очердь наполнена
        synchronized (this) {
            notifyAll(); // уведомить все спящие потоки ?
        }
    }

    public CPUQueue getCPUQueue() {
        return cpuQueue;
    }

    public int getProcessAmount(){
        return processAmount;
    }

    public boolean isFilling() {
        return filling;
    } // происходит ли наполнение

    public int randomize(int min, int max){
        return (int) (min + Math.random() * (max - min + 1));
    }
}
