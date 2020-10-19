package ua.kpi.tef.pdc.lab02;

import java.util.List;

/*
класс CPU – поток обслуживания процесса центральным процессором
*/

public class CPU implements Runnable {
    private static int counter = 0;
    private final int id = ++counter; // идентификатор процесса

    private final int min, max; // промежуток для времени, на которое процессор будет засыпать, обрабатывая процессы
    private ProcessFlow[] processFlows; // обслуживаемые очереди процессов
    private volatile int[] handledAmount; // сколько процессов обработал

    public CPU(int min, int max, ProcessFlow... processFlows){
        this.min = min;
        this.max = max;
        this.processFlows = processFlows;
        this.handledAmount = new int[processFlows.length];
    }

    @Override
    public void run() { // поток запущен
        if (processFlows.length == 1) {
            handleOneFlow();
        } else {
            handleTwoFlows();
        }
    }

    public void handleOneFlow() { // для процессора с одним источником процессов
        CPUQueue cpuQueue = processFlows[0].getCPUQueue();
        while(!cpuQueue.isEmpty() || processFlows[0].isFilling()) { // пока очередь не опустошится, будучи наполненой до конца
            if(cpuQueue.isEmpty()) // если в очереди не успел появится процесс
                waitForProcess(10);
            else
                handle(cpuQueue, 0);
        }
    }

    public void handleTwoFlows() { // для процессора с двумя источниками процессов
        CPUQueue cpuQueue1 = processFlows[0].getCPUQueue();
        CPUQueue cpuQueue2 = processFlows[1].getCPUQueue();
        while(!cpuQueue1.isEmpty() || processFlows[0].isFilling() || !cpuQueue2.isEmpty() || processFlows[1].isFilling()) { // пока очередь не опустошится, будучи наполненой до конца
            if(!cpuQueue1.isEmpty()) // если в 1 очереди не успел появится процесс
                handle(cpuQueue1, 0);
            else if (!cpuQueue2.isEmpty()) // если в 2 очереди не успел появится процесс
                handle(cpuQueue2, 1);
            else
                waitForProcess(10);
        }
    }

    public void handle(CPUQueue cpuQueue, int flow) { // выполняем процесс
        try {
            handledAmount[flow]++;
            cpuQueue.remove(); // удаляем процесс
//            System.out.println(this.toString() + " handled " + cpuProcess.toString());
            Thread.sleep(randomize(min, max)); // выполняем обработку процесса - спим
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitForProcess(int time) { // задержка для ожидания появления хоть одного процесса в очереди
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int randomize(int min, int max){
        return (int) (min + Math.random() * (max - min + 1));
    }

    public int getHandledAmount(int flow) {
        return handledAmount[flow];
    }

    @Override
    public String toString() {
        return "cpu[" + id + "]";
    }
}
