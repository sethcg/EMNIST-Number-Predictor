package emnist_number_predictor.components.input;
import static emnist_number_predictor.util.Const.*;

import emnist_number_predictor.app.App;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class InputCell extends StackPane {

	public int row, column;

	public float colorValue = BLACK_COLOR_VALUE;
	private static final String INPUT_CELL_DEFAULT_STYLE = "input-cell-default";

	private ObjectProperty<Double> cellWidth = new SimpleObjectProperty<Double>(this, "cell-width", INIT_CELL_WIDTH);
	private ObjectProperty<Double> cellHeight = new SimpleObjectProperty<Double>(this, "cell-height", INIT_CELL_HEIGHT);

    public InputCell(int row, int column) {
		this.getStyleClass().add(INPUT_CELL_DEFAULT_STYLE);
		this.row = row;
		this.column = column;

		// Initialize cell size. 
		this.prefWidthProperty().bind(cellWidth);
		this.prefHeightProperty().bind(cellHeight);

		// Adjust cell size, when the Appliction window width or height changes.
		App.window.width.addListener(new ChangeListener<Double>(){
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				cellWidth.setValue(App.window.width.get() / GRID_SIZE);
			}
		});
		App.window.height.addListener(new ChangeListener<Double>(){
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				cellWidth.setValue(App.window.height.get() / GRID_SIZE);
			}
		});

     	// Select or deselect cell on MousePress
     	this.setOnMousePressed(new EventHandler<MouseEvent>(){
     		@Override
     		public void handle(MouseEvent event) { 
				updateColor(event);
				event.consume();
			}
     	});
     	
     	// Select or deselect cells on MouseDrag
        this.setOnDragDetected(new EventHandler<MouseEvent>(){
     		@Override
     		public void handle(MouseEvent event) { 
				((InputCell) event.getSource()).startFullDrag(); 
				event.consume();
			}
     	});
     	this.setOnMouseDragEntered(new EventHandler<MouseEvent>(){
     		@Override
     		public void handle(MouseEvent event) { 
				updateColor(event);
				event.consume();
			}
     	});
    }

	public boolean isSelected() {
		return this.colorValue == WHITE_COLOR_VALUE;
	}

	public void select() {
		this.setStyle("-fx-background-color: -fx-cell-black;");
		this.colorValue = WHITE_COLOR_VALUE;
	}

	public void deselect() {
		this.setStyle("-fx-background-color: -fx-cell-white;");
		this.colorValue = BLACK_COLOR_VALUE;
	}

    private void updateColor(MouseEvent event){
    	InputCell inputCell = (InputCell) event.getSource();
		if(event.isPrimaryButtonDown()) { 
			// Right MouseButton select
			this.select();
    	} else if(event.isSecondaryButtonDown()) { 
			// Right MouseButton deselect
			this.deselect();
    	}
		App.controller.updatePrediction(inputCell);
    }

}
