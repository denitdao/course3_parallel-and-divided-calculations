package ua.kpi.tef.pdc.lab05;

import java.util.ArrayList;
import java.util.List;

public class GaussJordanElimination {

    static class Calculate extends Thread {

        float[] targetRow, usedRow;
        int n, diagId;

        public Calculate(float[] targetRow, float[] usedRow, int n, int diagId) {
            this.targetRow = targetRow;
            this.usedRow = usedRow;
            this.n = n;
            this.diagId = diagId;
        }

        @Override
        public void run() {
            float p = targetRow[diagId] / usedRow[diagId];
            for (int k = 0; k <= n; k++)
                targetRow[k] = targetRow[k] - (usedRow[k]) * p;
        }
    }

    static int OrganizeMatrix(float[][] m, int n) {
        int flag = 0;
        for (int i = 0; i < n; i++) {
            if (m[i][i] == 0) {
                int c = 1;
                while ((i + c) < n && m[i + c][i] == 0)
                    c++;
                if ((i + c) == n) {
                    flag = 1;
                    break;
                }
                for (int k = 0; k <= n; k++) {
                    float temp = m[i][k];
                    m[i][k] = m[i + c][k];
                    m[i + c][k] = temp;
                }
            }
        }
        return flag;
    }

    static void TransformMatrix(float[][] m, int n) {
        for (int i = 0; i < n; i++) { // on each column
            List<Thread> threads = new ArrayList<>();
            for (int j = 0; j < n; j++) { // set all items in a column to zero
                if (i != j) {
                    threads.add(new Calculate(m[j], m[i], n, i));
                }
            }
            threads.forEach(Thread::start);
            threads.forEach(t -> {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    static int CheckConsistency(float[][] m, int n) {
        int flag = 3; // flag == 3 for No solution
        for (int i = 0; i < n; i++) {
            int j;
            float sum = 0;
            for (j = 0; j < n; j++)
                sum = sum + m[i][j];
            if (sum == m[i][j]) {
                flag = 2; // flag == 2 for infinite solution
                break;
            }
        }
        return flag;
    }

    static void PrintMatrix(float[][] m, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n; j++)
                System.out.print(m[i][j] + ((j == n - 1) ? " | " : " "));
            System.out.println();
        }
        System.out.println();
    }

    static void PrintResult(float[][] m, int n, int flag) {
        System.out.print("Result is: ");
        if (flag == 2)
            System.out.println("Infinite Solutions Exists");
        else if (flag == 3)
            System.out.println("No Solution Exists");
        else {
            for (int i = 0; i < n; i++)
                System.out.print(m[i][n] / m[i][i] + " ");
        }
    }

    public static void main(String[] args) {
        System.out.println("Given Matrix is: ");
        int n = 3;
        float[][] m = {
                {2, 1, 2, 10},
                {1, 0, 1, 8},
                {3, 1, -1, 2}};
        PrintMatrix(m, n);

        // Moving Rows
        System.out.println("Organized Matrix is: ");
        int flag = OrganizeMatrix(m, n);
        PrintMatrix(m, n);

        // Performing Matrix transformation
        TransformMatrix(m, n);
        if (flag == 1)
            flag = CheckConsistency(m, n);

        // Printing Final Matrix
        System.out.println("Final Augmented Matrix is: ");
        PrintMatrix(m, n);
        PrintResult(m, n, flag);
    }
}
/*
6.	Жордана з вибором головного елемента по стовпцю





https://math.libretexts.org/Bookshelves/Applied_Mathematics/Book%3A_Applied_Finite_Mathematics_(Sekhon_and_Bloom)/02%3A_Matrices/2.02%3A_Systems_of_Linear_Equations_and_the_Gauss-Jordan_Method
*/