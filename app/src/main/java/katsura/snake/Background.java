package katsura.snake;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Katsura on 01.11.2015.
 */
public class Background {
    private Bitmap _image;

    private int _posX;
    private int _posY;

    private int _speed;

    public Background(Bitmap image, int width, int height) {
        _image = Bitmap.createScaledBitmap(image, width, height, false);
    }

    public void update() {
        _posX += _speed;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(_image, _posX, _posY, null);
    }

    public void setVector(int speed) {
        _speed = speed;
    }
}
