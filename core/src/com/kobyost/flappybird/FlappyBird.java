package com.kobyost.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture[] birds;
    Texture topTube;
    Texture bottomTube;
    Texture gameOverImg;
    Circle circle;
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;
    BitmapFont font;
    //ShapeRenderer shapeRenderer;
    int flapState = 0;
    float birdY = 0;
    float velocity = 0;
    int gameState = 0;
    float gravity = 2;
    float gap = 400;
    float maxTubeOffset;
    Random randomNumberGen;
    float tubVelocity = 4;
    int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];
    float distBetweenTubes;
    int score = 0;
    int tubeCounter = 0;
    boolean secondPress=false;

private  void startGame(){
    birdY = (Gdx.graphics.getHeight() / 2) - (birds[0].getHeight() / 2);
    for (int i = 0; i < numberOfTubes; i++) {
        tubeOffset[i] = (randomNumberGen.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
        tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distBetweenTubes;
        topTubeRectangles[i] = new Rectangle();
        bottomTubeRectangles[i] = new Rectangle();
    }
}
    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        gameOverImg = new Texture("gameover1.png");
        circle = new Circle();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];
        //shapeRenderer=new ShapeRenderer();

        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomNumberGen = new Random();
        distBetweenTubes = Gdx.graphics.getWidth() / 2;
        startGame();


    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {

            if (tubeX[tubeCounter] < Gdx.graphics.getWidth() / 2) {
                score++;
                Gdx.app.log("Score", score + "");
                if (tubeCounter < numberOfTubes - 1)
                    tubeCounter++;
                else {
                    tubeCounter = 0;
                }
            }


            if (Gdx.input.justTouched()) {
                velocity = -30;

            }
            for (int i = 0; i < numberOfTubes; i++) {
                if (tubeX[i] < -topTube.getWidth()) {
                    tubeX[i] += numberOfTubes * distBetweenTubes;
                    tubeOffset[i] = (randomNumberGen.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                } else {
                    tubeX[i] = tubeX[i] - tubVelocity;

                }

                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
                topTubeRectangles[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());


            }

                if (birdY > 0 ) {
                    velocity += gravity;
                    birdY -= velocity;
                } else {
                    gameState = 2;
                }


        } else if (gameState == 0) {

            if (Gdx.input.justTouched()) {
                gameState = 1;

            }
        } else if (gameState == 2) {
            birdY=0;
            batch.draw(gameOverImg, Gdx.graphics.getWidth() / 2 - gameOverImg.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOverImg.getHeight() / 2);
            if (Gdx.input.justTouched()) {

                gameState = 0;
                score=0;
                tubeCounter=0;
                velocity=0;
                startGame();
                secondPress=true;
            }
        }


        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }

        batch.draw(birds[flapState], (Gdx.graphics.getWidth() / 2) - (birds[flapState].getWidth() / 2), birdY);
        font.draw(batch, String.valueOf(score), 100, 200);
        batch.end();
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.BLUE);
        circle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);

        //shapeRenderer.circle(circle.x,circle.y,circle.radius);
        for (int i = 0; i < numberOfTubes; i++) {
            //shapeRenderer.rect(topTubeRectangles[i].x,topTubeRectangles[i].y,topTubeRectangles[i].getWidth(),topTubeRectangles[i].getHeight());
            //shapeRenderer.rect(bottomTubeRectangles[i].x,bottomTubeRectangles[i].y,bottomTubeRectangles[i].getWidth(),bottomTubeRectangles[i].getHeight());
            //collision detection
            if (Intersector.overlaps(circle, topTubeRectangles[i]) || Intersector.overlaps(circle, bottomTubeRectangles[i])) {
                gameState = 2;//gameOver
                Gdx.app.log("Detection", "Collision!");
            }
        }
        //shapeRenderer.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
    }
}
