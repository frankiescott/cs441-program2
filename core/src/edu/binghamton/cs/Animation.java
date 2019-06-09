package edu.binghamton.cs;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Animation extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	float x, y;
	float dx, dy;
	float w, h;
	int imgWidth, imgHeight;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
		x = 0;
		y = 0;
		dx = 5;
		dy = 15;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		x = x + dx;
		y = y + dy;
		if ((x > (w - imgWidth)) || (x < 0)) {
			dx = -dx;
		}
		if ((y > (h - imgHeight)) || (y < 0)) {
			dy = -dy;
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
}
