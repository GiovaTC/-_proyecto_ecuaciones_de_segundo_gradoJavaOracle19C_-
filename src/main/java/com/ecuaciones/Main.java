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

    private static void mostrarProcedimiento(Resultado r) {

        System.out.println("Ecuacion: " + r.ecuacion());
        System.out.println("\nPASO 1: IDENTIFICAR coeficientes!");

        System.out.println("A = " + r.a());
        System.out.println("B = " + r.b());
        System.out.println("C = " + r.c());

        System.out.println("\nPASO 2: CALCULAR discriminante!");
        System.out.println("D = B^2 - 4*A*C");

        System.out.println("D = (" + r.b() + ")^2 - 4*("
                + r.a() + ")*(" + r.c() + ")");

        System.out.println("D = " + r.discriminante());

        System.out.println("\nPASO 3: IDENTIFICAR soluciones!");
        System.out.println("TIPO: " + r.tipo());

        System.out.println("\nPASO 4: Resultados!");

        mostrarRaiz("X1", r.x1Real(), r.x1Imag());
        mostrarRaiz("X2", r.x2Real(), r.x2Imag());
    }

    private static void mostrarRaiz(String nombre, double real, double imaginaria)
    {
        if (imaginaria == 0)
        {
            System.out.printf("%s = %.4f%n", nombre, real);
        }
        else {

            System.out.printf("%s = %.4f %s %.4fi%n",
                    nombre,
                    real,
                    imaginaria >= 0 ? "+" : "-",
                    Math.abs(imaginaria));
        }
    }

    // GUARDAR INFORMACION EN ORACLE BD!
    private static void guardarOracle(Connection conexion, Resultado r) throws SQLException
    {
        String sql = "{call PR_GUARDAR_ECUACION(?,?,?,?,?,?,?,?,?,?)}";

        try (CallableStatement cs = conexion.prepareCall(sql))
        {

            cs.setString(1, r.ecuacion());
            cs.setDouble(2, r.a());
            cs.setDouble(3, r.b());
            cs.setDouble(4, r.c());
            cs.setDouble(5, r.discriminante());
            cs.setString(6, r.tipo());
            cs.setDouble(7, r.x1Real());
            cs.setDouble(8, r.x1Imag());
            cs.setDouble(9, r.x2Real());
            cs.setDouble(10, r.x2Imag());

            cs.execute();

            conexion.commit();

            System.out.println(
                    "\nRegistro almacenado correctamente en Oracle 19c.");
        }
    }

    private static void consultarResultados(Connection conexion) throws SQLException
    {

        String sql = """
                SELECT ID, ECUACION, DISCRIMINANTE, TIPO_SOLUCION, X1_REAL, X1_IMAGINARIO,
                X2_REAL, X2_IMAGINARIO FROM ECUACIONES_RESULTADOS ORDER BY ID
                """;

        System.out.println("\n====================================");
        System.out.println(" RESULTADOS ALMACENADOS EN ORACLE");
        System.out.println("====================================");

        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql))
        {
            while (rs.next())
            {
                System.out.println("\nID: " + rs.getInt("ID"));
                System.out.println(
                        "Ecuacion: " + rs.getString("ECUACION"));
                System.out.println(
                        "Discriminante: "
                                + rs.getDouble("DISCRIMINANTE"));
                System.out.println(
                        "Tipo: " + rs.getString("TIPO_SOLUCION"));

                mostrarRaiz("X1",
                        rs.getDouble("X1_REAL"),
                        rs.getDouble("X1_IMAGINARIO"));
                mostrarRaiz("X2",
                        rs.getDouble("X2_REAL"),
                        rs.getDouble("X2_IMAGINARIO"));
            }
        }
    }

    private static String formato(double numero) {
        return  String.format("%.2f", numero);
    }   
}