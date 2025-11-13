package com.ega.cms.controlador;

import com.ega.cms.modelo.*;
import com.ega.cms.vista.Vista;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.ArrayList;

public class MainController {
    private CMSManager manager;
    private Vista vista;

    public MainController(CMSManager manager, Vista vista) {
        this.manager = manager;
        this.vista = vista;
    }

    public void inicializarEventos() {
        // Enlaces de botones
        vista.getBotonCrear().setOnAction(e -> onBotonCrearClick());
        vista.getBotonEliminar().setOnAction(e -> onBotonEliminarClick());
        vista.getBotonPublicar().setOnAction(e -> onBotonPublicarClick());
        vista.getBotonSubirArchivo().setOnAction(e -> onBotonSubirClick());
        vista.getBotonReportes().setOnAction(e -> onBotonReportesClick());
        

        // Listener para la tabla.
        // Cuando el usuario selecciona una fila, se actualiza la vista previa.
        vista.getTableViewContenidos().getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    vista.actualizarVistaPrevia(newSelection);
                }
            }
        );

    }

    public void cargarDatosInicialesYRefrescar() {
        // Simula un login y actualiza la UI
        manager.login("S. Garcia (Admin)", ""); 
        vista.setUsuarioActual(manager.getUsuarioActual().getUsername());
        
        manager.cargarDatosIniciales();
        refrescarTablaContenidos();
    }

    private void onBotonCrearClick() {
        try {
            // Recoger datos de la Vista
            String titulo = vista.getTextFieldTitulo().getText();
            String contenido = vista.getTextAreaContenido().getText();
            String tipoSeleccionado = vista.getChoiceBoxTipo().getValue();
            String categoriaSeleccionada = vista.getChoiceBoxCategoria().getValue();
            
            if(titulo.isEmpty() || contenido.isEmpty()) {
                vista.mostrarAlertaError("El título y el contenido (texto/URL) no pueden estar vacíos.");
                return;
            }

            Contenido nuevoContenido = null;
            int nuevoId = manager.getContenidosParaVista().size() + 10;
            
            /*Usar un switch en el 'Tipo' para decidir qué clase construir
            Aquí es donde usamos las clases que implementan las interfaces
            que tu catedrático pidió.*/
            switch (tipoSeleccionado) {
                case "Artículo":
                    /*El usuario selecciona "Artículo" (String)
                    Nosotros creamos un 'new Articulo'
                    que implementa 'Academico' (Interfaz)
                    y le asignamos la categoría "Tutorial" (String)*/
                    nuevoContenido = new Articulo(nuevoId, titulo, manager.getUsuarioActual(), categoriaSeleccionada, contenido);
                    break;
                case "Video":
                    nuevoContenido = new Video(nuevoId, titulo, manager.getUsuarioActual(), categoriaSeleccionada, contenido, 0); 
                    break;
                case "Imagen":
                    nuevoContenido = new Imagen(nuevoId, titulo, manager.getUsuarioActual(), categoriaSeleccionada, contenido, "N/A");
                    break;
                default:
                    vista.mostrarAlertaError("Tipo de contenido no válido.");
                    return;
            }
            
            // Mandar al Modelo (sin cambios)
            manager.crearContenido(nuevoContenido);
            
            // Actualizar Vista (sin cambios)
            refrescarTablaContenidos();
            
            // Limpiar campos
            vista.getTextFieldTitulo().clear();
            vista.getTextAreaContenido().clear();
            
        } catch (PermisoException e) {
            vista.mostrarAlertaError(e.getMessage());
        }
    }

    private void onBotonEliminarClick() { 
        // Obtener el ítem seleccionado de la tabla en la Vista
        Contenido seleccionado = vista.getTableViewContenidos().getSelectionModel().getSelectedItem();

        // Verificar si el usuario realmente seleccionó algo
        if (seleccionado == null) {
            vista.mostrarAlertaError("Por favor, seleccione un contenido de la tabla para eliminar.");
            return; // No hacer nada si no hay selección
        }

        try {
            /*  Llamar al Modelo para que intente eliminar el contenido
            Aquí es donde el CMSManager revisará si el usuario es ADMIN*/
            manager.eliminarContenido(seleccionado);
            
            // Si todo salió bien, refrescar la tabla para que se vea el cambio
            refrescarTablaContenidos();

        } catch (PermisoException e) {
            // Si el Modelo lanza la excepción (porque es Editor), atraparla y mostrarla
            vista.mostrarAlertaError(e.getMessage());
        }
    }

    //Acción para el botón 'Publicar'.
    private void onBotonPublicarClick() {
        Contenido seleccionado = vista.getTableViewContenidos().getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            vista.mostrarAlertaError("Por favor, seleccione un contenido para publicar.");
            return;
        }

        try {
            if (seleccionado instanceof Publicable) {
                String resultado = manager.publicarContenido((Publicable) seleccionado);
                refrescarTablaContenidos(); // ¡NUEVO! Refresca para mostrar el estado "Publicado"
                
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Publicación Exitosa");
                info.setContentText(resultado);
                info.showAndWait();
            } else {
                vista.mostrarAlertaError("Este tipo de contenido no se puede publicar.");
            }
        } catch (PermisoException e) {
            vista.mostrarAlertaError(e.getMessage());
        }
    }

     //Abre un explorador de archivos.
    private void onBotonSubirClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo...");
        // Filtros para tipos de archivo
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Archivos Multimedia", "*.png", "*.jpg", "*.mp4", "*.mov"),
            new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        
        File archivoSeleccionado = fileChooser.showOpenDialog(null); // Muestra el diálogo

        if (archivoSeleccionado != null) {
            /* Pone la ruta del archivo en el campo de texto.
            toURI().toString() es la forma más segura de guardar una ruta. */
            vista.getTextAreaContenido().setText(archivoSeleccionado.toURI().toString());
        }
    }

    private void onBotonReportesClick() {
        // Pide el reporte al Modelo
        ArrayList<String> reporte = manager.generarReporte();
        
        // Muestra el reporte en una alerta informativa
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Reporte del Sistema");
        info.setHeaderText("Resumen de Contenidos Publicados");
        
        // Convierte la lista de strings en un solo string con saltos de línea
        String contenidoReporte = String.join("\n", reporte);
        
        // Usamos un TextArea para que el reporte sea grande y tenga scroll si es necesario
        TextArea textArea = new TextArea(contenidoReporte);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        
        info.getDialogPane().setContent(textArea);
        info.showAndWait();
    }

    private void refrescarTablaContenidos() {
        vista.getTableViewContenidos().setItems(
            FXCollections.observableArrayList(manager.getContenidosParaVista())
        );
    } 
}
