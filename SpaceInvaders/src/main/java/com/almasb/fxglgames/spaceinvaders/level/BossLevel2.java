package com.almasb.fxglgames.spaceinvaders.level;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.time.LocalTimer;
import com.almasb.fxglgames.spaceinvaders.Config;
import javafx.util.Duration;

import static com.almasb.fxgl.app.DSLKt.play;
import static com.almasb.fxgl.app.DSLKt.runOnce;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class BossLevel2 extends BossLevel {

    @Override
    public void init() {
        Entity boss = spawnBoss(Config.WIDTH / 2, Config.HEIGHT / 2, 50, "boss2.png");
        boss.addComponent(new Level2BossComponent());
    }

    private static class Level2BossComponent extends Component {

        private LocalTimer invisTimer = FXGL.newLocalTimer();
        private LocalTimer attackTimer = FXGL.newLocalTimer();
        private Duration nextAttack = Duration.seconds(0.25);

        @Override
        public void onUpdate(double tpf) {

            if (invisTimer.elapsed(Duration.seconds(5))) {
                entity.getComponent(CollidableComponent.class).setValue(false);
                entity.getView().setVisible(false);

                runOnce(() -> {
                    entity.getComponent(CollidableComponent.class).setValue(true);
                    entity.getView().setVisible(true);
                }, Duration.seconds(2));

                invisTimer.capture();
            }

            if (entity.getView().isVisible()) {
                if (attackTimer.elapsed(nextAttack)) {
                    shoot();

                    nextAttack = Duration.seconds(0.25);
                    attackTimer.capture();
                }
            }
        }

        private void shoot() {
            GameWorld world = getEntity().getWorld();
            Entity bullet = world.spawn("Bullet", new SpawnData(0, 0).put("owner", getEntity()));

            play("shoot" + (int)(Math.random() * 4 + 1) + ".wav");
        }
    }
}
