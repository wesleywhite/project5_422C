package assignment5;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape();

	private static String myPackage;
	private static int timestep = 0;
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static List<Critter> collection = new java.util.ArrayList<Critter>();
//	private static String[][] board = new String[Params.world_width][Params.world_height];
    private static Critter[][] board = new Critter[Params.world_width][Params.world_height];

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}

	private static java.util.Random rand = new java.util.Random();

	/**
	 * @return random int between 0 and max not inclusive.
	 * @param max of random number.
	 */
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	/**
	 * Sets the seed of the RNG for testing purposes.
	 * @param new_seed of RNG
	 */
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	protected final String look(int direction, boolean steps) {
	    int step = 1; // walk
	    if (steps)
	        step = 2; // run

        energy -= Params.look_energy_cost;
        List<Integer> coords = getCoords(direction, step, x_coord, y_coord);
        int x = coords.get(0);
        int y = coords.get(1);

        // probably could get rid of the for loop just look at the board
        for (Critter crit : collection) {
            if (crit.x_coord == x && crit.y_coord == y)
                return crit.toString();

        }

	    return null;
    }

    private static List<Integer> getCoords(int direction, int steps, int x0, int y0) {
	    List<Integer> list = new ArrayList<Integer>();
        int x = x0;
	    int y = y0;
	    int width = Params.world_width;
	    int height = Params.world_height;

        switch(direction) {
            // right
            case 0: x = (x + steps) % width;
                break;
            // right up
            case 1: y = Math.floorMod(y - steps, height);
                x = (x + steps) % width;
                break;
            // up
            case 2: y = Math.floorMod(y - steps, height);
                break;
            // left up
            case 3: x = Math.floorMod(x - steps, width);
                y = Math.floorMod(y - steps, height);
                break;
            // left
            case 4: x = Math.floorMod(x - steps, width);
                break;
            // down left
            case 5: x = Math.floorMod(x - steps, width);
                y = (y + steps) % height;
                break;
            // down
            case 6:	y = (y + steps) % height;
                break;
            // down right
            case 7: x = (x + steps) % width;
                y = (y + steps) % height;
                break;
        }

        list.add(x);
        list.add(y);
	    return list;
    }
	
	/* rest is unchanged from Project 4 */


	/**
	 * @return A one-character long string that visually depicts your critter in the ASCII interface.
	 */
	public String toString() {
		return "";
	}

	private int energy = 0;
	/**
	 * @return energy of the Critter.
	 */
	protected int getEnergy() {
		return energy;
	}
	
	private int x_coord;
	private int y_coord;
	private boolean hasMoved;
	private static int size = 500/Params.world_width;

    /**
     * Walks the Critter one place in the given direction. Energy cost is deducted.
     * This function does not care if another Critter is in the destination.
     * It does update the board appropriately.
     * @param direction of the walk
     */
    protected final void walk(int direction) {

        this.energy -= Params.walk_energy_cost; // Deduct the walk energy cost
        if (!hasMoved) {
            int oldX = x_coord;
            int oldY = y_coord;

            board[x_coord][y_coord] = null; // Clear the board of where it was

            move(direction, 1);
//            board[x_coord][y_coord] = this.toString(); // Add to new place on board
            board[x_coord][y_coord] = this; // Add to new place on board

            for (Critter critter : collection) { // But replace it if there is something else there too
                if (critter.x_coord == oldX && critter.y_coord == oldY) {
//                    board[oldX][oldY] = critter.toString();
                    board[oldX][oldY] = critter;
                }
            }
        }
    }

    /**
     * Run the Critter two places in the given direction. Energy cost is deducted.
     * This function does not care if another Critter is in the destination.
     * It does update the board appropriately.
     * @param direction of the run
     */
    protected final void run(int direction) {

        this.energy -= Params.run_energy_cost; // Deduct the run energy cost
        if (!hasMoved) {
            int oldX = x_coord;
            int oldY = y_coord;

            board[x_coord][y_coord] = null; // Clear the board of where it was

            move(direction, 2);
            board[x_coord][y_coord] = this; // Add to new place on board

            for (Critter critter : collection) { // But replace it if there is something else there too
                if (critter.x_coord == oldX && critter.y_coord == oldY) {
                    board[oldX][oldY] = critter;
                }
            }
        }
    }

    /**
     * Helper function that both run and walk call. Moves the Critter in given direction the given amount of steps.
     * Sets the hasMoved flag of the Critter it moves.
     * @param direction of the move.
     * @param steps to take in that direction.
     */
    private void move(int direction, int steps) {

        hasMoved = true;

        int height = Params.world_height;
        int width = Params.world_width;

        switch(direction) {
            // right
            case 0: x_coord = (x_coord + steps) % width;
                break;
            // right up
            case 1: x_coord = (x_coord + steps) % width;
                y_coord = Math.floorMod(y_coord - steps, height);
                break;
            // up
            case 2: y_coord = Math.floorMod(y_coord - steps, height);
                break;
            // left up
            case 3: x_coord = Math.floorMod(x_coord - steps, width);
                y_coord = Math.floorMod(y_coord - steps, height);
                break;
            // left
            case 4: x_coord = Math.floorMod(x_coord - steps, width);
                break;
            // down left
            case 5: x_coord = Math.floorMod(x_coord - steps, width);
                y_coord = (y_coord + steps) % height;
                break;
            // down
            case 6:	y_coord = (y_coord + steps) % height;
                break;
            // down right
            case 7: x_coord = (x_coord + steps) % width;
                y_coord = (y_coord + steps) % height;
                break;
        }
    }

    /**
     * Handles the energy manipulation of the baby and parent.
     * Adds the baby to the world board.
     * @param direction of where the baby is placed.
     * @param offspring to be added to world.
     */
    protected final void reproduce(Critter offspring, int direction) {
        if (this.energy >= Params.min_reproduce_energy) {
            offspring.energy = Math.floorDiv(this.energy, 2); // 1/2 rounding down
            this.energy = (int) Math.ceil((double) this.energy / 2); // 1/2 rounding up


            offspring.x_coord = this.x_coord;
            offspring.y_coord = this.y_coord; // Give parent's coordinates

            offspring.walk(direction); // This puts the baby on the board, but not in the collection.
            offspring.energy += Params.walk_energy_cost; // Adds back energy that was taken out in walk.

            babies.add(offspring); // Not added to the collection until after the time step.

        }
    }

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);

	public static void displayWorld(Object pane) {} 
	/* Alternate displayWorld, where you use Main.<pane> to reach into your
	   display component.
	   // public static void displayWorld() {}
	*/

    /**
     * Display the world with the Critters in the correct position.
     * If two or more Critters are in the same place, only one shows (could be any one).
     */
    public static void displayWorld(GridPane grid) {
        paintGridLines(grid);

        /*
        // Print top border
        System.out.print("+");
        for(int i = 0; i < Params.world_width; i++) {
            System.out.print("-");
        }
        System.out.println("+");
		/*
		for each space, check if critter occupies it
			if so: print their symbol
			else: print an empty space
		*/
        /*
        for(int y = 0; y < Params.world_height; y++) {
            System.out.print('|');
            for(int x = 0; x < Params.world_width; x++) {
                if(board[x][y] == null) {
                    System.out.print(' ');
                }
                else {
                    System.out.print(board[x][y]);
                }
            }
            System.out.println('|');
        }
        //print bottom border
        System.out.print("+");
        for(int i = 0; i < Params.world_width; i++) {
            System.out.print("-");
        }
        System.out.println("+");
        */
    }

    private static void paintGridLines(GridPane grid) {
         for (int r = 0; r < Params.world_width; r++)
            for (int c = 0; c < Params.world_height; c++) {
                Shape s = new Rectangle(size, size);
                s.setFill(null);
                s.setStroke(Color.BLACK);
                grid.add(s, c, r);
            }

    }
    /*
     * Paints the icon shapes on a grid.
     */
    public static void paint(GridPane grid) {
        grid.getChildren().clear();
        paintGridLines(grid);

        for (int r = 0; r < Params.world_width; r++)
            for (int c = 0; c < Params.world_height; c++) {
                if (board[r][c] != null) { // There is a critter there
                    Critter crit = board[r][c];
                    Shape s = crit.getIcon();
                    grid.add(s, r, c); // or r , c
                }
            }

    }

    /*
     * Returns a square or a circle depending on the shapeIndex parameter
     *
     */
    private Shape getIcon() {
        Shape s = null;
        Color color = viewColor();
        Color outline = viewOutlineColor();
        Color fill = viewFillColor();
        CritterShape cs = viewShape();
        Polygon polygon;

        switch(cs) {
            case SQUARE: s = new Rectangle(size, size);
                s.setFill(fill);
                break;
            case CIRCLE: s = new Circle(size/2);
                s.setFill(fill);
                break;
            case STAR: polygon = new Polygon();
                polygon.getPoints().addAll(new Double[]{
                        (double) (size - 1) / 2, 2.5, // WAS 10.0, 0.0 the 2.5 needs to be fixed
                        (double) (size - 1) / 3, (double) (size - 1) / 3,
                        0.0, (double)  (size - 1) / 3,
                        (double) (size - 1) / 3, (double) (size - 1) / 1.66,
                        (double) (size - 1) / 6, (double) size - 1,
                        (double) (size - 1) / 2, (double) (size - 1) / 1.5,
                        (double) (size - 1) / 1.1667, (double) size - 1, // 7
                        (double) (size - 1) / 1.5, (double) (size - 1) / 1.66,
                        (double) size - 3.5, (double) (size - 1) / 3, //9 // this -3.5 needs to be fixed
                        (double) (size - 1) / 1.5, (double) (size - 1) / 3}); // 10
                s = polygon;
                s.setFill(fill);
                break;
            case DIAMOND: polygon = new Polygon();
                polygon.getPoints().addAll(new Double[]{
                        (double) (size - 1) / 2, 1.0, // was 10.0, 0.0 the 1.0
                        0.0, (double) (size - 1) / 2,
                        (double) (size - 1) / 2, (double) size - 1,
                        (double) size - 2, (double) (size - 1) / 2}); // was 15, 10 // the -2
                s = polygon;
                s.setFill(fill);
                break;
            case TRIANGLE: polygon = new Polygon();
                polygon.getPoints().addAll(new Double[]{
                        (double) (size/2 - 1), 0.0, // WAS 10.0, 0.0
                        0.0, (double) size - 2, // was 0.0, 20.0 // the -2 everywhere
                        (double) size - 2, (double) size - 2});
                s = polygon;
                s.setFill(fill);
                break;
        }
        // set the outline
        s.setStroke(outline);
        return s;
    }


    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
     * an InvalidCritterException must be thrown.
     * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
     * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
     * an Exception.)
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void makeCritter(String critter_class_name) throws InvalidCritterException {
        try {
            String className = myPackage + "." + critter_class_name;
            Class c = Class.forName(className);
            Critter newCritter = (Critter) c.newInstance();
            newCritter.energy = Params.start_energy;
            newCritter.x_coord = getRandomInt(Params.world_width);
            newCritter.y_coord = getRandomInt(Params.world_height);
            newCritter.hasMoved = false;
            collection.add(newCritter);
//            board[newCritter.x_coord][newCritter.y_coord] = newCritter.toString();
            board[newCritter.x_coord][newCritter.y_coord] = newCritter;
        }
        catch(Exception e) {
            throw new InvalidCritterException(critter_class_name);
        }
    }

    /**
     * Gets a list of critters of a specific type.
     * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
        List<Critter> result = new java.util.ArrayList<Critter>();
        try {
            String className = myPackage + "." + critter_class_name;
            Class c = Class.forName(className);
            for (Critter critter : collection) {
                if (critter.getClass().equals(c)) {
                    result.add(critter);
                }
            }
        } catch(Exception e) {
            throw new InvalidCritterException(critter_class_name);
        }
        return result;
    }

    /**
     * Prints out how many Critters of each type there are on the board.
     * @param critters List of Critters.
     */
    public static String runStats(List<Critter> critters) {
        String s = "" + critters.size() + " critters as follows -- ";
        java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            Integer old_count = critter_count.get(crit_string);
            if (old_count == null) {
                critter_count.put(crit_string,  1);
            } else {
                critter_count.put(crit_string, old_count.intValue() + 1);
            }
        }
        String prefix = "";
        for (String str : critter_count.keySet()) {
            s = s + prefix + str + ":" + critter_count.get(str);
            prefix = ", ";
        }
        return s;
    }
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure thath the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctup update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
            if (super.energy <= 0) {
                Critter.remove(this);
            }
        }

        protected void setX_coord(int new_x_coord) {
            int oldX = super.x_coord, oldY = super.y_coord;
            board[oldX][oldY] = null;
            super.x_coord = new_x_coord;
//            board[super.x_coord][super.y_coord] = this.toString();
            board[super.x_coord][super.y_coord] = this;
            for (Critter critter : collection) {
                if (critter.x_coord == oldX && critter.y_coord == oldY) {
//                    board[oldX][oldY] = critter.toString();
                    board[oldX][oldY] = critter;
                }
            }
        }

        protected void setY_coord(int new_y_coord) {
            int oldX = super.x_coord, oldY = super.y_coord;
            board[oldX][oldY] = null;
            super.y_coord = new_y_coord;
            board[super.x_coord][super.y_coord] = this;
            for (Critter critter : collection) {
                if (critter.x_coord == oldX && critter.y_coord == oldY) {
                    board[oldX][oldY] = critter;
                }
            }
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return collection;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

    /**
     * Clear the world of all critters, dead and alive.
     */
    public static void clearWorld() {
        // Remove critters from collection
        collection.clear();
        // Clear the board
        for (int i = 0; i < Params.world_height; i++) {
            for (int j = 0; j < Params.world_width; j++) {
                board[i][j] = null;
            }
        }
    }

    /**
     * Handles all the activities of on world time step.
     * 1. doTimeStep
     * 2. doEncounter
     * 3. updateEnergy
     * 4. Generate Algae
     * 5. Move babies to general population
     */
    public static void worldTimeStep() {

        // 1. increment timestep
        timestep++;

        // 2. doTimeSteps(); This is where each critter will call walk/run
        for(Critter critter : collection) {
            critter.doTimeStep();
        }

        // 3. Do the fights. doEncounters();
        doEncounters();
        // Clear the hasMoved flag each time step
        for (Critter critter : collection) {
            critter.hasMoved = false;
        }

        // 4. updateRestEnergy();
        List<Critter> remove = new ArrayList<Critter>();
        for(Critter critter : collection) {
            critter.energy -= Params.rest_energy_cost;
            if (critter.energy <= 0) {
                remove.add(critter);
                board[critter.x_coord][critter.y_coord] = null;
                for (Critter crit : collection) {
                    if (!crit.equals(critter) && crit.x_coord == critter.x_coord && crit.y_coord == critter.y_coord)
//                        board[crit.x_coord][crit.y_coord] = crit.toString();
                        board[crit.x_coord][crit.y_coord] = crit;
                }
            }
        }
        collection.removeAll(remove);

        // 5. Generate Algae genAlgae();
        genAlgae();

        // 6. Move babies to general population. They are already on the board.
        collection.addAll(babies);
        babies.clear();
    }

    /**
     * Generates refresh_algae_count number of Algae and places them at random places in the world.
     */
    private static void genAlgae() {
        for (int i = 0; i < Params.refresh_algae_count; i++) {
            Algae alg = new Algae();
            alg.setEnergy(Params.start_energy);
            int x = getRandomInt(Params.world_height);
            int y = getRandomInt(Params.world_width);
            alg.setX_coord(x);
            alg.setY_coord(y);
            collection.add(alg);
//            board[x][y] = alg.toString();
            board[x][y] = alg;
        }
    }

    /**
     * Determines if there are two Critters in the same place.
     * Returns a list of 2 critters if they occupy the same spot in the world.
     * If no Critters are in the same spot, returns null.
     */
    private static List<Critter> samePlace() {
        for (int i = 0; i < collection.size() - 1; i++) {
            for (int j = i + 1; j < collection.size(); j++) {

                if (collection.get(i).x_coord == collection.get(j).x_coord && collection.get(i).y_coord == collection.get(j).y_coord) {
                    ArrayList<Critter> tempList = new ArrayList<Critter>();
                    tempList.add(collection.get(j));
                    tempList.add(collection.get(i));
                    return tempList;
                }

            }
        }
        return null;
    }

    /**
     * Determines if a certain spot in the world is currently occupied.
     * @param x is the x_coord to check
     * @param y is the y_coord to check
     * Returns true if the given x and y is occupied, false otherwise.
     */
    private static boolean isOccupied(int x, int y) {
        for (Critter crit : collection) {
            if (crit.x_coord == x && crit.y_coord == y)
                return true;
        }
        return false;
    }
    /**
     * Removes critter from the collection, and replaces its spot with another critter if there is one there.
     * @param critter to be removed.
     */
    private static void remove(Critter critter) {
        collection.remove(critter);
        board[critter.x_coord][critter.y_coord] = null;
        for (Critter crit : collection) {
            if (crit.x_coord == critter.x_coord && crit.y_coord == critter.y_coord)
//                board[critter.x_coord][critter.y_coord] = crit.toString();
                board[critter.x_coord][critter.y_coord] = crit;
        }

    }

    /**
     * Handles all the fighting logic.
     * If they don't want to fight, they will try to run away.
     * If they fight, whoever rolls the highest wins, and the loser dies.
     */
    private static void doEncounters() {
        List<Critter> crits = samePlace();

        while (crits != null) {

            Critter first = crits.get(0);
            Critter second = crits.get(1);
            int firstRoll, secondRoll;
            boolean firstFight, secondFight;

            if (first.toString().equals("@")) {
                // Algae cannot run away.
                first.hasMoved = true;
            }

            if (second.toString().equals("@")) {
                // Algae cannot run away.
                second.hasMoved = true;
            }

            firstFight = first.fight(second.toString());
            if (!firstFight) {
                // Wants to run away
                // And has not moved yet in time step
                if (!first.hasMoved) {
                    // int random = getRandomInt(8);
                    int x = (first.x_coord + 1) % Params.world_width;
                    int y = first.y_coord;
                    if (!isOccupied(x, y)) {
                        first.walk(0);
                        first.energy += Params.walk_energy_cost; // Add back the energy, it is subtracted later
                    }
                }
                first.energy -= Params.walk_energy_cost; // Subtract energy even if it cannot walk
            }
            if (first.energy <= 0) {
                remove(first);
            }

            secondFight = second.fight(first.toString());
            if (!secondFight) {
                // Wants to run away
                // And has not moved yet in time step
                if (!second.hasMoved) {
                    // int random = getRandomInt(8);
                    int x = Math.floorMod(second.x_coord - 1, Params.world_width); // tries to go left
                    int y = second.y_coord;
                    if (!isOccupied(x, y)) {
                        second.walk(4);
                        second.energy += Params.walk_energy_cost; // Add back the energy, it is subtracted later
                    }
                }
                second.energy -= Params.walk_energy_cost; // Subtract energy even if it cant walk
            }
            if (second.energy <= 0) {
                remove(second);
            }


            if (first.x_coord == second.x_coord && first.y_coord == second.y_coord && collection.contains(first) && collection.contains(second)) {

                // Roll the dice
                if (firstFight) {
                    firstRoll = getRandomInt(first.energy);
                } else if (first.toString().equals("@")) {
                    firstRoll = -1;
                } else {
                    firstRoll = 0;
                }

                if (secondFight) {
                    secondRoll = getRandomInt(second.energy);
                } else if (second.toString().equals("@")) {
                    secondRoll = -2;
                } else {
                    secondRoll = 0;
                }

                // Check who wins
                if (firstRoll >= secondRoll) {
                    first.energy += second.energy / 2;
//                    board[second.x_coord][second.y_coord] = first.toString();
                    board[second.x_coord][second.y_coord] = first;
                    collection.remove(second);
                } else {
                    second.energy += first.energy / 2;
//                    board[first.x_coord][first.y_coord] = second.toString();
                    board[first.x_coord][first.y_coord] = second;
                    collection.remove(first);
                }

            }

            crits = samePlace();
        }

    }
	
}
