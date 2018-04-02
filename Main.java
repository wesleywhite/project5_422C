package assignment5;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.lang.reflect.Method;
import java.util.List;

public class Main extends Application{

	public static void main(String[] args) {
		 launch(args);
	}
	@Override
	public void start(Stage primaryStage) { // primaryStage is created by Java VM
		try {
		    // View Window
			GridPane grid = new GridPane();
			Scene scene = new Scene(grid, 600, 600); // creates a scene object with the GridPane
			primaryStage.setScene(scene); // puts the scene onto the stage
			primaryStage.setTitle("View");
			primaryStage.show(); // display the stage with the scene

			Critter.displayWorld(grid); // paints the grid lines

            // Controller Window
            Stage secondStage = new Stage();
            secondStage.setTitle("Controller");
            secondStage.show();

            Stage thirdStage = new Stage();
            FlowPane flowPane = new FlowPane();
            Scene thirdScene = new Scene(flowPane, 500, 50);
            thirdStage.setScene(thirdScene);
            thirdStage.show();

            GridPane secondGrid = new GridPane();
            Scene secondScene = new Scene(secondGrid, 250, 250);
            secondStage.setScene(secondScene);
            secondGrid.setHgap(10);
            secondGrid.setVgap(10);
            grid.setPadding(new Insets(0, 10, 0, 10));

            Button showBtn = new Button("Show");
            secondGrid.add(showBtn, 0, 0);

			Button makeBtn = new Button("Make");
            secondGrid.add(makeBtn, 0, 1);

            TextField makeClassNameText = new TextField();
            HBox makeHbox = new HBox(makeClassNameText);
            makeHbox.setMaxWidth(75);
            secondGrid.add(makeHbox,1, 1);

            TextField numberToMakeText = new TextField();
            HBox secondMakeHbox = new HBox(numberToMakeText);
            secondMakeHbox.setMaxWidth(50);
            secondGrid.add(secondMakeHbox,2, 1);

            Button stepBtn = new Button("Step");
            secondGrid.add(stepBtn,0, 2);

            TextField stepsToTakeText = new TextField();
            HBox stepsHbox = new HBox(stepsToTakeText);
            stepsHbox.setMaxWidth(50);
            secondGrid.add(stepsHbox,1, 2);

            Button statsBtn = new Button("Stats");
            secondGrid.add(statsBtn,0, 3);

            TextField statsClassNameText = new TextField();
            HBox statsHbox = new HBox(statsClassNameText);
            statsHbox.setMaxWidth(75);
            secondGrid.add(statsHbox,1, 3);

            Button seedBtn = new Button("Seed");
            secondGrid.add(seedBtn,0, 4);

            TextField seedText = new TextField();
            HBox seedHbox = new HBox(seedText);
            seedHbox.setMaxWidth(50);
            secondGrid.add(seedHbox,1, 4);

			Button quitBtn = new Button("Quit");
            secondGrid.add(quitBtn,0, 5);

            // Text for the stats
            Label lbl = new Label();
            lbl.setText("Hey");
            flowPane.getChildren().add(lbl);


            // Action for show
            showBtn.setOnAction(e-> {
                Critter.paint(grid);
            });

            // Action for make
            makeBtn.setOnAction(e-> {
                int num = 1;
                String className = makeClassNameText.getText();
                String number = numberToMakeText.getText();
                try {
                    if (number.length() != 0)
                        num = Integer.parseInt(number); // does this throw an exception?
                    while (num > 0) {
                        Critter.makeCritter(className);
                        num--;
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    lbl.setText("Error Processing");
                }
                makeClassNameText.clear();
                numberToMakeText.clear();

            });

            // Action for step
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

            // Action for stats
            statsBtn.setOnAction(e-> {
                String className = statsClassNameText.getText();
                try {
                    List<Critter> temp = Critter.getInstances(className);
                    Class c = Class.forName("assignment5." + className);
                    Object o = c.newInstance();
                    String methodName = "runStats";
                    Method runStats = o.getClass().getMethod(methodName, List.class);
                    String stats = (String) runStats.invoke(o, temp);
                    System.out.println(stats);
                    lbl.setText(stats);

                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
                statsClassNameText.clear();

            });

            // Action for seed
            seedBtn.setOnAction(e-> {
                Long num;
                String seedNum = seedText.getText();
                try {
                    num = Long.parseLong(seedNum);
                    Critter.setSeed(num);
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
                seedText.clear();

            });

            // Action for quit
            quitBtn.setOnAction(e-> {
                System.exit(0);
            });


		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
