package edu.binghamton.cs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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

	private Skin loadSkin() {
		return new Skin(Gdx.files.internal("clean-crispy-ui.json"));
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		img = new Texture("badlogic.jpg");
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
		x = 0;
		y = 0;
		dx = 15;
		dy = 0;
		gravity = -4;

		renderJumpButton();
	}

	private void renderJumpButton() {
		Button jump = new Button(loadSkin(),"default");
		jump.setSize(100, 100);
		jump.setPosition(5, 5);
		jump.addListener(new InputListener(){
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
			}
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (dy == 0) {
					dy = dy + 70;
				}
				return true;
			}
		});
		stage.addActor(jump);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
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
		}
		if (y < 0) {
			dy = 0;
			y = 0;
		}

		batch.begin();
		batch.draw(img, x, y);
		batch.end();
		stage.act();
		stage.draw();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	@Override
	public void resize(int width, int height) {
		w = width;
		h = height;
	}
}
