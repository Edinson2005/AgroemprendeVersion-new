package com.edinson.agroemnew;

import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class Notificaciones extends AppCompatActivity {

    private MaterialCardView card;
    private LinearLayout expandableContent;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        card = findViewById(R.id.card);
        expandableContent = findViewById(R.id.expandableContent);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpanded = !isExpanded;
                expandableContent.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            }
        });
    }
}
