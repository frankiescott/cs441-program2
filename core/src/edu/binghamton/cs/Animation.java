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
	private Stage stage;

	float w, h;
	int imgWidth, imgHeight;
	float gravity;
	ShapeRenderer shapeRenderer;
	Label totalHealth;
	GameObject player;
	GameObject enemy;
	private Skin loadSkin() {
		return new Skin(Gdx.files.internal("clean-crispy-ui.json"));
	}

	class GameObject {
		float x, y;
		float dx, dy;
		int health;
		Texture img;
		int imgWidth, imgHeight;

		GameObject(int x, int y, int dx, int dy, int health, String img) {
			this.x = (float) x;
			this.y = (float) y;
			this.dx = (float) dx;
			this.dy = (float) dy;
			this.health = health;
			this.img = new Texture(img);
			this.imgHeight = this.img.getHeight();
			this.imgWidth = this.img.getWidth();
		}
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		shapeRenderer = new ShapeRenderer();

		player = new GameObject(0, 0, 15, 0, 500, "badlogic.jpg");
		gravity = -4;

		configureControlInterface();
	}

	private void configureControlInterface() {
		Skin skin = loadSkin();
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
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, (float) 0.5);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if ((player.y <= 4) && (abs(player.dy) <= 4)) {
			player.y = 0;
			player.dy = 0;
		} else {
			player.dy = player.dy + gravity;
		}

		player.x = player.x + player.dx;
		player.y = player.y + player.dy;

		if ((player.x > (w - player.imgWidth)) || (player.x < 0)) {
			player.dx = -player.dx;
			player.health = player.health - 50; //hitting the wall reduces health
			if (player.health < 0) {
				player.health = 500; //reset health bar when health reaches zero
			}
			totalHealth.setText("Health: " + player.health + "/500");
		}
		if (player.y < 0) {
			player.dy = 0;
			player.y = 0;
		}

		//health bar
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 250, h - 100, player.health, 50);
		shapeRenderer.end();

		batch.begin();
		batch.draw(player.img, player.x, player.y);
		batch.end();

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
