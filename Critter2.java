package assignment5;

import assignment5.Critter.TestCritter;
import javafx.scene.paint.Color;

public class Critter2 extends TestCritter {

    @Override
    public void doTimeStep() {
        if (look(2, false) == null) {
            walk(2); // walks up if nothing is there
        } else {
            walk(6); // or walks down
        }
        if (getEnergy() >= Params.min_reproduce_energy) {
            Critter2 child = new Critter2();
            reproduce(child, 2); // Always placed above.
        }

    }

    @Override
    public boolean fight(String opponent) {

        return true; // Always fights
    }

    @Override
    public String toString () {
        return "2";
    }

    @Override
    public CritterShape viewShape() { return CritterShape.SQUARE; }

    @Override
    public javafx.scene.paint.Color viewFillColor() { return Color.ORCHID; }

    @Override
    public javafx.scene.paint.Color viewOutlineColor() { return Color.DARKBLUE; }

}