package testeadapt3.cursoandroid2.com.mytinder;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    LinearLayout meuLayout;
    Intent virarPagina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );


        meuLayout = findViewById( R.id.meuLayoutMainActivity );

        meuLayout.setOnTouchListener( new OnSwipeTouchListener( MainActivity.this ) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {

            }

            public void onSwipeLeft() {
                // gestos(true);
                virarPagina = new Intent( getApplicationContext(), LoginActivity.class );
                startActivityForResult( virarPagina, 11 );
            }

            public void onSwipeBottom() {
            }
        } );
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11 && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null && bundle.getBoolean( "Logar" )) {
                Intent intent = new Intent( getApplicationContext(), teste1Activity.class );
                // limpar a pilha e adicionar a proxima activity.
               // intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity( intent );
                //finish();
                //finishAffinity();
            }
        }
    }
}
