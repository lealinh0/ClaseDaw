/*

 */
package es.fernandez.crhistian.bdrelacionesm;

import java.sql.*;
import java.util.*;

/**
 *
 * @author Amador Abelleira Gómez
 */
public class BDRelacionesm {

  private Connection conexion;
  private Statement sentencia;
  private final boolean conexionAbierta;
  private final String host;
  private final String bd;
  private final String usuario;
  private final String password;
  private final int puerto;
  private final boolean eco;

  /**
   * Crea un objeto de tipo Dba para conexión a un SGBD MySql
   *
   * @param host Host del servidro MySql
   * @param puerto Puerto de escucha
   * @param bd Base de datos seleccioanda
   * @param usuario Usuaro con permisos sobre la bD
   * @param password Password del usuario
   * @param conexionAbierta true para mentener la conexión abierta (se debe de
   * cerrar con una llamada explícia al método desconectar()<br>
   * false para abrir/cerra la conexión en cada ejecucion de sentecias
   * @param eco true apra mostrar por pantallas mensajes de errorf<br>
   * false para no mostrar mensajes
   */
  public BDRelacionesm(String host, int puerto, String bd, String usuario, String password,
          boolean conexionAbierta, boolean eco) {
    this.conexionAbierta = conexionAbierta;
    this.host = host;
    this.bd = bd;
    this.usuario = usuario;
    this.password = password;
    this.puerto = puerto;
    this.eco = eco;
    if (conexionAbierta) {
      conecta();
    } else {
      conexion = null;
      sentencia = null;
    }
  }

  /**
   * Crea un objeto de tipo Dba para conexión a un SGBD MySql en el puerto
   * estándar 3306
   *
   * @param host Host del servidro MySql
   * @param bd Base de datos seleccioanda
   * @param usuario Usuaro con permisos sobre la bD
   * @param password Password del usuario
   * @param conexionAbierta true para mentener la conexión abierta (se debe de
   * cerrar con una llamada explícia al método desconectar()<br>
   * false para abrir/cerra la conexión en cada ejecucion de sentecias
   * @param eco true apra mostrar por pantallas mensajes de errorf<br>
   * false para no mostrar mensajes
   */
  public BDRelacionesm(String host, String bd, String usuario, String password,
          boolean conexionAbierta, boolean eco) {
    this(host, 3306, bd, usuario, password, conexionAbierta, eco);
  }

  /**
   * Crea un objeto de tipo Dba para conexión a un SGBD MySql en el puerto
   * estándar 3306 de localhost
   *
   * @param bd Base de datos seleccioanda
   * @param usuario Usuaro con permisos sobre la bD
   * @param password Password del usuario
   * @param conexionAbierta true para mentener la conexión abierta (se debe de
   * cerrar con una llamada explícia al método desconectar()<br>
   * false para abrir/cerra la conexión en cada ejecucion de sentecias
   * @param eco true para mostrar por pantallas mensajes de errorf<br>
   * false para no mostrar mensajes
   */
  public BDRelacionesm(String bd, String usuario, String password,
          boolean conexionAbierta, boolean eco) {
    this("localhost", 3306, bd, usuario, password, conexionAbierta, eco);
  }

  /**
   * Crea un objeto de tipo Dba para conexión a un SGBD MySql en el puerto
   * estándar 3306 de localhost. No se selecciona ninguna BD
   *
   * @param usuario Usuaro con permisos sobre la bD
   * @param password Password del usuario
   * @param conexionAbierta true para mentener la conexión abierta (se debe de
   * cerrar con una llamada explícia al método desconectar()<br>
   * false para abrir/cerra la conexión en cada ejecucion de sentecias
   * @param eco true apra mostrar por pantallas mensajes de errorf<br>
   * false para no mostrar mensajes
   */
  public BDRelacionesm(String usuario, String password,
          boolean conexionAbierta, boolean eco) {
    this("localhost", 3306, "", usuario, password, conexionAbierta, eco);
  }

  /**
   * Crea una conexión conexion un SXBD MySQL o MariaDB
   *
   * @return true en caso de conexión exitosa <br>
   * false en otro caso
   */
  public final String conecta() {
    try {
      conexion = DriverManager.getConnection("jdbc:mysql://" + host + ":" + puerto + "/" + bd,
              usuario, password);
      sentencia = getConexion().createStatement();
      return "";
    } catch (SQLException e) {
      if (eco) {
        System.err.println(e);
      }
      return e+"";
    }
  }

  /**
   * Cierra conexión con el SGBD MySQL/MariaDB
   *
   * @return true en caso de conexión exitosa <br>
   * false en otro caso
   */
  public boolean desconecta() {
    try {
      getSentencia().close();
      getConexion().close();
      return true;
    } catch (SQLException e) {
      if (eco) {
        System.out.println(e);
      }
      return false;
    }
  }

  /**
   * Ejecuta sentecias inseert, update, delete o sentencias DDL
   *
   * @param sql Sql con la sentecia a ejecutar
   * @return true en caso de conexión exitosa <br>
   * false en otro caso
   */
  public String sqlUpdate(String sql) {
    try {
      if (!conexionAbierta) {
        conecta();
      }
      getSentencia().executeUpdate(sql);
      if (!conexionAbierta) {
        desconecta();
      }
      return "";
    } catch (SQLException e) {
      if (eco) {
        System.out.println(e);
      }
      return e+"";
    }
  }

  /**
   * Ejecuta sentencias select
   *
   * @param sql Sql con la select a ejecutar
   * @return ArrayList conteniendo los registros devueltos.<br>
   * Cada elemento del ArrayList en un Array de Strings conteniendo las columnas
   * del registro.<br>
   * O bien null en caso de error
   *
   */
  public ArrayList<String[]> sqlQuery(String sql) {
    try {
      if (!conexionAbierta) {
        conecta();
      }
      ResultSet rs = getSentencia().executeQuery(sql);
      var lista = new ArrayList<String[]>();
      ResultSetMetaData md = rs.getMetaData();
      int numeroColumas = md.getColumnCount();
      while (rs.next()) {
        String[] linea = new String[numeroColumas];
        for (int i = 1; i <= numeroColumas; ++i) {
          linea[i - 1] = rs.getString(i);
        }
        lista.add(linea);
      }
      if (!conexionAbierta) {
        desconecta();
      }
      return lista;
    } catch (SQLException e) {
      if (eco) {
        System.out.println(e);
      }
      return null;
    }
  }
  
  
  
  
 public String[] sqlQuery1(String sql) {
    try {
      if (!conexionAbierta) {
        conecta();
      }
      ResultSet rs = getSentencia().executeQuery(sql);
      String[] linea = null;
      ResultSetMetaData md = rs.getMetaData();
      int numeroColumas = md.getColumnCount();
      while (rs.next()) {
        linea= new String[numeroColumas];
        for (int i = 1; i <= numeroColumas; ++i) {
          linea[i - 1] = rs.getString(i);
        }
      }
      if (!conexionAbierta) {
        desconecta();
      }
      return linea;
    } catch (SQLException e) {
      if (eco) {
        System.out.println(e);
      }
      return null;
    }
  }

  /**
   * @return the conexion
   */
  public Connection getConexion() {
    return conexion;
  }

  /**
   * @return the sentencia
   */
  public Statement getSentencia() {
    return sentencia;
  }
}