/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package es.fernandez.crhistian.t_10_01;

/**
 *
 * @author leal
 */
/*
Aplicación que permita introducir y consultar las notas de los alumnos de 1º DAW en el módulo
de Programación.

Las notas se almacenarán en una tabla denominada notas que estará contenida en una BD
en el servidor 10.0.3.0

Nombre de la BD, usuario y contraseña son los mismos: vuestro nombre.

La tabla notas constará de los campos:
• dni        de tipo cadena de caracteres y clave primaria
• nombre     de tipo cadena de caracteres y clave primaria
• p1         entero
• p2         entero
• p3         entero
• final      real
ninguno de los campos admitirá valores nulos.

1) Crear la BD, crear la tabla e inicializar los valores

2) Programa de gestión, con las siguientes opciones:
   1- Actualizar nota
   2- Listar resultados
   3- Salir

Hacer chequeo de todas las entradas de usuario.

la opción 1 pedirá seleccionar alumno y parcial(o final) y la nueva nota, que se actualizará
en la BD

la opción 2 mostrará el listado de todos los registros ordenados (de mayor a menor) por:
   - la mayor de: nota final, media de las 3 evaluaciones
   - p3
   - p2
   - p1
debe mostar una columna a mayores indicando si el alumno pasa a 2º curso o no (pasa si la nota
calculada como final (punto 2) es mayor o igual a 5

TODO el listado tiene que estar realizado en la select. En Java nos limitaremos a darle formato.

 */

import java.util.Scanner;
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 *
 * @author leal
 */
public class T_10_01 {

  public static void main(String[] args) {
    Scanner tec = new Scanner(System.in);
    Connection conexion = null;
    String user = "leal";
    String password = "leal";
    String url = "jdbc:mysql://10.0.3.0:3306/leal"
            + "";
    Statement sta;
    ResultSet rs = null;

    //Conexion al servidor
    try {
      conexion = DriverManager.getConnection(url, user, password);
      System.out.println("Conexión establecida");
      sta = conexion.createStatement();
      sta.executeUpdate("create database if not exists leal");
      sta.executeUpdate("""
                        create table if not exists notas3
                        (
                        dni varchar(9) not null,
                        nombre varchar(15) not null,
                        p1 int not null,
                        p2 int not null,
                        p3 int not null,
                        xd double not null,
                        primary key(dni)
                        )
                        """);
    } catch (SQLException e) {
      System.out.println("No se ha podido crear la tabla\n" + e);
    }

   
    try {
      sta = conexion.createStatement();
      sta.executeUpdate("""
                        insert into notas3 (dni, nombre, p1, p2, p3, xd)
                        values
                        ('11111111A','Adolfo', 1,1,1,1.0),
                        ('22222222B','Yago', 1,1,1,1.0),
                        ('33333333C','Adrian', 1,1,1,1.0),
                        ('44444444D','Constantino', 1,1,1,1.0),
                        ('55555555E','Fran', 1,1,1,1.0),
                        ('66666666F','Fra', 1,1,1,1.0),
                        ('77777777G','Crhistian', 1,1,1,1.0),
                        ('88888888H','Requejo', 1,1,1,1.0),
                        ('99999999I','Manuel', 1,1,1,1.0),
                        ('11111111J','Jose', 1,1,1,1.0),
                        ('22222222K','Javier', 1,1,1,1.0),
                        ('33333333L','Rebeca', 1,1,1,1.0),
                        ('44444444M','Cristian', 1,1,1,1.0),
                        ('55555555N','David', 1,1,1,1.0),
                        ('66666666Ñ','Tito', 1,1,1,1.0),
                        ('77777777O','Xurxo', 1,1,1,1.0),
                        ('88888888P','Benjamin', 1,1,1,1.0)
                        """);
    } catch (SQLException e) {
      System.out.println("No se ha podido insertar los datos en la tabla\n" + e);
    }

    //PROGRAMA DE GESTION
    while (true) {
      System.out.println("""
                          \n
                          Elige una opción:
                          1.Actualizar nota
                          2.Listar resultados
                          3.Salir
                          """);
      int opcion = tec.nextInt();
      tec.nextLine();
      switch (opcion) {
        case 1:
          System.out.println("Introduce el nombre del alumno: ");
          String nombreAl = tec.nextLine();
          if (nombreAl.isEmpty()) {
            break;
          }
          System.out.println("Introduce el parcial(p1, p2 o p3) o xd: ");
          String nota = tec.nextLine();
          System.out.println("Introduce la nueva nota: ");
          int valor = tec.nextInt();
          try {
            sta = conexion.createStatement();
            sta.executeUpdate(
                    "update notas3"
                    + " set " + nota + " = " + valor
                    + " where nombre = '" + nombreAl + "';"
            );
              System.out.println("update notas"
                    + " set " + nota + " = " + valor
                    + " where nombre = '" + nombreAl + "';");
          } catch (SQLException e) {
            System.out.println("No se ha podido realizar la actualizacion de datos \n" + e);
          }
          break;

        case 2:
        try {
          sta = conexion.createStatement();
          String select = "select dni, nombre, p1, p2, p3, IF((p1 + p2 + p3)/3 > xd, "
              + "(p1 + p2 + p3)/3, xd) as pFinal, IF(IF((p1 + p2 + p3)/3 > "
              + "xd, (p1 + p2 + p3)/3, xd) < 5, \"No\", \"Sí\") "
              + "as pasaASegundo from notas3 group by dni order by pFinal desc, p3 desc, p2 desc, p1 desc";
           rs = sta.executeQuery(select);
            System.out.println(select);
          if (rs != null) {
            int cont = 1;
            while (rs.next()) {
              String dni = rs.getString("dni");
              String nombre = rs.getString("nombre");
              int parcial1 = rs.getInt("p1");
              int parcial2 = rs.getInt("p2");
              int parcial3 = rs.getInt("p3");
              double notaFinal = rs.getDouble("pFinal");
              String pasaSegundo = rs.getString("pasaASegundo");
              System.out.println(
                      "\nAlumno " + cont + ":"
                      + "\nDNI -> " + dni
                      + "\nNombre -> " + nombre
                      + "\nNota parcial 1 -> " + parcial1
                      + "\nNota parcial 2 -> " + parcial2
                      + "\nNota parcial 3 -> " + parcial3
                      + "\nNota final-> " + notaFinal
                      + "\nPasa a segundo-> " + pasaSegundo
                     );
              cont += 1;
            }
          }
        } catch (SQLException e) {
          System.out.println("No se ha podido realizar la query \n" + e);
        }
        break;

        case 3:
          try {
          conexion.close();
          System.out.println("Conexion cerrada con exito");
        } catch (SQLException e) {
          System.out.println("No se ha podido cerrar la conexion \n" + e);
        }
        System.exit(0);
        break;

      }
    }
  }
}
