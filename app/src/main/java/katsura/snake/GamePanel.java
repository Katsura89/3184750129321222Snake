package katsura.snake;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Katsura on 01.11.2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static int _displayWidth = 856;
    public static int _displayHeight = 480;

    private Context _context;
    private GameThread _gameThread;

    private Player _player;
    private BodyPart _nextBodyPart;

    private List<BodyPart> _bodyParts;

    public  GamePanel(Context context) {
        super(context);

        _context = context;
        _gameThread = new GameThread(getHolder(), this);

        _bodyParts = new ArrayList<BodyPart>();

        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
    }

    @Override public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry){
            try {
                _gameThread.setRunning(false);
                _gameThread.join();
                retry = false;
            }
            catch (Exception e) {
            }
        }
        _gameThread.stop();
    }

    @Override public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //_player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.player));
        _player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.coin2));

        WindowManager wm = (WindowManager)_context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        _displayWidth = size.x;
        _displayHeight = size.y;

        GameConstants.DISPLAY_RESOLUTION_X = _displayWidth;
        GameConstants.DISPLAY_RESOLUTION_Y = _displayHeight;

        _gameThread.setRunning(true);
        _gameThread.start();
    }

    long _lastKeyPressTime = System.nanoTime();
    boolean _lockMovement = false;

    @Override public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getActionMasked();

        long timeSeconds = (System.nanoTime() - _lastKeyPressTime) / 1000000;
        if (timeSeconds > 250) {
            _lockMovement = false;
        }

        if (!_lockMovement) {
            _lockMovement = true;
            _lastKeyPressTime = System.nanoTime();

            if (action == MotionEvent.ACTION_DOWN) {
                float touchPosX = motionEvent.getX();
                float touchPosY = motionEvent.getY();

                if (touchPosY < (_displayHeight * 0.25)) {
                    if (_bodyParts.size() > 0 && _bodyParts.get(0).getMovingDirection() != MovingDirection.Down)
                        _player.setMovingDirection(MovingDirection.Up);
                    if (_bodyParts.size() == 0)
                        _player.setMovingDirection(MovingDirection.Up);
                } else if (touchPosY > (_displayHeight - (_displayHeight * 0.25))) {
                    if (_bodyParts.size() > 0 && _bodyParts.get(0).getMovingDirection() != MovingDirection.Up)
                        _player.setMovingDirection(MovingDirection.Down);
                    if (_bodyParts.size() == 0)
                        _player.setMovingDirection(MovingDirection.Down);
                } else if (touchPosX > (_displayWidth - (_displayWidth * 0.25))) {
                    if (_bodyParts.size() > 0 && _bodyParts.get(0).getMovingDirection() != MovingDirection.Left)
                        _player.setMovingDirection(MovingDirection.Right);
                    if (_bodyParts.size() == 0)
                        _player.setMovingDirection(MovingDirection.Right);
                } else if (touchPosX < (_displayWidth * 0.25)) {
                    if (_bodyParts.size() > 0 && _bodyParts.get(0).getMovingDirection() != MovingDirection.Right)
                        _player.setMovingDirection(MovingDirection.Left);
                    if (_bodyParts.size() == 0)
                        _player.setMovingDirection(MovingDirection.Left);
                } else {
                    //_player.setMovingDirection(MovingDirection.None);
                }

                MovingDirection after = _player.getMovingDirection();
            }
        }

        return super.onTouchEvent(motionEvent);
    }

    public void update() throws Exception {
        try {
            _player.update();
        }
        catch (Exception e) {
            throw e;
        }

        if(_nextBodyPart == null){
            _nextBodyPart = new BodyPart(BitmapFactory.decodeResource(getResources(), R.drawable.coin), _displayWidth - 75, _displayHeight - 75);
        }

        GameObject lastUpdated = _player;

        for (int i = 0; i < _bodyParts.size(); i++) {
            _bodyParts.get(i).update(lastUpdated);
            lastUpdated = _bodyParts.get(i);

            if(i > 2) {
                if (_bodyParts.get(i).isEnqueued() && _player.getRectangle().intersect(lastUpdated.getRectangle())) {
                    throw new Exception("Game Over!");
                }
            }
        }

        if(_player.getRectangle().intersect(_nextBodyPart.getRectangle())) {
            _gameThread.AddPlayerScore(_nextBodyPart.GainedPoints);

            if(_bodyParts.size() == 0)
                _nextBodyPart.enqueueBodyPart(_player);
            else
                _nextBodyPart.enqueueBodyPart(_bodyParts.get(_bodyParts.size() - 1));

            _bodyParts.add(_nextBodyPart);
            _nextBodyPart = null;
        }
    }

    @Override public void draw(Canvas canvas) {
        super.draw(canvas);
        _player.draw(canvas);
        _nextBodyPart.draw(canvas);

        for (int i = 0; i < _bodyParts.size(); i++)
            _bodyParts.get(i).draw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(75);
        canvas.drawText("Score: " + _gameThread.GetPlayerScore(), 40, 80, paint);

        //final float scaleFactorX = getWidth() / (float)WIDTH;
        //final float scaleFactorY = getHeight() / (float)HEIGHT;

        //if(canvas != null) {
            //final int savedState = canvas.save();
            //canvas.scale(scaleFactorX, scaleFactorY);
            //canvas.restoreToCount(savedState);
        //}
    }

    public void GameOver(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(75);
        canvas.drawText("Game Over!", 750, 500, paint);
    }
}
