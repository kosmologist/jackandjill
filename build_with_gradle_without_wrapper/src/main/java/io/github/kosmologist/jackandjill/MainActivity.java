package io.github.kosmologist.jackandjill;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView label = new TextView(this);
        label.setText("Hello Jack and Jill");
        setContentView(label);

    }
}
