package testeadapt3.cursoandroid2.com.mytinder;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FinderActivity extends AppCompatActivity {

    ImageView imagemCima;
    ImageView imagemBaixo;
    TextView texto;
    RelativeLayout meuLaypout;
    ImageView icone;


    int contador;
    boolean podeSwipe = true;

    String[] listaNomes = {"Flor", "Flor1", "Flor2", "Flor3", "Flor4", "Flor5", "Flor6", "Flor7"};
    int[] listaImagem = {R.drawable.flor, R.drawable.flor1, R.drawable.flor2, R.drawable.flor3, R.drawable.flor4, R.drawable.flor5
            , R.drawable.flor6, R.drawable.flor7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_finder );

        imagemCima = (ImageView) findViewById( R.id.imagem1Cima );
        imagemBaixo = (ImageView) findViewById( R.id.imagem2Baixo );
        texto = (TextView) findViewById( R.id.textViewNomes );
        meuLaypout = (RelativeLayout) findViewById( R.id.meuLayoutId );
        icone = (ImageView)findViewById( R.id.iconeID );

        texto.setText( listaNomes[0] );
        imagemBaixo.setImageResource( R.drawable.flor1 );
        imagemCima.setImageResource( R.drawable.flor );
        imagemBaixo.setAlpha( 0.0f );
        imagemBaixo.setRotation( -30.0f );
        imagemBaixo.setScaleY( 0.35f );
        imagemBaixo.setScaleX( 0.35f );

//        meuLaypout.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText( getApplicationContext(), "Voce clicou", Toast.LENGTH_LONG ).show();
//                animation(Math.random()>0.5);
//            }
//        } );

        meuLaypout.setOnTouchListener(new OnSwipeTouchListener(FinderActivity.this) {
            public void onSwipeTop() {
                //Toast.makeText(FinderActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                //Toast.makeText(FinderActivity.this, "right", Toast.LENGTH_SHORT).show();
                animation( false );
            }
            public void onSwipeLeft() {
                //Toast.makeText(FinderActivity.this, "left", Toast.LENGTH_SHORT).show();
                animation( true );
            }
            public void onSwipeBottom() {
                //Toast.makeText(FinderActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });
    }


    private void animation(Boolean estado ) {

        float direcao = 1000.0f;
        float giro = 30.0f;
        icone.setImageResource( R.drawable.favourite_accept );


        if (estado){
            direcao = -1000.0f;
            giro = -30.0f;
            icone.setImageResource( R.drawable.exc );
        }



        if (podeSwipe) {
            contador++;
            podeSwipe = false;
            if (contador == listaNomes.length) {
                contador = 0;
            } else {
                texto.setText( listaNomes[contador] );
            }

            imagemCima.animate()
                    .translationXBy( direcao )
                    .rotationBy( giro )
                    .setDuration( 500 );

            imagemBaixo.animate()
                    .rotationBy( 30.0f )
                    .alphaBy( 1.0f )
                    .scaleX( 1.0f )
                    .scaleY( 1.0f )
                    .setDuration( 700 );

            new CountDownTimer( 750, 1000 ) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.i( "LogX Second left", String.valueOf( millisUntilFinished / 1000 ) );

                }

                @Override
                public void onFinish() {
                    Log.i( "LogX Done", "CountDow timer finish" );
                    trocarTexturas();
                    podeSwipe = true;

                }
            }.start();
        }
    }


    private void trocarTexturas() {

        Log.i( "LogX Done", new String( String.valueOf( contador ) ) );
        if (contador == listaImagem.length - 1) {
            imagemBaixo.setImageResource( listaImagem[0] );
        } else {
            imagemBaixo.setImageResource( listaImagem[contador + 1] );
        }
        imagemCima.setImageResource( listaImagem[contador] );
        reposicionarImagens();

    }

    private void reposicionarImagens() {
        imagemCima.animate()
                .translationX( 0 )
                .rotation( 0 )
                .setDuration( 0 );

        imagemBaixo.animate()
                .rotation( -30 )
                .alpha( 0 )
                .scaleX( 0.35f )
                .scaleY( 0.35f )
                .setDuration( 0 );

    }
}
