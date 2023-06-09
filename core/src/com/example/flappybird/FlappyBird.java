package com.example.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.renderers.BillboardRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	Circle birdCircle;
	BitmapFont font;
	BitmapFont font1;
	int FlapStates = 0;
	int GameState = 0;
	float velocity;
	float gravity = 2;
	float gap = 1250;
	float MaxTubeOfSet;

	float birdY;

	float distanceBetweenTubes;
	int numOfTubes = 4;
	int score = 0;
	int scoringTube = 0;
	float tubeVelocity = 4;
	float[] tubeOfSet = new float[numOfTubes];
	float[] tubeX = new float[numOfTubes];

	Random randomGenerator;

	SpriteBatch batch;
	Texture background;
	Texture TopTube;
	Texture BottomTube;
	Texture GameOver;
	Texture[] birds;
	Rectangle[] topRectangle;
	Rectangle[] bottomRectangle;



	@Override
	public void create () {
		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		font1 = new BitmapFont();
		font1.setColor(Color.WHITE);
		font1.getData().setScale(10);
		batch = new SpriteBatch();

		background = new Texture("background.png");
		TopTube = new Texture("pipe-down.png");
		BottomTube = new Texture("pipe-up.png");
		GameOver = new Texture("GameOver.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird-midflap.png");
		birds[1] = new Texture("bird-downflap.png");

		distanceBetweenTubes = Gdx.graphics.getWidth() *3/4;
		MaxTubeOfSet = Gdx.graphics.getHeight()/2 - gap/2 - 150;
		randomGenerator = new Random();
		topRectangle = new Rectangle[numOfTubes];
		bottomRectangle = new Rectangle[numOfTubes];

		startGame();
	}

	public void startGame(){
		birdY = Gdx.graphics.getHeight() / 2 -birds[FlapStates].getHeight() / 2;
		for(int i=0;i<numOfTubes;i++){
			tubeOfSet[i] = randomGenerator.nextInt(1200);
			tubeX[i] = Gdx.graphics.getWidth()/2 -TopTube.getWidth()/2 + Gdx.graphics.getWidth() + i*distanceBetweenTubes;

			topRectangle[i] = new Rectangle();
			bottomRectangle[i] = new Rectangle();
			font.setColor(Color.WHITE);
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background,0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(GameState == 1){

			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2){
				score++;

				if (scoringTube < numOfTubes - 1){
					scoringTube++;
				}else {
					scoringTube = 0;
				}
			}

			if(Gdx.input.isTouched()){
				velocity = -25;

				if(FlapStates == 0){
					FlapStates = 1;
				}else{
					FlapStates = 0;
				}
			}

			for(int i=0;i<numOfTubes;i++) {

				if(tubeX[i] < -200){
					tubeOfSet[i] = randomGenerator.nextInt(1200);
					tubeX[i] += numOfTubes*distanceBetweenTubes;
				}else {
					tubeX[i] = tubeX[i] - tubeVelocity;

				}
				topRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 - tubeOfSet[i], 200, 1800);
				bottomRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - BottomTube.getHeight() * 3 - tubeOfSet[i], 200, 1800);

				batch.draw(TopTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 - tubeOfSet[i], 200, 1800);
				batch.draw(BottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - BottomTube.getHeight() * 3 - tubeOfSet[i], 200, 1800);

			}
			if(birdY >0){
				velocity = velocity + gravity;
				birdY -= velocity;
			}else{
				GameState = 2;
			}

		}else if (GameState == 0){
			if(Gdx.input.isTouched()) {
				GameState = 1;

				if(FlapStates == 0){
					FlapStates = 1;
				}else{
					FlapStates = 0;
				}
			}
		}else if (GameState == 2){
			batch.draw(GameOver , Gdx.graphics.getWidth() / 2 -710 , Gdx.graphics.getHeight() / 2 - GameOver.getHeight() / 2 , 1400 , 550);
			font1.draw(batch , "Score : " + score , Gdx.graphics.getWidth() / 2 - 300, Gdx.graphics.getHeight() / 2 - GameOver.getHeight() / 2);
			font.setColor(Color.CLEAR);
			birdY = 0;
			if(Gdx.input.isTouched()) {
				GameState = 1;

				if(FlapStates == 0){
					FlapStates = 1;
				}else{
					FlapStates = 0;
				}
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
			}

		}


		batch.draw(birds[FlapStates],Gdx.graphics.getWidth() / 2 -birds[FlapStates].getWidth() / 2 ,birdY,100 ,100);
		font.draw(batch , String.valueOf(score) , 100 , 200);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth() /2 +25,birdY +  50, 50);

		for(int i=0;i<numOfTubes;i++) {

			if(Intersector.overlaps(birdCircle , topRectangle[i]) || Intersector.overlaps(birdCircle , bottomRectangle[i])){
				GameState = 2;
			}
		}

	}
}
