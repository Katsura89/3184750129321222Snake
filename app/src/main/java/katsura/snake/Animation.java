package katsura.snake;

import android.graphics.Bitmap;

public class Animation {
    private Bitmap[] _frames;
    private int _currentFrame;
    private long _startTime;
    private long _delay;
    private boolean _playedOnce;

    public Animation() {
    }

    public void setFrames(Bitmap[] frames)
    {
        _frames = frames;
        _currentFrame = 0;
        _startTime = System.nanoTime();
    }

    public void setDelay(long delay) {
        _delay = delay;
    }

    public void setFrame(int currentFrame) {
        _currentFrame = currentFrame;
    }

    public void update() {
        long elapsed = (System.nanoTime() - _startTime) / 1000000;

        if(elapsed > _delay) {
            _currentFrame++;
            _startTime = System.nanoTime();
        }
        if(_currentFrame == _frames.length) {
            _currentFrame = 0;
            _playedOnce = true;
        }
    }

    public Bitmap getImage() {
        return _frames[_currentFrame];
    }

    public int getFrame() {
        return _currentFrame;
    }

    public boolean playedOnce() {
        return _playedOnce;
    }
}