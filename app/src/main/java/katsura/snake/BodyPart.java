package katsura.snake;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Katsura on 02.11.2015.
 */
public class BodyPart extends GameObject {
    private Bitmap _bitmap;
    private boolean _enqueued = false;

    public boolean isEnqueued() {
        return _enqueued;
    }

    public int GainedPoints = 25;

    public BodyPart(Bitmap image, int maxPosX, int maxPosY) {
        _bitmap = image;

        Random random = new Random();
        this.PosX = random.nextInt(maxPosX);
        this.PosY = random.nextInt(maxPosY);

        this.Width = 75;
        this.Height = 75;
    }

    public void enqueueBodyPart(GameObject lastBodyPartOfSnake) {
        this.PosX = lastBodyPartOfSnake.PosX;
        this.PosY = lastBodyPartOfSnake.PosY;

        switch (lastBodyPartOfSnake.getMovingDirection()) {
            case Up:
                this.PosY += GameConstants.MOVEMENT_SPEED * 6;
                break;
            case Down:
                this.PosY -= GameConstants.MOVEMENT_SPEED * 6;
                break;
            case Left:
                this.PosX += GameConstants.MOVEMENT_SPEED * 6;
                break;
            case Right:
                this.PosX -= GameConstants.MOVEMENT_SPEED * 6;
                break;
        }

        this.setMovingDirection(lastBodyPartOfSnake.getMovingDirection());

        _enqueued = true;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(_bitmap, this.PosX, this.PosY, null);
    }

    public void update(GameObject lastBodyPart) {
        if(_enqueued) {
            switch (lastBodyPart.getMovingDirection()) {
                case Up:
                case Down:
                    if (this.PosX == lastBodyPart.PosX) {
                        this.setMovingDirection(lastBodyPart.getMovingDirection());
                    }
                    break;
                case Left:
                case Right:
                    if (this.PosY == lastBodyPart.PosY) {
                        this.setMovingDirection(lastBodyPart.getMovingDirection());
                    }
                    break;
            }

            updatePosition();
        }
    }

    private void updatePosition() {
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

        if(this.PosX < 0 && this.MovingDirection == katsura.snake.MovingDirection.Left) {
            this.PosX = GameConstants.DISPLAY_RESOLUTION_X;
        }
        else if(this.PosX > GameConstants.DISPLAY_RESOLUTION_X && this.MovingDirection == katsura.snake.MovingDirection.Right) {
            this.PosX = 0;
        }
        else if(this.PosY < 0 && this.MovingDirection == katsura.snake.MovingDirection.UP) {
            this.PosY = GameConstants.DISPLAY_RESOLUTION_Y;
        }
        else if(this.PosY > GameConstants.DISPLAY_RESOLUTION_Y && this.MovingDirection == katsura.snake.MovingDirection.Down) {
            this.PosY = 0;
        }
    }
}
