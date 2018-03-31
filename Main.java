package assignment5;

import javafx.geometry.Insets;
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

//            for (int i = 0; i < 10; i++)
//			    Critter.makeCritter("Craig");

            Stage secondStage = new Stage();
            secondStage.setTitle("Controller");
            secondStage.show();

            //StackPane pane = new StackPane();
            GridPane secondGrid = new GridPane();
            Scene secondScene = new Scene(secondGrid, 250, 250);

            secondStage.setScene(secondScene);

            secondGrid.setHgap(10);
            secondGrid.setVgap(10);
            grid.setPadding(new Insets(0, 10, 0, 10));


            Button showBtn = new Button("Show");
//          secondGrid.getChildren().add(btn1);
//			btn1.setTranslateX(-80.0);
//			btn1.setTranslateY(-80.0);
            secondGrid.add(showBtn, 0, 0);

			Button makeBtn = new Button("Make");
//          secondGrid.getChildren().add(btn2);
//			btn2.setTranslateX(-80.0);
//			btn2.setTranslateY(-40.0);
            secondGrid.add(makeBtn, 0, 1);

            TextField classNameText = new TextField();
            HBox hbox = new HBox(classNameText);
            hbox.setMaxWidth(75);
//            secondGrid.getChildren().add(hbox);
            secondGrid.add(hbox,1, 1);

            TextField numberToMake = new TextField();
            HBox secondHbox = new HBox(numberToMake);
            secondHbox.setMaxWidth(50);
//            secondGrid.getChildren().add(hbox);
            secondGrid.add(secondHbox,2, 1);

            Button stepBtn = new Button("Step");
            secondGrid.add(stepBtn,0, 2);

            TextField stepsToTakeText = new TextField();
            HBox stepsHbox = new HBox(stepsToTakeText);
            stepsHbox.setMaxWidth(50);
//            secondGrid.getChildren().add(hbox);
            secondGrid.add(stepsHbox,1, 2);

            Button btn4 = new Button("Stats");
            secondGrid.add(btn4,0, 3);

            TextField classNameStatsText = new TextField();
            HBox thirdHbox = new HBox(classNameStatsText);
            thirdHbox.setMaxWidth(75);
//            secondGrid.getChildren().add(hbox);
            secondGrid.add(thirdHbox,1, 3);


            Button btn5 = new Button("Seed");
            secondGrid.add(btn5,0, 4);

            TextField seedText = new TextField();
            HBox fourthHbox = new HBox(seedText);
            fourthHbox.setMaxWidth(50);
//            secondGrid.getChildren().add(hbox);
            secondGrid.add(fourthHbox,1, 4);

			Button btn6 = new Button("Quit");
//            secondGrid.getChildren().add(btn3);
//			btn3.setTranslateX(-80.0);
            secondGrid.add(btn6,0, 5);

			/*
	        btn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent e) {
	            	Painter.paint(grid);
	            }
	        });
			*/

            showBtn.setOnAction(e-> {
                Critter.paint(grid);
            });

            makeBtn.setOnAction(e-> {
                int num = 1;
                String className = classNameText.getText();
                String number = numberToMake.getText();
                try {
                    if (number.length() != 0)
                        num = Integer.parseInt(number); // does this throw an exception?
                    while (num > 0) {
                        Critter.makeCritter(className);
                        num--;
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
                classNameText.clear();
                numberToMake.clear();

            });

            stepBtn.setOnAction(e-> {
                int num = 1;
                String number = stepsToTakeText.getText();
                try {
                    if (number.length() != 0)
                        num = Integer.parseInt(number);
                    while (num > 0) {
                        Critter.worldTimeStep();
                        num--;
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
                stepsToTakeText.clear();
            });



		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
