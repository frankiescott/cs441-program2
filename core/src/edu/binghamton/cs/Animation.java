package edu.binghamton.cs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static java.lang.Math.abs;

public class Animation extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private Stage stage; //used for control buttons

	private float scoreSeconds = 0f; //timer for score
	private float jumpSeconds = 0f; //timer for enemy jumping

	private float w, h;
	private float gravity;

	private ShapeRenderer shapeRenderer; //health bar

	private Label totalHealth;
	private Label scoreDisplay;
	private Label highScoreDisplay;

	private Player player;
	private Enemy enemy;

	private int score;
	private int highScore;

	private Skin loadSkin() {
		return new Skin(Gdx.files.internal("clean-crispy-ui.json"));
	}

	//resets player health, resets player & enemy positions, resets score, and updates high score if necessary
	public void resetGame() {
		player.health = 500;
		totalHealth.setText("Health: " + player.health + "/500");
		if (this.score > this.highScore) {
			this.highScore = this.score;
			highScoreDisplay.setText("High Score: " + this.highScore);
		}
		this.score = 0;
		player.x = 0;
		player.y = 0;
		enemy.x = Gdx.graphics.getWidth() - 100;
		enemy.y = 0;
	}

	class Player extends GameObject {
		int health; 

		Player(int x, int y, int dx, int dy, String img, int health) {
			super(x, y, dx, dy, img);
			this.health = health;
		}

		//since the player has health, we need to override the update method to take care of damage
		@Override
		public void update() {
			//jumping physics
			if ((this.y <= 4) && (abs(this.dy) <= 4)) {
				this.y = 0;
				this.dy = 0;
			} else {
				this.dy = this.dy + gravity;
			}

			//update position on screen
			this.x = this.x + this.dx;
			this.y = this.y + this.dy;

			//check if player is colliding with the enemy
			if (isColliding(player, enemy)) {
				if (this.health - 5 <= 0) {
					resetGame();
				} else {
					this.health = this.health - 5;
					totalHealth.setText("Health: " + this.health + "/500");
				}
			}

			//wall collision
			if ((this.x > (w - this.imgWidth)) || (this.x < 0)) {
				this.dx = -this.dx;
			}

			//ground collision
			if (this.y < 0) {
				this.dy = 0;
				this.y = 0;
			}
		}
	}

	class Enemy extends GameObject {
		int jumpTimer;

		Enemy(int x, int y, int dx, int dy, String img) {
			super(x, y, dx, dy, img);
			this.jumpTimer = (int) (Math.random() * 5 + 2);
		}

		//generates a new random jump timer
		public void updateJumpTimer() {
			this.jumpTimer = (int) (Math.random() * 4 + 1);
		}

		public void jump() {
			if (this.dy == 0) {
				this.dy = this.dy + 80;
			}
			this.updateJumpTimer();
		}
	}

	//generic GameObject class
	class GameObject {
		float x, y;
		float dx, dy;
		Texture img;
		int imgWidth, imgHeight;

		GameObject(int x, int y, int dx, int dy, String img) {
			this.x = (float) x;
			this.y = (float) y;
			this.dx = (float) dx;
			this.dy = (float) dy;
			this.img = new Texture(img);
			this.imgHeight = this.img.getHeight();
			this.imgWidth = this.img.getWidth();
		}

		public void render() {
			batch.begin();
			batch.draw(this.img, this.x, this.y);
			batch.end();
		}

		public boolean isColliding(GameObject a, GameObject b) {
			if ( (a.x < b.x + b.imgWidth) && (a.x + a.imgWidth > b.x) && (a.y < b.y + b.imgHeight) && (a.y + a.imgHeight > b.y) ) {
				return true;
			}
			return false;
		}

		public void update() {
			//jumping physics
			if ((this.y <= 4) && (abs(this.dy) <= 4)) {
				this.y = 0;
				this.dy = 0;
			} else {
				this.dy = this.dy + gravity;
			}

			//update position on screen
			this.x = this.x + this.dx;
			this.y = this.y + this.dy;

			//wall collision
			if ((this.x > (w - this.imgWidth)) || (this.x < 0)) {
				this.dx = -this.dx;
			}

			//ground collision
			if (this.y < 0) {
				this.dy = 0;
				this.y = 0;
			}
		}
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		shapeRenderer = new ShapeRenderer(); //used for health bar

		//generate GameObjects
		player = new Player(0, 0, 15, 0, "badlogic.jpg", 500);
		enemy = new Enemy(Gdx.graphics.getWidth()-100, 0, 25, 0,"enemy.jpg");

		//set game values
		gravity = -4;
		score = 0;
		highScore = 0;

		configureControlInterface();
	}

	private void configureControlInterface() {
		Skin skin = loadSkin();

		//used to position elements on screen
		int rowHeight = Gdx.graphics.getWidth() / 12;
		int colWidth = Gdx.graphics.getWidth() / 12;

		Button jump = new TextButton("Jump", skin,"arcade");
		jump.setSize(colWidth*2, rowHeight*2);
		jump.setPosition(5, 5);
		((TextButton) jump).getLabel().setFontScale(3);

		Button incv = new TextButton("^", skin, "arcade");
		incv.setSize(colWidth*2, rowHeight*2);
		incv.setPosition(colWidth*10, 5 + rowHeight*2);
		((TextButton) incv).getLabel().setFontScale(3);

		Button decv = new TextButton("v", skin, "arcade");
		decv.setSize(colWidth*2, rowHeight*2);
		decv.setPosition(colWidth*10, 5);
		((TextButton) decv).getLabel().setFontScale(3);

		final Label title = new Label("Velocity: " + abs(player.dx), skin,"default");
		title.setSize(Gdx.graphics.getWidth(),rowHeight*2 - title.getHeight()*2);
		title.setPosition(0,Gdx.graphics.getHeight() - rowHeight);
		title.setAlignment(Align.center);
		title.setFontScale(3);

		scoreDisplay = new Label("Score: " + this.score, skin,"default");
		scoreDisplay.setSize(Gdx.graphics.getWidth() / 2,rowHeight*2 - title.getHeight()*2);
		scoreDisplay.setPosition(20,Gdx.graphics.getHeight());
		scoreDisplay.setAlignment(Align.left);
		scoreDisplay.setFontScale(3);

		highScoreDisplay = new Label("High Score: " + this.highScore, skin,"default");
		highScoreDisplay.setSize(Gdx.graphics.getWidth() / 2,rowHeight*2 - title.getHeight()*2);
		highScoreDisplay.setPosition(Gdx.graphics.getWidth() / 2 - 100,Gdx.graphics.getHeight());
		highScoreDisplay.setAlignment(Align.right);
		highScoreDisplay.setFontScale(3);

		totalHealth = new Label("Health: " + player.health + "/500", skin,"default");
		totalHealth.setSize(Gdx.graphics.getWidth(),rowHeight*2);
		totalHealth.setPosition(0,Gdx.graphics.getHeight() - rowHeight*2);
		totalHealth.setAlignment(Align.center);
		totalHealth.setFontScale(3);

		jump.addListener(new InputListener(){
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (player.dy == 0) {
					player.dy = player.dy + 70;
				}
				return true;
			}
		});
		incv.addListener(new InputListener(){
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (player.dx >= 15 && player.dx < 35) { //moving right
					player.dx = player.dx + 5;
				}
				if (player.dx <= -15 && player.dx > -35) { //moving left
					player.dx = player.dx - 5;
				}
				title.setText("Velocity: " + abs(player.dx));
				return true;
			}
		});
		decv.addListener(new InputListener(){
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (player.dx > 15 && player.dx <= 35) { //moving right
					player.dx = player.dx - 5;
				}
				if (player.dx < -15 && player.dx >= -35) { //moving left
					player.dx = player.dx + 5;
				}
				title.setText("Velocity: " + abs(player.dx));
				return true;
			}
		});
		stage.addActor(jump);
		stage.addActor(incv);
		stage.addActor(decv);
		stage.addActor(title);
		stage.addActor(totalHealth);
		stage.addActor(scoreDisplay);
		stage.addActor(highScoreDisplay);
	}

	public void updateScore() {
		this.score = this.score + 1;
		scoreDisplay.setText("Score: " + this.score);
	}

	@Override
	public void render() {
		scoreSeconds = scoreSeconds + Gdx.graphics.getRawDeltaTime();
		if (scoreSeconds > 1) {
			scoreSeconds = scoreSeconds - 1;
			updateScore();
		}

		jumpSeconds = jumpSeconds + Gdx.graphics.getRawDeltaTime();
		if (jumpSeconds > enemy.jumpTimer) {
			jumpSeconds = jumpSeconds - enemy.jumpTimer;
			enemy.jump();
		}

		Gdx.gl.glClearColor(1, 1, 1, (float) 0.5);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		player.update();
		enemy.update();

		//health bar
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 250, h - 100, player.health, 50);
		shapeRenderer.end();

		player.render();
		enemy.render();

		//used for control interface
		stage.act();
		stage.draw();
	}

	@Override
	public void dispose () {
		batch.dispose();
		shapeRenderer.dispose();
		img.dispose();
	}

	@Override
	public void resize(int width, int height) {
		w = width;
		h = height;
	}
}
