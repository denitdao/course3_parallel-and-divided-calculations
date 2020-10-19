package ua.kpi.tef.pdc.lab01;

//import org.openjdk.jmh.annotations.*;
import java.util.Arrays;
//import java.util.concurrent.TimeUnit;

public class TestBenchmark {
    static int val = 3;
    /*@Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 2)
    @Warmup(iterations = 4)
    @Measurement(iterations = 4)*/
    public static void testMethod(){
        int[] vector = new int[1000000];
        val = 5;
        Arrays.setAll(vector, i -> (int) (Math.random() * 401) - 200);
    }

    public static void main(String[] args) {
        testMethod();
    }
}
