package com.example.paint;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private PaintView paintView;

    private final ActivityResultLauncher<Intent> openImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        try {
                            paintView.setBackgroundImage(MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                        } catch (IOException e) {
                            Toast.makeText(this, "이미지를 열 수 없습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    private final ActivityResultLauncher<String> saveImageLauncher = registerForActivityResult(
            new ActivityResultContracts.CreateDocument("image/png"),
            uri -> {
                if (uri != null) {
                    try (OutputStream out = getContentResolver().openOutputStream(uri)) {
                        paintView.getBitmap().compress(android.graphics.Bitmap.CompressFormat.PNG, 100, out);
                        Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(this, "저장 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        paintView = findViewById(R.id.paintView);

        // 색상 버튼
        Button redBtn = findViewById(R.id.btnRed);
        Button blueBtn = findViewById(R.id.btnBlue);
        Button blackBtn = findViewById(R.id.btnBlack);

        redBtn.setOnClickListener(v -> paintView.setColor(Color.RED));
        blueBtn.setOnClickListener(v -> paintView.setColor(Color.BLUE));
        blackBtn.setOnClickListener(v -> paintView.setColor(Color.BLACK));

        // 굵기 조절
        SeekBar strokeSeekBar = findViewById(R.id.seekBar);
        strokeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                paintView.setStrokeWidth(progress);
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 지우기
        Button clearBtn = findViewById(R.id.btnClear);
        clearBtn.setOnClickListener(v -> paintView.clearCanvas());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_open) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            openImageLauncher.launch(intent);
            return true;
        } else if (item.getItemId() == R.id.action_save) {
            saveImageLauncher.launch("drawing.png");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
