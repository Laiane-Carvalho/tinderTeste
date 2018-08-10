package testeadapt3.cursoandroid2.com.mytinder;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.SupportActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import testeadapt3.cursoandroid2.com.mytinder.enuns.TipoLogin;

public class FinderActivity extends SupportActivity {

    //binds layout
    @BindView( R.id.imagem1Cima )
    ImageView imagemCima;

    @BindView( R.id.imagem2Baixo )
    ImageView imagemBaixo;

    @BindView( R.id.textViewNomes )
    TextView texto;

    @BindView( R.id.iconeID )
    ImageView icone;

    @BindView( R.id.meuLayoutId )
    RelativeLayout meuLaypout;

    //picture user perfil facebook
    @BindView( R.id.picturePerfilUser )
    private ImageView picturePerfilUser;

    @BindView( R.id.namePerfilUser )
    private TextView namePerfilUser;

    String name;
    String email;
    String id;
    String caminhoImagem;
    AccessToken accessToken;

    LoginFacebook loginFacebook;
    int contador;
    boolean podeSwipe = true;

    //Banco dados
    FirebaseAuth auth;
    FirebaseDatabase databaseRef;
    ValueEventListener friendsEventListner;

    // array para colher dados dos ususarios logados...
    ArrayList <String> listaNomes;

    int[] listaImagem = {R.drawable.flor, R.drawable.flor1, R.drawable.flor2, R.drawable.flor3, R.drawable.flor4, R.drawable.flor5
            , R.drawable.flor6, R.drawable.flor7};

    @Override
    int layoutID() {
        return R.layout.activity_finder;
    }

    @Override
    void inicializar(Bundle savedInstanceState) {
        setContentView( R.layout.activity_finder );
        android.support.v7.widget.Toolbar toolbar = findViewById( R.id.includeToolbar );

        verificarCadastro();
        listaNomes = new ArrayList <>();
        auth = FirebaseAuth.getInstance();
        loginFacebook = new LoginFacebook( this );
        databaseRef = FirebaseDatabase.getInstance();

        //  texto.setText( listaNomes.get( 0 ) );
        imagemBaixo.setImageResource( R.drawable.flor1 );
        imagemCima.setImageResource( R.drawable.flor );
        imagemBaixo.setAlpha( 0.0f );
        imagemBaixo.setRotation( -30.0f );
        imagemBaixo.setScaleY( 0.35f );
        imagemBaixo.setScaleX( 0.35f );

        meuLaypout.setOnTouchListener( new OnSwipeTouchListener( FinderActivity.this ) {
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

        } );
    }

    @Override
    protected void onStart() {
        super.onStart();

        typesLogIn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  return super.onCreateOptionsMenu( menu );
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_toolbar_principal, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_out_aplication) {
            desconectar();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    private void animation(Boolean estado) {
        float direcao = 1000.0f;
        float giro = 30.0f;
        icone.setImageResource( R.drawable.favourite_accept );

        if (estado) {
            direcao = -1000.0f;
            giro = -30.0f;
            icone.setImageResource( R.drawable.exc );
        }

        if (podeSwipe) {
            contador++;
            podeSwipe = false;
            if (contador == listaNomes.size()) {
                contador = 0;
            } else {
                texto.setText( listaNomes.get( contador ) );
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

    public void desconectar() {
        loginFacebook.sairFacebook();
    }

    public void typesLogIn() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            if (LoginActivity.checkLoginFacebookOrGoogle().equals( TipoLogin.FACEBOOK )) {
                verificFacebook();
            }
            if (LoginActivity.checkLoginFacebookOrGoogle().equals( TipoLogin.CADASTRO )) {
                verificarCadastro();
            }
        }
    }

    private void verificarCadastro() {

        ///estudar
    }

    private void verificFacebook() {
        accessToken = AccessToken.getCurrentAccessToken();
        FirebaseUser user = auth.getCurrentUser();
        if (accessToken == null && user != null) {
            finish();
        }
        GraphRequest request = GraphRequest.newMeRequest( accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    name = object.getString( "name" );
                    email = object.getString( "email" );
                    id = (String) object.get( "id" );
                    namePerfilUser.setText( name );
                    caminhoImagem = "https://graph.facebook.com/" + object.getString( "id" ) + "/picture?type=large";
                    Picasso.get().load( caminhoImagem ).into( picturePerfilUser );

                    // gravar no database
                    DatabaseReference myReference = databaseRef.getReference();
                    Map <String, Object> maps = new HashMap <>();
                    maps.put( "id", id );
                    maps.put( "nome", name );
                    maps.put( "email", email );
                    maps.put( "imagUrl", caminhoImagem );
                    myReference.child( id ).setValue( maps );
                    myReference.addListenerForSingleValueEvent( friendsEventListner );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } );
        Bundle parametrs = new Bundle();
        parametrs.putString( "fields", "id,name,email" );
        request.setParameters( parametrs );
        request.executeAsync();

        friendsEventListner = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    listaNomes.add( data.child( "id" ).getValue( String.class ) );
                }
                Log.i( "euLog", "amigos:" + String.valueOf( listaNomes ) );
                texto.setText( listaNomes.get( 0 ) );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
    }
}
