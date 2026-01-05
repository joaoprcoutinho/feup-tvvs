package pt.feup.tvvs.soulknight.model.game.scene;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.feup.tvvs.soulknight.model.dataStructs.Position;
import pt.feup.tvvs.soulknight.model.game.elements.collectables.Collectables;
import pt.feup.tvvs.soulknight.model.game.elements.enemies.Enemies;
import pt.feup.tvvs.soulknight.model.game.elements.knight.Knight;
import pt.feup.tvvs.soulknight.model.game.elements.particle.Particle;
import pt.feup.tvvs.soulknight.model.game.elements.Spike;
import pt.feup.tvvs.soulknight.model.game.elements.Tree;
import pt.feup.tvvs.soulknight.model.game.elements.rocks.Rock;
import pt.feup.tvvs.soulknight.model.game.elements.tile.Tile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SceneLoaderMutationTests {

    private SceneLoader loader;
    private Knight knight;

    @BeforeEach
    void setup() throws IOException {
        loader = new SceneLoader(0);
        knight = new Knight(1, 1, 1, 1, 1);
    }

    @Test
    public void testCreateScene_FullCoverage() {
        Scene scene = loader.createScene(knight);

        assertNotNull(scene.getPlayer());
        assertEquals(scene, scene.getPlayer().getScene());
        assertEquals(scene.getPlayer().getPosition(), scene.getStartPosition());

        assertNotNull(scene.getTiles());
        assertNotNull(scene.getWidth());
        assertNotNull(scene.getHeight());

        Spike[][] spikes = scene.getSpikes();
        boolean hasSpike = false;
        for (Spike[] row : spikes) {
            for (Spike s : row) {
                if (s != null) hasSpike = true;
            }
        }
        assertTrue(hasSpike);

        Tree[][] trees = scene.getTrees();
        boolean hasTree = false;
        for (Tree[] row : trees) {
            for (Tree t : row) {
                if (t != null) hasTree = true;
            }
        }
        assertTrue(hasTree);

        Rock[][] rocks = scene.getRocks();
        boolean hasRock = false;
        for (Rock[] row : rocks) {
            for (Rock r : row) {
                if (r != null) hasRock = true;
            }
        }
        assertTrue(hasRock);

        Collectables[][] orbs = scene.getOrbs();
        boolean hasOrb = false;
        for (Collectables[] row : orbs) {
            for (Collectables orb : row) {
                if (orb != null) hasOrb = true;
            }
        }
        assertTrue(hasOrb);

        List<Enemies> monsters = scene.getMonsters();
        assertNotNull(monsters);
        assertFalse(monsters.isEmpty());
        for (Enemies m : monsters) {
            assertNotNull(m.getPosition());
            assertEquals(scene, m.getScene());
        }

        List<Particle> particles = scene.getParticles();
        assertNotNull(particles);
        assertFalse(particles.isEmpty());

        scene.setEndPosition(new Position(0, 0));
        assertTrue(scene.isAtEndPosition());
        scene.setEndPosition(new Position(10, 0));
        scene.getPlayer().setPosition(new Position(11, 0));
        assertTrue(scene.isAtEndPosition());
    }

    @Test
    public void testCollisionMethods() {
        Scene scene = loader.createScene(knight);

        Position size = new Position(knight.getWidth(), knight.getHeight());

        knight.setPosition(new Position(0, 0));

        scene.collidesLeft(knight.getPosition(), size);
        scene.collidesRight(knight.getPosition(), size);
        scene.collidesUp(knight.getPosition(), size);
        scene.collidesDown(knight.getPosition(), size);

        scene.collideSpike();

        knight.setScene(scene);
        knight.isOnGround();
    }

    @Test
    public void testParticleArithmeticMutations() {
        Scene scene = loader.createScene(knight);
        List<Particle> particles = scene.getParticles();
        for (Particle p : particles) {
            assertTrue(p.getPosition().x() >= 0 && p.getPosition().x() <= scene.getWidth());
            assertTrue(p.getPosition().y() >= 0 && p.getPosition().y() <= scene.getHeight());
        }
    }
}
