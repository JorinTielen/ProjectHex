import com.badlogic.gdx.graphics.Texture;
import com.fantasticfive.game.enums.GroundType;

public class Resource extends Building {
    private int productionPerTurn;
    private int purchaseCost;

    public Resource(int health, Texture image, int purchaseCost, int productionPerTurn, GroundType[] buildableOn) {
        super(health, image, buildableOn);
        this.purchaseCost = purchaseCost;
        this.productionPerTurn = productionPerTurn;
    }

    public int getPurchaseCost() { return purchaseCost; }

    public int getProductionPerTurn() {
        return this.productionPerTurn;
    }
}
