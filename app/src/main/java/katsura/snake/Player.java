package katsura.snake;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Katsura on 01.11.2015.
 */
public class Player extends GameObject {
    private Animation _animation;

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

        for (int i = 0; i < sprites.length; i++) {
            sprites[i] = Bitmap.createBitmap(image, i * this.Width, 0, this.Width, this.Height);
        }

        _animation.setFrames(sprites);
        _animation.setDelay(500);

        _startTime = System.nanoTime();
    }

    public void update() throws Exception {
        _animation.update();

        if(this.getMovingDirection() != null) {
            switch (this.getMovingDirection()) {
                case Up:
                    this.PosY -= GameConstants.MOVEMENT_SPEED;
                    break;
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
            throw new Exception("Game Over!");
        }
        else if(this.PosX > GameConstants.DISPLAY_RESOLUTION_X && this.MovingDirection == katsura.snake.MovingDirection.Right) {
            throw new Exception("Game Over!");
        }
        else if(this.PosY < 0 && this.MovingDirection == katsura.snake.MovingDirection.UP) {
            throw new Exception("Game Over!");
        }
        else if(this.PosY > GameConstants.DISPLAY_RESOLUTION_Y && this.MovingDirection == katsura.snake.MovingDirection.Down) {
            throw new Exception("Game Over!");
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(_animation.getImage(), this.PosX, this.PosY, null);
    }

}
