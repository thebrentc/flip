package uk.ac.reading.sis05kol.mooc;

//Other parts of the android libraries that we use
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class TheGame extends GameThread{
	
	static int games = 0;
	
	myUtils utils = new myUtils(); // use myUtils.java

	//Will store the image(s) of a coin
	private Bitmap[] mCoin;
	private int coin = 0;
	
	//Will store state and animation
	private String state; // waiting, flipping	
	static int UP = 1;
	static int DOWN = 2;
	int direction = 0;	
	private int rotation = 0;
	static int[] TURNS = {45,90,135,180,225,270,315,360}; // where coin changes 	
	
	//The X and Y position of the coin on the screen (middle of coin)
	private float mCoinX = 0;
	private float mCoinY = 0;
	
	//The speed (pixel/second) of the coin 
	private float mCoinSpeed = 0;
	
	//Coin will 'spin' (360 degrees?) for set number of spins
	//With randomisation by randomly offsetting by 1 or 0 spins
	//Also randomisation by setting starting coin image?
	private int spin = 0, spins = 0;

	//This is run before anything else, so we can prepare things here
	public TheGame(GameView gameView) {
		//House keeping
		super(gameView);
		
		//Prepare the image(s) so we can draw it on the screen (using a canvas)
		mCoin = new Bitmap[8]; 
		mCoin[0] = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
				R.drawable.coin_white);		
		mCoin[1] = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
				R.drawable.coin_white_quarter);
		mCoin[2] = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
				R.drawable.coin_white_sideon);
		mCoin[3] = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
				R.drawable.coin_black_quarter);
		mCoin[4] = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
				R.drawable.coin_black);
		mCoin[5] = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
				R.drawable.coin_black_quarter);		
		mCoin[6] = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
				R.drawable.coin_black_sideon);
		mCoin[7] = BitmapFactory.decodeResource
				(gameView.getContext().getResources(), 
				R.drawable.coin_white_quarter);		
	}
	
	//This is run before a new game (also after an old game)
	@Override
	public void setupBeginning() {
		// Initialise state
		state = "waiting";
		rotation = 0;
		// randomise starting coin
		if (utils.booleanRandom()) coin = 0; else coin = 4; 
		//Initialise speeds
		//mCoinSpeed = 1;
		mCoinSpeed = mCoin[0].getHeight()/16;
		
		
		//Guestimate number of spins
		spins = (int) ((mCanvasHeight / mCoinSpeed)/4) - 1;// ?
		//then offset by 0 or 1 randomly //?
		if (utils.booleanRandom()) spins--;
		//setScore(spins);
		spin = 0;
		
		//Place the coin.
		//mCoin.Width() and mCoin.getHeight() gives us the height and width of the image of the coin
		//use mCoin[0] as reference image here and elsewhere
		mCoinX = mCanvasWidth / 2;
		mCoinY = mCanvasHeight - mCoin[0].getHeight()/2;
		
	}

	@Override
	protected void doDraw(Canvas canvas) {
		//If there isn't a canvas to draw on do nothing
		//It is ok not understanding what is happening here
		if(canvas == null) return;
		
		super.doDraw(canvas);
		
		//draw the current image of the coin using the X and Y of the coin
		//drawBitmap uses top left corner as reference, we use middle of picture
		//null means that we will use the image without any extra features (called Paint)
		canvas.drawBitmap(mCoin[coin], mCoinX - mCoin[coin].getWidth() / 2, mCoinY - mCoin[coin].getHeight() / 2, null);
	}
		
	//This is run whenever the phone is touched by the user	
	@Override
	protected void actionOnTouch(float x, float y) {
		//Toggle waiting to flipping
		//Restrict to tap on coin only?
		if (state == "waiting") {
			setupBeginning();
			games++;
			setScore(games);
			state = "flipping";
			direction = UP;
		}
		else {
			state = "waiting";
			setupBeginning();	
		}
			
	}
	
	
	//This is run whenever the phone moves around its axises 
	@Override
	protected void actionWhenPhoneMoved(float xDirection, float yDirection, float zDirection) {

	}
		
	//This is run just before the game "scenario" is printed on the screen
	@Override
	protected void updateGame(float secondsElapsed) {
		if (state == "flipping") {
			// move coin
			if (direction == UP) {				
				mCoinY = mCoinY - mCoinSpeed;
				//if (mCoinY < 0 + mCoin[0].getHeight()/2 + 24) {
				if (spin >= spins/2) {
					direction = DOWN;	
				}				
			}
			else {				
				mCoinY = mCoinY + mCoinSpeed;
				//if (mCoinY > mCanvasHeight - mCoin[0].getHeight()/2 - 24) {
				if (spin >= spins-2 && rotation == 360) /* finished! ? */ {
					//direction = UP;
					state = "waiting";
				}
			}
			
			// rotate coin
			rotation=rotation+45;			
			if (utils.in_array(rotation, TURNS))
			{
				if (coin < 7 ) coin++; else coin = 0; 				
			}
			if (rotation > 360 ) {
				rotation = 0;
				spin++;
				//setScore(spin);
			}
		}
	}
}

// This file is part of the course "Begin Programming: Build your first mobile game" from futurelearn.com
// Copyright: University of Reading and Karsten Lundqvist
// Modified by thebrentc.net November 2013
// It is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// It is is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// 
// You should have received a copy of the GNU General Public License
// along with it.  If not, see <http://www.gnu.org/licenses/>.