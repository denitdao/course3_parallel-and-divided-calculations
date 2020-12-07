package ua.kpi.tef.pdc.lab05;

public class GaussJordanElimination {

    static void PrintMatrix(float[][] m, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n; j++)
                System.out.print(m[i][j] + " ");
            System.out.println();
        }
        System.out.println();
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
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Excluding all i == j
                if (i != j) {
                    // Converting Matrix to reduced row echelon form(diagonal matrix)
                    float p = m[j][i] / m[i][i];
                    for (int k = 0; k <= n; k++)
                        m[j][k] = m[j][k] - (m[i][k]) * p;
                }
            }
        }
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

    public static void main(String[] args) {
        System.out.println("Given Matrix is: ");
        int n = 3;
        float[][] m = {
                {0, 2, 1, 4},
                {1, 1, 2, 6},
                {2, 1, 1, 7}};
                    /*{
                {0, 2, 1, 4},
                {1, 1, 2, 6},
                {2, 1, 1, 7}};*/
        PrintMatrix(m, n);

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
http://e-maxx.ru/algo/linear_systems_gauss

https://www.geeksforgeeks.org/program-for-gauss-jordan-elimination-method/

https://math.libretexts.org/Bookshelves/Applied_Mathematics/Book%3A_Applied_Finite_Mathematics_(Sekhon_and_Bloom)/02%3A_Matrices/2.02%3A_Systems_of_Linear_Equations_and_the_Gauss-Jordan_Method
*/