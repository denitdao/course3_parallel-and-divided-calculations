package ua.kpi.tef.pdc.lab02;

// процесс
public class CPUProcess {
    private static int counter = 0;
    private final int id = ++counter; // идентификатор процесса

    private int processGenerationInterval; // интервал времени между генерированиями процессов

    public CPUProcess(int min, int max){ // получаем границы для интервала между генерированиями процессов
        this.processGenerationInterval = randomize(min, max);
    }

    public int getInterval(){
        return processGenerationInterval;
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString() {
        return "process[" + id + "]";
    }

    public int randomize(int min, int max){
        return (int) (min + Math.random() * (max - min + 1));
    }
}
