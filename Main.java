package assignment5;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Main extends Application{

	public static void main(String[] args) {
		 launch(args);
	}
	@Override
	public void start(Stage primaryStage) { // primaryStage is created by Java VM
		try {
			GridPane grid = new GridPane();
			Scene scene = new Scene(grid, 600, 600); // creates a scene object with the GridPane
			primaryStage.setScene(scene); // puts the scene onto the stage
			primaryStage.setTitle("View");
			primaryStage.show(); // display the stage with the scene

			Critter.displayWorld(grid); // paints the icons on the grid

            Stage secondStage = new Stage();
            secondStage.setTitle("Controller");
            secondStage.show();

            StackPane pane = new StackPane();
            Scene secondS = new Scene(pane, 250, 250);

            secondStage.setScene(secondS);

            Button btn1 = new Button("Show");
            pane.getChildren().add(btn1);
			btn1.setTranslateX(-80.0);
			btn1.setTranslateY(-80.0);

			Button btn2 = new Button("IDK YET");
			pane.getChildren().add(btn2);
			btn2.setTranslateX(-80.0);
			btn2.setTranslateY(-40.0);

			Button btn3 = new Button("Quit");
			pane.getChildren().add(btn3);
			btn3.setTranslateX(-80.0);

			/*
	        btn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent e) {
	            	Painter.paint(grid);
	            }
	        });
			*/
            /*
            btn.setOnAction(e-> {
                System.out.println(e);
                Painter.paint(grid);
            });

        */

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
