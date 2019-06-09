package edu.binghamton.cs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static java.lang.Math.abs;

public class Animation extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;
	float x, y;
	float dx, dy;
	float w, h;
	int imgWidth, imgHeight;
	float gravity;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
		x = 0;
		y = 0;
		dx = 15;
		dy = 25;
		gravity = -3;

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if ((y < 3) && (abs(dy) < 3)) {
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
		if ((y > (h - imgHeight)) || (y < 0)) {
			dy = -dy * (float) 0.9;
		}

		batch.begin();
		batch.draw(img, x, y);
		batch.end();
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

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		dy = dy + 50;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
