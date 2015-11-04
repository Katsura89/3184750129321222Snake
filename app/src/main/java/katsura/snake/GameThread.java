package katsura.snake;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Katsura on 01.11.2015.
 */
public class GameThread extends Thread {
    private int _fps = 30;
    private double _averageFps;
    private SurfaceHolder _surfaceHolder;
    private GamePanel _gamePanel;
    private boolean _running;
    private static Canvas _canvas;
    private int _playerScore = 0;

    public int GetPlayerScore() {
        return  _playerScore;
    }

    public void AddPlayerScore(int pointsToAdd) {
        _playerScore += pointsToAdd;
    }

    public GameThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();

        _surfaceHolder = surfaceHolder;
        _gamePanel = gamePanel;
    }

    @Override public void run(){
        long startTime;
        long timeMilliseconds;
        long waitTime;
        long totalTime = 0;
        long frameCount = 0;
        long targetTime = 1000 / _fps;

        while (_running){
            startTime = System.nanoTime();
            _canvas = null;

            try {
                _canvas = this._surfaceHolder.lockCanvas();

                synchronized (_surfaceHolder){
                    try {
                        _gamePanel.update();
                        _gamePanel.draw(_canvas);
                    }
                    catch (Exception e) {
                        if(e.getMessage().equals("Game Over!")) {
                            this.setRunning(false);
                            _gamePanel.GameOver(_canvas);
                        }
                    }
                }
            }
            catch (Exception e) {
            }
            finally {
                if(_canvas != null){
                    try {
                        _surfaceHolder.unlockCanvasAndPost(_canvas);
                    }
                    catch (Exception e){
                    }
                }
            }

            timeMilliseconds = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMilliseconds;

            try {
                this.sleep(waitTime);
            }
            catch (Exception e){
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if(frameCount == _fps){
                _averageFps = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
            }
        }
    }

    public void setRunning(boolean state) {
        _running = state;
    }
}
