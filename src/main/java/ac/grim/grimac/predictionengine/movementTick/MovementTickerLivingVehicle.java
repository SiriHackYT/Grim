package ac.grim.grimac.predictionengine.movementTick;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineNormal;
import ac.grim.grimac.predictionengine.predictions.PredictionEngineWater;
import ac.grim.grimac.predictionengine.predictions.rideable.PredictionEngineRideableNormal;
import ac.grim.grimac.utils.enums.MoverType;
import ac.grim.grimac.utils.nmsImplementations.BlockProperties;
import org.bukkit.util.Vector;

public class MovementTickerLivingVehicle extends MovementTicker {
    Vector movementInput = new Vector();

    public MovementTickerLivingVehicle(GrimPlayer player) {
        super(player);
    }

    @Override
    public void doWaterMove(float swimSpeed, boolean isFalling, float swimFriction) {
        Vector movementInputResult = new PredictionEngineNormal().getMovementResultFromInput(player, movementInput, swimSpeed, player.xRot);
        addAndMove(MoverType.SELF, movementInputResult);

        PredictionEngineWater.staticVectorEndOfTick(player, player.clientVelocity, swimFriction, player.gravity, isFalling);
    }

    @Override
    public void doLavaMove() {
        Vector movementInputResult = new PredictionEngineNormal().getMovementResultFromInput(player, movementInput, 0.02F, player.xRot);
        addAndMove(MoverType.SELF, movementInputResult);
    }

    @Override
    public void doNormalMove(float blockFriction) {
        new PredictionEngineRideableNormal(movementInput).guessBestMovement(BlockProperties.getFrictionInfluencedSpeed(blockFriction, player), player);
    }

    public void addAndMove(MoverType moverType, Vector movementResult) {
        player.clientVelocity.add(movementResult);
        super.move(moverType, player.clientVelocity);
    }
}
