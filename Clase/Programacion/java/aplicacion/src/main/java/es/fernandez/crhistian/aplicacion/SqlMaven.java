/*

*/
package es.fernandez.crhistian.aplicacion;
import es.fernandez.crhistian.bdrelacionesm.BDRelacionesm;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
*
* @author alumno
*/
public class SqlMaven {
static BDRelacionesm bd;

static final Logger LOG = Logger.getLogger(SqlMaven.class.getSimpleName());


static void redirigeSalidaError(String nombreFichero) {
try {

PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(nombreFichero, true)), true);
System.setErr(ps);

} catch (FileNotFoundException ex) {
System.err.println("No existe el fichero");
}
}

public static void main(String[] args) {
redirigeSalidaError("LogSQL");
Statement stm;
String user = "leal",
password = "leal",
url = "jdbc:mysql://localhost:3306/";
try (Connection con = DriverManager.getConnection(url, user, password)) {
System.out.println("Conexion establecida");

stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
ResultSet.CONCUR_UPDATABLE);


} catch (SQLException ex) {
System.out.println("Error al conectar con la BD");
LOG.log(Level.SEVERE, "No se pudo conectar a la BD", ex);
}
    }

}
