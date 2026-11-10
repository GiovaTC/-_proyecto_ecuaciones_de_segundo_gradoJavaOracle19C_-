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

        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CLAVE)) {

            conexion.setAutoCommit(false);

            System.out.println("\nCONEXION con ORACLE exitosa.");

            for (int i = 0; i < ejemplos.length; i++)
            {

                double a = ejemplos[i][0];
                double b = ejemplos[i][1];
                double c = ejemplos[i][2];

                System.out.println("\n------------------------------------");
                System.out.println("EJEMPLO " + (i + 1));
                System.out.println("------------------------------------");

                Resultado resultado = resolver(a, b, c);

                if (resultado == null)
                {
                    continue;
                }

                mostrarProcedimiento(resultado);

                guardarOracle(conexion, resultado);
            }

            consultarResultados(conexion);
        } catch (SQLException e) {
            System.out.println("\nError de conexion o SQL: "
                    + e.getMessage());

            e.printStackTrace();
        }
    }

    // RESOLVER ECUACION DE SEGUNDO GRADO!
    private static Resultado resolver(double a, double b, double c)
    {
        if ( a == 0)
        {
            System.out.println(
                    "Error: el coeficiente A no puede ser cero.");
            return null;
        }

        double discriminante = b * b - 4 * a * c;
        double x1Real;
        double x1Imag;
        double x2Real;
        double x2Imag;

        String tipo;

        if (discriminante > 0)
        {
            tipo = "DOS SOLUCIONES REALES";

            x1Real = (-b + Math.sqrt(discriminante)) / (2 * a);
            x2Real = (-b - Math.sqrt(discriminante)) / (2 * a);

            x1Imag = 0;
            x2Imag = 0;
        } else if (discriminante == 0) {
            tipo = "UNA SOLUCION REAL DOBLE!";

            x1Real = -b / (2 * a);
            x2Real = x1Real;

            x1Imag = 0;
            x2Imag = 0;
        } else {
            tipo = "DOS SOLUCIONES COMPLEJAS!";
            x1Real = -b / (2 * a);
            x2Real = x1Real;

            x1Imag = Math.sqrt(-discriminante) / (2 * a);
            x2Imag = -x1Imag;
        }

        String ecuacion = formato(a) + "x^2 "
                + (b >= 0 ? "+ " : "- ") + formato(Math.abs(b))
                + "x "
                + (c >= 0 ? "+ " : "- ") + formato(Math.abs(c))
                + " = 0";

        return new Resultado(ecuacion, a, b, c, discriminante, tipo,
                x1Real, x1Imag, x2Real, x2Imag);
    }

    private static String formato(double a) {
        return  null;   
    }

    private static void mostrarProcedimiento(Resultado resultado) {

    }

    private static void guardarOracle(Connection conexion, Resultado resultado) {

    }

    private static void consultarResultados(Connection conexion) {

    }
}