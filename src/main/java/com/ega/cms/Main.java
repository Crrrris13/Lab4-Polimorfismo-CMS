package com.ega.cms;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.ega.cms.controlador.MainController;
import com.ega.cms.modelo.CMSManager;
import com.ega.cms.vista.Vista;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Crear las 3 capas
        CMSManager manager = new CMSManager();
        Vista vista = new Vista();
        MainController controller = new MainController(manager, vista);

        // Llama a la Vista para que cree todos los botones, tablas y layouts. Ahora 'root' existe y los componentes de la vista (botonCrear, etc.) ya no son 'null'.
        Parent root = vista.construirUI();

        // ENLAZAR DESPUÉS: Ahora que los botones SÍ existen, Le decimos al Controlador que les añada los eventos.
        controller.inicializarEventos();

        // CARGAR DATOS AL FINAL: Simula el login y carga los datos. El controlador ahora puede refrescar la tabla porque ya existe.
        manager.login("S. Garcia (Admin)", ""); // Simula un login de Admin al inicio
        controller.cargarDatosInicialesYRefrescar();
    
        // Mostrar la escena (Esto está bien)
        Scene scene = new Scene(root, 1200, 800);

        String css = this.getClass().getResource("/estilos.css").toExternalForm();
        scene.getStylesheets().add(css);
    
        stage.setTitle("EGA CMS - Panel de Contenidos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}