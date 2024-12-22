//package com.example.test3;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.util.ArrayList;
//import android.content.Intent; // <-- Add this line
//public class MainActivity extends AppCompatActivity {
//    private ArrayAdapter<String> adapter;
//    private ArrayList<String> tokenStrings;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.home_screen);
//
//        Button button1 = findViewById(R.id.search_button);
//        Button button2 = findViewById(R.id.add_button);
//        button1.setOnClickListener(new View.OnClickListener() {  // Set up click listener for Button 1 (NewActivity1)
//            @Override
//            public void onClick(View v) {
//                // Create an intent to start NewActivity1
//                Intent intent = new Intent(MainActivity.this, search_word.class); // Intent(context, class) - context is the current activity and class is the activity to start
//                startActivity(intent);
//            }
//        });
//
//        button2.setOnClickListener(new View.OnClickListener() {  // Set up click listener for Button 1 (NewActivity1)
//            @Override
//            public void onClick(View v) {
//                // Create an intent to start NewActivity1
//                Intent intent = new Intent(MainActivity.this, add_link.class); // Intent(context, class) - context is the current activity and class is the activity to start
//                startActivity(intent);
//            }
//        });
//
//    }
//}
package com.example.test3

/**
 * This is the main activity of the application.
 * It sets the content of the activity to the HomeScreen composable.
 */

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.test3.theme.AppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                //HomeScreen()
                HomeScreenNav()
            }
            //HomeScreen()
            //HomeAppBar()
        }
    }
}