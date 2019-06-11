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

	float x, y;
	float dx, dy;
	float w, h;
	int imgWidth, imgHeight;
	float gravity;
	ShapeRenderer shapeRenderer;
	int health;
	Label totalHealth;

	private Skin loadSkin() {
		return new Skin(Gdx.files.internal("clean-crispy-ui.json"));
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		shapeRenderer = new ShapeRenderer();

		img = new Texture("badlogic.jpg");
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
		x = 0;
		y = 0;
		dx = 15;
		dy = 0;
		gravity = -4;
		health = 500;

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

		final Label title = new Label("Velocity: " + abs(dx), skin,"default");
		title.setSize(Gdx.graphics.getWidth(),rowHeight*2 - title.getHeight()*2);
		title.setPosition(0,Gdx.graphics.getHeight() - rowHeight);
		title.setAlignment(Align.center);
		title.setFontScale(3);

		totalHealth = new Label("Health: " + health + "/500", skin,"default");
		totalHealth.setSize(Gdx.graphics.getWidth(),rowHeight*2);
		totalHealth.setPosition(0,Gdx.graphics.getHeight() - rowHeight*2);
		totalHealth.setAlignment(Align.center);
		totalHealth.setFontScale(3);

		jump.addListener(new InputListener(){
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (dy == 0) {
					dy = dy + 70;
				}
				return true;
			}
		});
		incv.addListener(new InputListener(){
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (dx >= 15 && dx < 35) { //moving right
					dx = dx + 5;
				}
				if (dx <= -15 && dx > -35) { //moving left
					dx = dx - 5;
				}
				title.setText("Velocity: " + abs(dx));
				return true;
			}
		});
		decv.addListener(new InputListener(){
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (dx > 15 && dx <= 35) { //moving right
					dx = dx - 5;
				}
				if (dx < -15 && dx >= -35) { //moving left
					dx = dx + 5;
				}
				title.setText("Velocity: " + abs(dx));
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

		if ((y <= 4) && (abs(dy) <= 4)) {
			y = 0;
			dy = 0;
		} else {
			dy = dy + gravity;
		}

		x = x + dx;
		y = y + dy;

		if ((x > (w - imgWidth)) || (x < 0)) {
			dx = -dx;
			health = health - 50; //hitting the wall reduces health
			if (health < 0) {
				health = 500; //reset health bar when health reaches zero
			}
			totalHealth.setText("Health: " + health + "/500");
		}
		if (y < 0) {
			dy = 0;
			y = 0;
		}

		//health bar
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(Gdx.graphics.getWidth() / 2 - 250, h - 100, health, 50);
		shapeRenderer.end();

		batch.begin();
		batch.draw(img, x, y);
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
