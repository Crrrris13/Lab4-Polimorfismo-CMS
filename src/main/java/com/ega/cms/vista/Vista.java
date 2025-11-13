package com.ega.cms.vista;

import com.ega.cms.modelo.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;

    // Construye la UI programáticamente y aplica los estilos CSS.
public class Vista {
    // Componentes de UI
    private TableView<Contenido> tableViewContenidos;
    private TextField textFieldTitulo;
    private TextArea textAreaContenido;
    private ChoiceBox<String> choiceBoxCategoria;
    private Button botonCrear;
    private Button botonEliminar;
    private Button botonPublicar;
    private Button botonSubirArchivo;
    private Label labelUsuarioActual;
    private Button btnRep;

    // Componentes del Panel de Vista Previa 
    private Label labelPreviewTitulo;
    private Label labelPreviewTipo;
    private Label labelPreviewCategoria;
    private Label labelPreviewDescripcion;
    private Label labelPreviewEtiquetas;
    private Label labelPreviewFecha;
    private Label labelPreviewVistas;
    private Label labelPreviewEstado;
    private ImageView previewImagen;

    private ChoiceBox<String> choiceBoxTipo;

    public Parent construirUI() {
        BorderPane root = new BorderPane();

        // Panel Izquierdo (Menú)
        VBox menuIzquierdo = new VBox();
        menuIzquierdo.getStyleClass().add("menu-izquierdo"); // Aplica CSS
        
        Button btnDash = new Button("Dashboard");
        Button btnCont = new Button("Contenidos");
        Button btnCat = new Button("Categorías");
        Button btnUser = new Button("Usuarios");
        btnRep = new Button("Reportes");
        Button btnAju = new Button("Ajustes");


        // Aplica CSS a todos los botones del menú
        for (Button btn : new Button[]{btnDash, btnCont, btnCat, btnUser, btnRep, btnAju}) {
            btn.getStyleClass().add("boton-menu");
        }
        menuIzquierdo.getChildren().addAll(btnDash, btnCont, btnCat, btnUser, btnRep, btnAju);
        root.setLeft(menuIzquierdo);

        // Panel Superior (Header)
        HBox header = new HBox();
        header.getStyleClass().add("header-bar"); // Aplica CSS
        labelUsuarioActual = new Label("Usuario: No identificado");
        labelUsuarioActual.getStyleClass().add("label-usuario"); // Aplica CSS
        header.getChildren().add(labelUsuarioActual);
        root.setTop(header);

        // --- 3. Panel Central (Contenidos) ---
        VBox panelCentral = new VBox();
        panelCentral.getStyleClass().add("panel-central"); // Aplica CSS

        // Barra de acciones
        HBox barraAcciones = new HBox(10);
        barraAcciones.getStyleClass().add("barra-acciones"); // Aplica CSS
        barraAcciones.setAlignment(Pos.CENTER_LEFT);
        
        botonCrear = new Button("+ Nuevo Contenido");
        botonCrear.getStyleClass().addAll("boton-accion", "boton-nuevo-contenido"); // Múltiples estilos
        
        botonEliminar = new Button("Eliminar Seleccionado");
        botonEliminar.getStyleClass().addAll("boton-accion", "boton-secundario");
        
        botonPublicar = new Button("Publicar Seleccionado");
        botonPublicar.getStyleClass().addAll("boton-accion", "boton-secundario");

        barraAcciones.getChildren().addAll(botonCrear, botonEliminar, botonPublicar);
        
        // Tabla de Contenidos (con las nuevas columnas del boceto)
        tableViewContenidos = new TableView<>();
        tableViewContenidos.getStyleClass().add("table-view"); // Aplica CSS
        
        TableColumn<Contenido, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Contenido, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        
        TableColumn<Contenido, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        
        TableColumn<Contenido, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Contenido, Usuario> colAutor = new TableColumn<>("Autor");
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        
        TableColumn<Contenido, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        
        tableViewContenidos.getColumns().addAll(colId, colTitulo, colTipo, colCategoria, colAutor, colEstado);
        tableViewContenidos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Panel inferior (Creación y Vista Previa)
        HBox panelInferior = new HBox(20);
        panelInferior.getChildren().addAll(crearPanelCreacion(), crearPanelVistaPrevia());
        
        panelCentral.getChildren().addAll(barraAcciones, tableViewContenidos, panelInferior);
        root.setCenter(panelCentral);

        return root;
    }

     // Método privado para organizar la creación del panel
    private VBox crearPanelCreacion() {
        VBox panelCreacion = new VBox();
        panelCreacion.getStyleClass().add("panel-detalle");
        
        textFieldTitulo = new TextField();
        textFieldTitulo.setPromptText("Título del contenido...");
        
        // --- DROPDOWN ---
        choiceBoxTipo = new ChoiceBox<>();
        choiceBoxTipo.getItems().addAll("Artículo", "Video", "Imagen");
        choiceBoxTipo.setValue("Artículo"); // Valor por defecto
        
        choiceBoxCategoria = new ChoiceBox<>();
        choiceBoxCategoria.getItems().addAll("Tutorial", "Promocional", "Informativo", "Otro");
        choiceBoxCategoria.setValue("Tutorial"); // Valor por defecto
        
        textAreaContenido = new TextArea();
        textAreaContenido.setPromptText("Texto del artículo, URL del archivo, etc...");
        
        botonSubirArchivo = new Button("Subir Archivo...");
        botonSubirArchivo.getStyleClass().addAll("boton-accion", "boton-secundario");
        
        panelCreacion.getChildren().addAll(
            new Label("Título:"), textFieldTitulo,
            new Label("Tipo:"), choiceBoxTipo,
            new Label("Categoría:"), choiceBoxCategoria,
            new Label("Contenido (Texto o URL):"), textAreaContenido,
            botonSubirArchivo
        );
        return panelCreacion;
    }

    // Método privado para crear el panel de vista previa 
    private VBox crearPanelVistaPrevia() {
        VBox panelVistaPrevia = new VBox();
        panelVistaPrevia.getStyleClass().add("panel-detalle");
        
        labelPreviewTitulo = new Label("Título: (Seleccione un item)");
        labelPreviewTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        labelPreviewTipo = new Label("Tipo: N/A");
        labelPreviewCategoria = new Label("Categoría: N/A");
        labelPreviewDescripcion = new Label("Descripción del Contenido...");
        labelPreviewEtiquetas = new Label("Etiquetas: N/A");
        labelPreviewFecha = new Label("Fecha publicación: N/A");
        labelPreviewVistas = new Label("Vistas: N/A");
        labelPreviewEstado = new Label("Estado: N/A");
        
        // Espacio para la imagen
        previewImagen = new ImageView();
        previewImagen.setFitHeight(150);
        previewImagen.setPreserveRatio(true);
        
        panelVistaPrevia.getChildren().addAll(
            labelPreviewTitulo, previewImagen, labelPreviewTipo, labelPreviewCategoria,
            labelPreviewDescripcion, labelPreviewEtiquetas, labelPreviewFecha,
            labelPreviewVistas, labelPreviewEstado
        );
        return panelVistaPrevia;
    }

    // --- Getters para el Controlador ---
    public TableView<Contenido> getTableViewContenidos() { 
        return tableViewContenidos; 
    }
    
    public TextField getTextFieldTitulo() { 
        return textFieldTitulo; 
    }

    public TextArea getTextAreaContenido() { 
        return textAreaContenido; 
    }

    public ChoiceBox<String> getChoiceBoxCategoria() { 
        return choiceBoxCategoria; 
    }

    public Button getBotonCrear() { 
        return botonCrear; 
    }

    public Button getBotonEliminar() { 
        return botonEliminar; 
    }

    public Button getBotonPublicar() { 
        return botonPublicar; 
    }

    public Button getBotonSubirArchivo() { 
        return botonSubirArchivo; 
    }

    public ChoiceBox<String> getChoiceBoxTipo() { 
        return choiceBoxTipo; 
    }

    public Button getBotonReportes() { 
        return btnRep; 
    }

    // --- Métodos de actualización de la Vista ---
    public void setUsuarioActual(String nombreUsuario) {
        labelUsuarioActual.setText("Usuario: " + nombreUsuario);
    }
    
    public void mostrarAlertaError(String mensaje) { 
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Permiso");
        alert.setHeaderText("Acción Denegada");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Actualiza el panel de vista previa
    public void actualizarVistaPrevia(Contenido c) {
        if (c == null) return;
        labelPreviewTitulo.setText("Título: " + c.getTitulo());
        labelPreviewTipo.setText("Tipo: " + c.getTipo());
        labelPreviewCategoria.setText("Categoría: " + c.getCategoria());
        labelPreviewEtiquetas.setText("Etiquetas: " + c.getEtiquetasString());
        labelPreviewFecha.setText("Fecha publicación: " + c.getFechaPublicacion().toString());
        labelPreviewVistas.setText("Vistas: " + c.getVistas());
        labelPreviewEstado.setText("Estado: " + c.getEstado());
        
        // Lógica simple para mostrar una imagen o descripción
        if (c instanceof Imagen) {
            // try { previewImagen.setImage(new Image(((Imagen) c).getUrl())); } catch (Exception e) {}
            labelPreviewDescripcion.setText("Mostrando imagen...");
        } else if (c instanceof Video) {
            labelPreviewDescripcion.setText("Contenido de video. URL: " + ((Video) c).getUrl());
        } else if (c instanceof Articulo) {
            labelPreviewDescripcion.setText(((Articulo) c).getTexto().substring(0, Math.min(100, ((Articulo) c).getTexto().length())) + "...");
        }
    }

}