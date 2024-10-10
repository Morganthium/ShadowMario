import bagel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Sample solution for SWEN20003 Project 1, Semester 1, 2024
 * With amendments made for Project 2 by @author Tarish Kadam
 * @author Dimuthu Kariyawasan & Tharun Dharmawickrema
 *
 */
public class ShadowMario extends AbstractGame {

    private final int WINDOW_HEIGHT;
    private final String GAME_TITLE;
    private final Image BACKGROUND_IMAGE;
    private final String FONT_FILE;
    private final Font TITLE_FONT;
    private final int TITLE_X;
    private final int TITLE_Y;
    private final String INSTRUCTION;
    private final Font INSTRUCTION_FONT;
    private final int INS_Y;
    private final Font MESSAGE_FONT;
    private final int MESSAGE_Y;
    private final Properties PROPS;
    private final Properties MESSAGE_PROPS;
    private final Font SCORE_FONT;
    private final int SCORE_X;
    private final int SCORE_Y;
    private final Font HEALTH_FONT;
    private final int HEALTH_X;
    private final int HEALTH_Y;
    private int score;
    private boolean finished = false;
    private Player player;
    private Platform platform;
    private Enemy[] enemies;
    private Coin[] coins;
    private EndFlag endFlag;
    private EnemyBoss enemyBoss;
    private List<FlyingPlatform> flyingPlatforms;
    private List<DoubleScorePower> doublePowers = new ArrayList<>();
    private List<InvinciblePower> invinciblePowers = new ArrayList<>();

    private boolean started = false;

    public ShadowMario(Properties game_props, Properties message_props) {
        super(Integer.parseInt(game_props.getProperty("windowWidth")),
              Integer.parseInt(game_props.getProperty("windowHeight")),
              message_props.getProperty("title"));

        WINDOW_HEIGHT = Integer.parseInt(game_props.getProperty("windowHeight"));
        GAME_TITLE = message_props.getProperty("title");
        BACKGROUND_IMAGE = new Image(game_props.getProperty("backgroundImage"));
        FONT_FILE = game_props.getProperty("font");

        TITLE_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("title.fontSize")));
        TITLE_X = Integer.parseInt(game_props.getProperty("title.x"));
        TITLE_Y = Integer.parseInt(game_props.getProperty("title.y"));

        SCORE_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("score.fontSize")));
        SCORE_X = Integer.parseInt(game_props.getProperty("score.x"));
        SCORE_Y = Integer.parseInt(game_props.getProperty("score.y"));

        INSTRUCTION = message_props.getProperty("instruction");
        INSTRUCTION_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("instruction.fontSize")));
        INS_Y = Integer.parseInt(game_props.getProperty("instruction.y"));

        MESSAGE_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("message.fontSize")));
        MESSAGE_Y = Integer.parseInt(game_props.getProperty("message.y"));

        HEALTH_FONT = new Font(FONT_FILE, Integer.parseInt(game_props.getProperty("playerHealth.fontSize")));
        HEALTH_X = Integer.parseInt(game_props.getProperty("playerHealth.x"));
        HEALTH_Y = Integer.parseInt(game_props.getProperty("playerHealth.y"));

        this.PROPS = game_props;
        this.MESSAGE_PROPS = message_props;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");
        ShadowMario game = new ShadowMario(game_props, message_props);
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        // close window
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!started) {
            TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString(INSTRUCTION,
                    Window.getWidth() / 2 - INSTRUCTION_FONT.getWidth(INSTRUCTION)/2, INS_Y);

            // Check for key presses to start different levels
            if (input.wasPressed(Keys.NUM_1)) {
                loadLevel("level1File");
            } else if (input.wasPressed(Keys.NUM_2)) {
                loadLevel("level2File");
            } else if (input.wasPressed(Keys.NUM_3)) {
                loadLevel("level3File");
            }
        } else if (player.isDead() && player.getY() > WINDOW_HEIGHT) {
            String message = MESSAGE_PROPS.getProperty("gameOver");
            MESSAGE_FONT.drawString(message,
                    Window.getWidth() / 2 - MESSAGE_FONT.getWidth(message)/2,
                    MESSAGE_Y);
            if (input.wasPressed(Keys.SPACE)) {
                started = false;
            }
        } else {
            if (finished) {
                String message = MESSAGE_PROPS.getProperty("gameWon");
                MESSAGE_FONT.drawString(message,
                        Window.getWidth() / 2 - MESSAGE_FONT.getWidth(message)/2,
                        MESSAGE_Y);
                if(input.wasPressed(Keys.SPACE)) {
                    started = false;
                 }
            } else {
                // game is running
                SCORE_FONT.drawString(MESSAGE_PROPS.getProperty("score") + score, SCORE_X, SCORE_Y);
                HEALTH_FONT.drawString(MESSAGE_PROPS.getProperty("health") + Math.round(player.getHealth()*100),
                        HEALTH_X, HEALTH_Y);
                updateGameObjects(input);
            }
        }
    }

    /**
     * Method that creates the game objects using the lines read from the CSV file.
     */

    private void populateGameObjects(String[][] lines) {
        List<Coin> coinList = new ArrayList<>();
        List<Enemy> enemyList = new ArrayList<>();
        List<FlyingPlatform> flyingPlatformList = new ArrayList<>();
        List<DoubleScorePower> doublePowerList = new ArrayList<>();
        List<InvinciblePower> invinciblePowerList = new ArrayList<>();

        for (String[] lineElement : lines) {
            int x = Integer.parseInt(lineElement[1]);
            int y = Integer.parseInt(lineElement[2]);

            switch (lineElement[0]) {
                case "PLAYER":
                    player = new Player(x, y, this.PROPS);
                    break;
                case "PLATFORM":
                    platform = new Platform(x, y, this.PROPS);
                    break;
                case "ENEMY":
                    Enemy enemy = new Enemy(x, y, this.PROPS);
                    enemyList.add(enemy);
                    break;
                case "COIN":
                    Coin coin = new Coin(x, y, this.PROPS);
                    coinList.add(coin);
                    break;
                case "FLYING_PLATFORM":
                    FlyingPlatform flyingPlatform = new FlyingPlatform(x, y, this.PROPS);
                    flyingPlatformList.add(flyingPlatform);
                    break;
                case "DOUBLE_SCORE":
                    DoubleScorePower doubleScorePower = new DoubleScorePower(x, y, this.PROPS);
                    doublePowerList.add(doubleScorePower);
                    break;
                case "INVINCIBLE_POWER":
                    InvinciblePower invinciblePower = new InvinciblePower(x, y, this.PROPS);
                    invinciblePowerList.add(invinciblePower);
                    break;
                case "ENEMY_BOSS":
                    int bossHealthX = Integer.parseInt(this.PROPS.getProperty("enemyBossHealth.x"));
                    int bossHealthY = Integer.parseInt(this.PROPS.getProperty("enemyBossHealth.y"));
                    enemyBoss = new EnemyBoss(x, y, this.PROPS, HEALTH_FONT, bossHealthX, bossHealthY);
                    break;
                case "END_FLAG":
                    endFlag = new EndFlag(x, y, this.PROPS);
                    break;
            }
        }

        // Convert lists to arrays or store them as lists
        coins = coinList.toArray(new Coin[0]);
        enemies = enemyList.toArray(new Enemy[0]);
        flyingPlatforms = flyingPlatformList;
        doublePowers = doublePowerList;
        invinciblePowers = invinciblePowerList;
    }


    // Method to load each level dependent on keypress
    private void loadLevel(String levelKey) {
        started = true;
        finished = false;
        score = 0;

        String levelFilePath = this.PROPS.getProperty(levelKey);
        if (levelFilePath != null) {
            String[][] lines = IOUtils.readCsv(levelFilePath);
            populateGameObjects(lines);
        } else {
            System.err.println("Level file path not found for key: " + levelKey);
        }
    }

    /**
     * Method that updates the game objects each frame, when the game is running.
     */
    public void updateGameObjects(Input input) {
        platform.update(input);

        int halfLength = Integer.parseInt(PROPS.getProperty("gameObjects.flyingPlatform.halfLength"));
        int halfHeight = Integer.parseInt(PROPS.getProperty("gameObjects.flyingPlatform.halfHeight"));

        for(FlyingPlatform fp : flyingPlatforms) {
            fp.update(input); // Update each flying platform
            // Check if the player can land on the flying platform
            if (CollisionDetector.canLandOnPlatform(player, fp, halfLength, halfHeight)) {
                player.setVerticalSpeed(0); // Stops vertical movement, simulating landing
                player.setY(fp.getY() - halfHeight); // Adjust player's Y to sit on the platform
            }
        }
        for (DoubleScorePower dp : doublePowers) {
            dp.updateWithTarget(input, player);
        }

        for (InvinciblePower ip : invinciblePowers) {
            ip.updateWithTarget(input, player);
        }

        boolean doubleScoreActive = false;
        boolean invincibleActive = false;

        for (DoubleScorePower dp : doublePowers) {
            if (dp.isActive()) {
                doubleScoreActive = true;
                break;
            }
        }

        for (InvinciblePower ip : invinciblePowers) {
            if (ip.isActive()) {
                invincibleActive = true;
                break;
            }
        }

        for (Enemy e : enemies) {
            if (invincibleActive) {
                // Ignore damage if invincible
                e.updateWithoutDamage(input, player);
            } else {
                e.updateWithTarget(input, player);
            }
        }

        for (Coin c : coins) {
            int collectedValue = c.updateWithTarget(input, player);
            if (doubleScoreActive) {
                score += collectedValue * 2; // Double the score DoubleScorePower is active
            } else {
                score += collectedValue;
            }
        }

        for (Fireball fireball : player.getFireballs()) {
            fireball.update();
            fireball.checkCollision(player);
            fireball.checkCollision(enemyBoss);
        }

        if (enemyBoss != null) {
            enemyBoss.update(input, player);
        }

        player.update(input);
        endFlag.updateWithTarget(input, player);

        // Modified win condition for level 3
        if (endFlag.isCollided() && (enemyBoss == null || enemyBoss.isDead())) {
            finished = true;
        }
    }
}