# -_proyecto_ecuaciones_de_segundo_gradoJavaOracle19C_- :.
# Proyecto: Ecuaciones de Segundo Grado con Java y Oracle 19c:

<img width="1254" height="1254" alt="image" src="https://github.com/user-attachments/assets/ec913f23-09dd-432b-9972-dc2aad173afb" />  

<img width="1277" height="1079" alt="image" src="https://github.com/user-attachments/assets/0e378bf5-11eb-4641-b125-5d785e3b63ce" />   

<img width="1274" height="1079" alt="image" src="https://github.com/user-attachments/assets/dd289d1f-5a58-4b02-9b42-baac2964e26a" />    
## 1. Descripción

Aplicación desarrollada en Java 21 utilizando IntelliJ IDEA y Oracle Database 19c.

El programa permite resolver tres ejemplos de ecuaciones de segundo grado mediante la fórmula general, mostrar el procedimiento matemático en consola y almacenar los resultados en una base de datos Oracle mediante un procedimiento almacenado PL/SQL.

## 2. Tecnologías utilizadas

- Java 21.
- IntelliJ IDEA.
- Maven.
- Oracle Database 19c.
- Oracle JDBC (ojdbc11).
- SQL Developer.
- CallableStatement.
- Procedimientos almacenados PL/SQL.

## 3. Ecuación matemática

La ecuación general de segundo grado es:

\[
ax^2+bx+c=0
\]

La fórmula general es:

\[
x=\frac{-b\pm\sqrt{b^2-4ac}}{2a}
\]

El discriminante se calcula mediante:

\[
D=b^2-4ac
\]

Interpretación:

| Discriminante | Resultado |
|---|---|
| D > 0 | Dos soluciones reales diferentes |
| D = 0 | Una solución real doble |
| D < 0 | Dos soluciones complejas |

## 4. Ejemplos implementados

### Ejemplo 1

\[
x^2-5x+6=0
\]

- A = 1
- B = -5
- C = 6
- D = 1
- X1 = 3
- X2 = 2

### Ejemplo 2

\[
x^2-4x+4=0
\]

- A = 1
- B = -4
- C = 4
- D = 0
- X1 = 2
- X2 = 2

### Ejemplo 3

\[
x^2+2x+5=0
\]

- A = 1
- B = 2
- C = 5
- D = -16
- X1 = -1 + 2i
- X2 = -1 - 2i

## 5. Estructura del proyecto

```text
EcuacionesSegundoGrado/
│
├── pom.xml
│
├── database/
│   └── script_oracle.sql
│
└── src/
    └── main/
        └── java/
            └── com/
                └── ecuaciones/
                    └── Main.java
```

## 6. Configuración Maven - pom.xml

Ubicación: `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.ecuaciones</groupId>
    <artifactId>EcuacionesSegundoGrado</artifactId>
    <version>1.0</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.oracle.database.jdbc</groupId>
            <artifactId>ojdbc11</artifactId>
            <version>23.6.0.24.10</version>
        </dependency>
    </dependencies>

</project>
```

## 7. Script completo Oracle 19c

Ubicación: `database/script_oracle.sql`

```sql
-- ==========================================
-- PROYECTO ECUACIONES SEGUNDO GRADO
-- ORACLE DATABASE 19C
-- ==========================================

-- CREAR TABLA
CREATE TABLE ECUACIONES_RESULTADOS (
    ID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ECUACION VARCHAR2(200),
    COEFICIENTE_A NUMBER,
    COEFICIENTE_B NUMBER,
    COEFICIENTE_C NUMBER,
    DISCRIMINANTE NUMBER,
    TIPO_SOLUCION VARCHAR2(50),
    X1_REAL NUMBER,
    X1_IMAGINARIO NUMBER,
    X2_REAL NUMBER,
    X2_IMAGINARIO NUMBER,
    FECHA_REGISTRO TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PROCEDIMIENTO ALMACENADO
CREATE OR REPLACE PROCEDURE PR_GUARDAR_ECUACION (
    P_ECUACION IN VARCHAR2,
    P_A IN NUMBER,
    P_B IN NUMBER,
    P_C IN NUMBER,
    P_DISCRIMINANTE IN NUMBER,
    P_TIPO IN VARCHAR2,
    P_X1_REAL IN NUMBER,
    P_X1_IMAG IN NUMBER,
    P_X2_REAL IN NUMBER,
    P_X2_IMAG IN NUMBER
)
AS
BEGIN

    INSERT INTO ECUACIONES_RESULTADOS (
        ECUACION,
        COEFICIENTE_A,
        COEFICIENTE_B,
        COEFICIENTE_C,
        DISCRIMINANTE,
        TIPO_SOLUCION,
        X1_REAL,
        X1_IMAGINARIO,
        X2_REAL,
        X2_IMAGINARIO
    )
    VALUES (
        P_ECUACION,
        P_A,
        P_B,
        P_C,
        P_DISCRIMINANTE,
        P_TIPO,
        P_X1_REAL,
        P_X1_IMAG,
        P_X2_REAL,
        P_X2_IMAG
    );

END;
/

-- CONSULTAR REGISTROS
SELECT *
FROM ECUACIONES_RESULTADOS
ORDER BY ID;
```

## 8. Código completo Java - Main.java

Ubicación: `src/main/java/com/ecuaciones/Main.java`

```java
package com.ecuaciones;

import java.sql.*;
import java.util.Locale;

public class Main {

    private static final String URL =
            "jdbc:oracle:thin:@//localhost:1521/orcl";

    private static final String USUARIO = "system";
    private static final String CLAVE = "TU_PASSWORD";

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

        double[][] ejemplos = {
                {1, -5, 6},
                {1, -4, 4},
                {1, 2, 5}
        };

        System.out.println("====================================");
        System.out.println(" ECUACIONES DE SEGUNDO GRADO");
        System.out.println(" JAVA 21 - ORACLE DATABASE 19C");
        System.out.println("====================================");

        try (Connection conexion = DriverManager.getConnection(
                URL, USUARIO, CLAVE)) {

            conexion.setAutoCommit(false);

            System.out.println("\nConexion con Oracle exitosa.");

            for (int i = 0; i < ejemplos.length; i++) {

                double a = ejemplos[i][0];
                double b = ejemplos[i][1];
                double c = ejemplos[i][2];

                System.out.println("\n------------------------------------");
                System.out.println("EJEMPLO " + (i + 1));
                System.out.println("------------------------------------");

                Resultado resultado = resolver(a, b, c);

                if (resultado == null) {
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

    // RESOLVER ECUACION DE SEGUNDO GRADO
    public static Resultado resolver(
            double a, double b, double c) {

        if (a == 0) {
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

        if (discriminante > 0) {

            tipo = "DOS SOLUCIONES REALES";

            x1Real = (-b + Math.sqrt(discriminante)) / (2 * a);
            x2Real = (-b - Math.sqrt(discriminante)) / (2 * a);

            x1Imag = 0;
            x2Imag = 0;

        } else if (discriminante == 0) {

            tipo = "UNA SOLUCION REAL DOBLE";

            x1Real = -b / (2 * a);
            x2Real = x1Real;

            x1Imag = 0;
            x2Imag = 0;

        } else {

            tipo = "DOS SOLUCIONES COMPLEJAS";

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

        return new Resultado(
                ecuacion, a, b, c,
                discriminante, tipo,
                x1Real, x1Imag,
                x2Real, x2Imag
        );
    }

    // MOSTRAR PROCEDIMIENTO MATEMATICO
    public static void mostrarProcedimiento(Resultado r) {

        System.out.println("Ecuacion: " + r.ecuacion());

        System.out.println("\nPaso 1: Identificar coeficientes");

        System.out.println("A = " + r.a());
        System.out.println("B = " + r.b());
        System.out.println("C = " + r.c());

        System.out.println("\nPaso 2: Calcular discriminante");

        System.out.println("D = B^2 - 4*A*C");

        System.out.println("D = (" + r.b() + ")^2 - 4*("
                + r.a() + ")*(" + r.c() + ")");

        System.out.println("D = " + r.discriminante());

        System.out.println("\nPaso 3: Identificar soluciones");

        System.out.println("Tipo: " + r.tipo());

        System.out.println("\nPaso 4: Resultados");

        mostrarRaiz("X1", r.x1Real(), r.x1Imag());
        mostrarRaiz("X2", r.x2Real(), r.x2Imag());
    }

    // MOSTRAR RAIZ REAL O COMPLEJA
    private static void mostrarRaiz(
            String nombre, double real, double imaginaria) {

        if (imaginaria == 0) {

            System.out.printf("%s = %.4f%n", nombre, real);

        } else {

            System.out.printf("%s = %.4f %s %.4fi%n",
                    nombre,
                    real,
                    imaginaria >= 0 ? "+" : "-",
                    Math.abs(imaginaria));
        }
    }

    // GUARDAR INFORMACION EN ORACLE
    public static void guardarOracle(
            Connection conexion, Resultado r) throws SQLException {

        String sql = "{call PR_GUARDAR_ECUACION(?,?,?,?,?,?,?,?,?,?)}";

        try (CallableStatement cs = conexion.prepareCall(sql)) {

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

    // CONSULTAR RESULTADOS ALMACENADOS
    public static void consultarResultados(
            Connection conexion) throws SQLException {

        String sql = """
                SELECT ID, ECUACION, DISCRIMINANTE,
                       TIPO_SOLUCION, X1_REAL, X1_IMAGINARIO,
                       X2_REAL, X2_IMAGINARIO
                FROM ECUACIONES_RESULTADOS
                ORDER BY ID
                """;

        System.out.println("\n====================================");
        System.out.println(" RESULTADOS ALMACENADOS EN ORACLE");
        System.out.println("====================================");

        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                System.out.println("\nID: " + rs.getInt("ID"));

                System.out.println(
                        "Ecuacion: " + rs.getString("ECUACION"));

                System.out.println(
                        "Discriminante: "
                                + rs.getDouble("DISCRIMINANTE"));

                System.out.println(
                        "Tipo: " + rs.getString("TIPO_SOLUCION"));

                mostrarRaiz(
                        "X1",
                        rs.getDouble("X1_REAL"),
                        rs.getDouble("X1_IMAGINARIO"));

                mostrarRaiz(
                        "X2",
                        rs.getDouble("X2_REAL"),
                        rs.getDouble("X2_IMAGINARIO"));
            }
        }
    }

    private static String formato(double numero) {
        return String.format("%.2f", numero);
    }
}
```

## 9. Configuración de conexión Oracle

El proyecto utiliza la siguiente configuración:

```java
private static final String URL =
        "jdbc:oracle:thin:@//localhost:1521/orcl";

private static final String USUARIO = "system";
private static final String CLAVE = "TU_PASSWORD";
```

Reemplazar `TU_PASSWORD` por la contraseña correspondiente.

Si tu servicio Oracle tiene otro nombre, debes modificar la URL JDBC.

## 10. Ejecución del proyecto

### Paso 1. Crear el proyecto

En IntelliJ IDEA:

1. Seleccionar New Project.
2. Seleccionar Maven.
3. Configurar Java 21.
4. Nombre: EcuacionesSegundoGrado.

### Paso 2. Configurar Maven

Copiar el contenido del archivo `pom.xml`.

Actualizar las dependencias Maven.

### Paso 3. Configurar Oracle

1. Iniciar Oracle Database 19c.
2. Abrir SQL Developer.
3. Conectarse al esquema de trabajo.
4. Ejecutar `script_oracle.sql`.
5. Comprobar que se creó la tabla y el procedimiento.

### Paso 4. Ejecutar Java

1. Abrir `Main.java`.
2. Configurar usuario y contraseña.
3. Ejecutar el método `main`.
4. Verificar los tres ejemplos en consola.

## 11. Resultado esperado

```text
====================================
 ECUACIONES DE SEGUNDO GRADO
 JAVA 21 - ORACLE DATABASE 19C
====================================

Conexion con Oracle exitosa.

EJEMPLO 1
D = 1.0
X1 = 3.0000
X2 = 2.0000

Registro almacenado correctamente.

EJEMPLO 2
D = 0.0
X1 = 2.0000
X2 = 2.0000

Registro almacenado correctamente.

EJEMPLO 3
D = -16.0
X1 = -1.0000 + 2.0000i
X2 = -1.0000 - 2.0000i

Registro almacenado correctamente.
```

## 12. Consultas SQL de verificación

Consultar todos los resultados:

```sql
SELECT *
FROM ECUACIONES_RESULTADOS
ORDER BY ID;
```

Consultar los tipos de soluciones:

```sql
SELECT
    TIPO_SOLUCION,
    COUNT(*) AS TOTAL
FROM ECUACIONES_RESULTADOS
GROUP BY TIPO_SOLUCION;
```

Consultar las ecuaciones con soluciones complejas:

```sql
SELECT
    ECUACION,
    DISCRIMINANTE,
    X1_REAL,
    X1_IMAGINARIO,
    X2_REAL,
    X2_IMAGINARIO
FROM ECUACIONES_RESULTADOS
WHERE DISCRIMINANTE < 0;
```

## 13. Objetivos de aprendizaje

Con este proyecto se desarrollan conocimientos sobre:

- Resolución de ecuaciones cuadráticas.
- Uso de la fórmula general.
- Cálculo de discriminantes.
- Operaciones matemáticas con Java.
- Condicionales `if`, `else if` y `else`.
- Métodos y estructuras de datos.
- Conexiones JDBC.
- Ejecución de procedimientos almacenados.
- Inserción y consulta de información en Oracle 19c.

## 14. Autor y proyecto

**Proyecto:** Ecuaciones de Segundo Grado.

**Lenguaje:** Java 21.

**Base de datos:** Oracle Database 19c.

**IDE:** IntelliJ IDEA.

**Integración:** JDBC y PL/SQL.

---

**Fin del proyecto.**
