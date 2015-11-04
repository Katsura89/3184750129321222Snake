package katsura.snake;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Katsura on 01.11.2015.
 */
public class Player extends GameObject {
    private Animation _animation;

    private Bitmap _imageFacingUp;
    private Bitmap _imageFacingLeft;
    private Bitmap _imageFacingRight;
    private Bitmap _imageFacingDown;

    private long _startTime;

    public Player(Bitmap image) {
        _animation = new Animation();

        this.PosX = 750;
        this.PosY = 500;
        //this.Width = 150;
        //this.Height = 150;
        this.Width = 75;
        this.Height = 75;

        int startTime;

        Bitmap[] sprites = new Bitmap[1];

        _imageFacingUp = Bitmap.createBitmap(image, 0 * this.Width, 0, this.Width, this.Height);
        _imageFacingLeft = Bitmap.createBitmap(image, 1 * this.Width, 0, this.Width, this.Height);
        _imageFacingRight = Bitmap.createBitmap(image, 2 * this.Width, 0, this.Width, this.Height);
        _imageFacingDown = Bitmap.createBitmap(image, 3 * this.Width, 0, this.Width, this.Height);

        _animation.setFrames(sprites);
        _animation.setDelay(500);

        _startTime = System.nanoTime();
    }

    public void update() throws Exception {
        if(this.getMovingDirection() != null) {
            switch (this.getMovingDirection()) {
                case Up:
                    this.PosY -= GameConstants.MOVEMENT_SPEED;
                    throw new Exception("Game Over!");
                    //break;
                case Down:
                    this.PosY += GameConstants.MOVEMENT_SPEED;
                    break;
                case Left:
                    this.PosX -= GameConstants.MOVEMENT_SPEED;
                    break;
                case Right:
                    this.PosX += GameConstants.MOVEMENT_SPEED;
                    break;
            }
        }

        if(this.PosX < 0 && this.MovingDirection == katsura.snake.MovingDirection.Left) {
            this.PosX = GameConstants.DISPLAY_RESOLUTION_X;
        }
        else if(this.PosX > GameConstants.DISPLAY_RESOLUTION_X && this.MovingDirection == katsura.snake.MovingDirection.Right) {
            this.PosX = 0;
        }
        else if(this.PosY < 0 && this.MovingDirection == katsura.snake.MovingDirection.Up) {
            this.PosY = GameConstants.DISPLAY_RESOLUTION_Y;
        }
        else if(this.PosY > GameConstants.DISPLAY_RESOLUTION_Y && this.MovingDirection == katsura.snake.MovingDirection.Down) {
            this.PosY = 0;
        }
    }

    public void draw(Canvas canvas) {
        switch (this.getMovingDirection()) {
            case Up:
                canvas.drawBitmap(_imageFacingUp, this.PosX, this.PosY, null);
                break;
            case Down:
                canvas.drawBitmap(_imageFacingDown, this.PosX, this.PosY, null);
                break;
            case Left:
                canvas.drawBitmap(_imageFacingLeft, this.PosX, this.PosY, null);
                break;
            case Right:
                canvas.drawBitmap(_imageFacingRight, this.PosX, this.PosY, null);
                break;
        }
    }
}
