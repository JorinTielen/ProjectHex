import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.GroundType;
import com.fantasticfive.game.enums.UnitType;

/**
 * The barracks class. Extends the Building class. Used in the game to purchase units.
 */
public class Barracks extends Building{
    private UnitType[] creatableUnits;
    private int purchaseCost;

    public Barracks(int health, Texture image, int purchaseCost, GroundType[] buildableOn) {
        super(health, image, buildableOn);
        this.purchaseCost = purchaseCost;
    }

    /**
     * Set the units barracks can produce
     * @param creatableUnits List of units barracks can produce
     */
    public void setCreatableUnits(UnitType[] creatableUnits) {
        this.creatableUnits = creatableUnits;
    }

    /**
     * Get a list of units barracks can produce
     * @return List of units barracks can produce
     */
    public UnitType[] getCreatableUnits(){
        return creatableUnits;
    }

    public int getPurchaseCost() { return purchaseCost; }
}
