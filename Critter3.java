package assignment5;

import assignment5.Critter.TestCritter;
import javafx.scene.paint.Color;

public class Critter3 extends TestCritter {

    @Override
    public void doTimeStep() {
        run(4); // Always runs to the left.
        if (getEnergy() >= Params.min_reproduce_energy) {
            Critter3 child = new Critter3();
            reproduce(child, 4); // Always placed to the left.
        }
    }

    @Override
    public boolean fight(String opponent) {
        if (opponent.equals("@"))
            return true;
        return getEnergy() >= (Params.start_energy / 2); // Fights if it has at least half of the starting energy
    }

    @Override
    public String toString () {
        return "3";
    }

    @Override
    public CritterShape viewShape() { return CritterShape.TRIANGLE; }

    @Override
    public javafx.scene.paint.Color viewColor() { return Color.ORANGE; }
}