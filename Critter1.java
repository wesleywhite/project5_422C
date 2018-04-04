package assignment5;

import assignment5.Critter.TestCritter;
import javafx.scene.paint.Color;

public class Critter1 extends TestCritter {

    @Override
    public void doTimeStep() {
        walk(0); // Always walks to the right.
        if (getEnergy() >= Params.min_reproduce_energy) {
            Critter1 child = new Critter1();
            reproduce(child, 0); // Always placed to the right.
        }

    }

    @Override
    public boolean fight(String opponent) {
        if (look(0, false) == null) // Fights if there is nothing to the right of it/
            return true;
        return false;
    }

    @Override
    public String toString () {
        return "1";
    }

    @Override
    public CritterShape viewShape() { return CritterShape.DIAMOND; }

    @Override
    public javafx.scene.paint.Color viewFillColor() { return Color.RED; }

    @Override
    public javafx.scene.paint.Color viewOutlineColor() { return Color.YELLOWGREEN; }
}