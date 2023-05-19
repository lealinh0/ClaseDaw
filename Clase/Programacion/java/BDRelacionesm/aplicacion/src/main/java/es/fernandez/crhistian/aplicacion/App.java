package es.fernandez.crhistian.aplicacion;

import es.fernandez.crhistian.bdrelacionesm.BDRelacionesm;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.io.IOException;
import javafx.application.Platform;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    static BDRelacionesm bd;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 770, 700);
        stage.setScene(scene);
        stage.setTitle("Probando JavaFX");
        stage.show();
        stage.setResizable(false);
    }
    
    static void redirigeSalidaError(String nombreFichero) {
    try {

    PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(nombreFichero, true)), true);
        System.setErr(ps);

    } catch (FileNotFoundException ex) {
        System.err.println("No existe el fichero");
        }
    }
    
    static final Logger LOG = Logger.getLogger(SqlMaven.class.getSimpleName());
    
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        redirigeSalidaError("LogSQL"); 
        bd = new BDRelacionesm("10.0.3.0","leal","leal","leal",true,true);
        
        
        try{
            if (!bd.conecta().isEmpty()) {
                gestor.mostrarAlertError("ERROR BD","No se pudeo conectar con la BD. Se abortará el programa");
                Platform.exit();
                System.exit(0);
            }
            
            
            Statement stm;
            stm = bd.getConexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        }catch (Exception e) {
            System.out.println(""+e);
            System.out.println("Error al conectar con la BD");
            LOG.log(Level.SEVERE, "No se pudo conectar a la BD", e);
      
        }
        try {
            String sql = "create table if not exists usuarios ("
            +"dni VARCHAR(9) not null,"
            +"nombre VARCHAR(15) not null,"
            +"apellido VARCHAR(30) NOT NULL,"
            +"email VARCHAR(15) NOT NULL,"
            +"password VARCHAR(15) NOT NULL,"
            +"notas VARCHAR(500),"
            +"primary key(dni)"
            +")";
            if(bd.sqlUpdate(sql).isBlank()==true){
                System.out.println("Se ha creado la tabla correctamente");
            }
        } catch (Exception e) {
            System.out.println("Error al conectar con la BD");
            LOG.log(Level.SEVERE, "No se pudo conectar a la BD", e);
        }
       
        
        launch();
        if(bd.desconecta()){
            System.out.println("Desconexión establecida");
        }else{
            System.out.println("Error en la desconexión");
        }

    }

}