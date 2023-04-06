package com.example.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class FlappyBird extends ApplicationAdapter {
	int FlapStates = 0;
	int GameState = 0;
	float velocity;
	float gravity = 2;

	float birdY;

	SpriteBatch batch;
	Texture background;
	Texture[] birds;


	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird-midflap.png");
		birds[1] = new Texture("bird-downflap.png");

		birdY = Gdx.graphics.getHeight() / 2 -birds[FlapStates].getHeight() / 2;
	}

	@Override
	public void render () {

		if(GameState != 0){

			if(Gdx.input.isTouched()){
				velocity = -30;

				if(FlapStates == 0){
					FlapStates = 1;
				}else{
					FlapStates = 0;
				}
			}

			if(birdY >0 || velocity < 0){
				velocity = velocity + gravity;
				birdY -= velocity;
			}

		}else{
			if(Gdx.input.isTouched()) {
				GameState = 1;

				if(FlapStates == 0){
					FlapStates = 1;
				}else{
					FlapStates = 0;
				}
			}
		}

		batch.begin();
		batch.draw(background,0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.draw(birds[FlapStates],Gdx.graphics.getWidth() / 2 -birds[FlapStates].getWidth() / 2 ,birdY,100 ,100);
		batch.end();
	}
}
