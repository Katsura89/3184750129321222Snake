package katsura.snake;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.util.Log;
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

    private Context _context;

    private boolean _isGameOver;

    private long getTargetTime() {
        return 1000 / _fps;
    }

    public int GetPlayerScore() {
        return  _playerScore;
    }

    public void AddPlayerScore(int pointsToAdd) {
        _playerScore += pointsToAdd;

        if((_playerScore % 100) == 0) {
            _fps += 5;
        }
    }

    public int getPlayerHighscore() {
        SharedPreferences sharedPref =  ((Activity)_context).getPreferences(Context.MODE_PRIVATE);
        Integer highscore = sharedPref.getInt("Highscore", 0);

        return highscore;
    }

    public void setHighscore() {
        Integer highscore = getPlayerHighscore();

        if(_playerScore > highscore) {
            SharedPreferences sharedPref =  ((Activity)_context).getPreferences(((Activity)_context).MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("Highscore", _playerScore);
            editor.commit();
        }
    }

    public GameThread(SurfaceHolder surfaceHolder, GamePanel gamePanel, Context context) {
        super();

        _surfaceHolder = surfaceHolder;
        _gamePanel = gamePanel;
        _context = context;
    }

    @Override public void run(){
        startGameLoop();
    }

    public void startGameLoop() {
        _isGameOver = false;

        long startTime;
        long timeMilliseconds;
        long waitTime;
        long totalTime = 0;
        long frameCount = 0;

        while (_running){
            startTime = System.nanoTime();
            _canvas = null;

            try {
                _canvas = this._surfaceHolder.lockCanvas();

                synchronized (_surfaceHolder){
                    try {
                        if(!_isGameOver) {
                            _gamePanel.update();
                        }

                        _gamePanel.draw(_canvas);
                    }
                    catch (Exception e) {
                        if(e.getMessage().equals("Game Over!")) {
                            _isGameOver = true;
                            setHighscore();
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
            waitTime = getTargetTime() - timeMilliseconds;

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

    public boolean getGameOver() {
        return _isGameOver;
    }

    public void setGameOver(boolean state) {
        _isGameOver = false;
    }

    public void setScore(int score) {
        _playerScore = score;
    }
}
