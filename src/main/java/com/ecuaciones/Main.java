package com.ecuaciones;

import java.sql.*;
import java.util.Locale;

public class Main {

    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/orcl";

    private static final String USUARIO = "system";

    private static final String CLAVE = "Tapiero123";

    record Resultado(
            String ecuacion,
            double a,
            double b,
            double c,
            double discriminante,
            String tipo,
            double x1Real,
            double x1Imag,
            double x2Real,
            double x2Imag
    ) {}

    public static void main(String[] args) {

        Locale.setDefault(Locale.US);

        double [][] ejemplos =
                {
                        {1, -5, 6},
                        {1, -4, 4},
                        {1, 2, 5}
                };

        System.out.println("====================================");
        System.out.println(" ECUACIONES DE SEGUNDO GRADO");
        System.out.println(" JAVA 21 - ORACLE DATABASE 19C");
        System.out.println("===================================="); 

    }
}