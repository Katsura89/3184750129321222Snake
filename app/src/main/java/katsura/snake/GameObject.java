package katsura.snake;

import android.graphics.Rect;

/**
 * Created by Katsura on 01.11.2015.
 */
public abstract class GameObject {
    protected MovingDirection MovingDirection;

    protected int PosX;
    protected int PosY;

    protected int MovementSpeedX;
    protected int MovementSpeedY;

    protected int Width;
    protected int Height;

    public void SetPosX(int posX) {
        this.PosX = posX;
    }

    public void SetPosY(int posY) {
        this.PosY = posY;
    }

    public int GetPosX() {
        return this.PosX;
    }

    public int GetPosY() {
        return this.PosY;
    }

    public int GetWidth() {
        return this.Width;
    }

    public int GetHeight() {
        return this.Height;
    }

    public Rect getRectangle() {
        return new Rect(PosX, PosY, PosX + Width, PosY + Height);
    }

    public void setMovingDirection(MovingDirection direction) {
        this.MovingDirection = direction;
    }

    public MovingDirection getMovingDirection() {
        return this.MovingDirection;
    }
}
