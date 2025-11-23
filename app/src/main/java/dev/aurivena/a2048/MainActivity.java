package dev.aurivena.a2048;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import dev.aurivena.a2048.domain.model.State;


public class MainActivity extends AppCompatActivity {
    GestureDetector gestureDetector;
    GameCenter gameCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newGameButton = findViewById(R.id.restartButton);
        Button undoButton = findViewById(R.id.undoButton);

        gameCenter = new GameCenter(findViewById(R.id.board), findViewById(R.id.score), findViewById(R.id.best));

        newGameButton.setOnClickListener(v -> gameCenter.startNewGame());
        undoButton.setOnClickListener(v -> gameCenter.undo());

        gameCenter.startNewGame();


        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffX = e2.getX() - e1.getX();

                float diffY = e2.getY() - e1.getY();

                if (Math.abs(diffX) > Math.abs(e2.getY() - e1.getY())) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX < 0) {
                            gameCenter.rotateField(State.LEFT);
                        } else {
                            gameCenter.rotateField(State.RIGHT);
                        }
                        return true;
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY < 0) {
                            gameCenter.rotateField(State.UP);
                        } else {
                            gameCenter.rotateField(State.DOWN);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT -> {
                gameCenter.rotateField(State.LEFT);
                yield true;
            }
            case KeyEvent.KEYCODE_DPAD_RIGHT -> {
                gameCenter.rotateField(State.RIGHT);
                yield true;
            }
            case KeyEvent.KEYCODE_DPAD_UP -> {
                gameCenter.rotateField(State.UP);
                yield true;
            }
            case KeyEvent.KEYCODE_DPAD_DOWN -> {
                gameCenter.rotateField(State.DOWN);
                yield true;
            }
            default -> super.onKeyDown(keyCode, event);
        };
    }
}

