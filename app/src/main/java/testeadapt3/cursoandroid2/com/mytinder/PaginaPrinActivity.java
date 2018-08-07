package testeadapt3.cursoandroid2.com.mytinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class PaginaPrinActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private LoginFacebook loginFacebook;
    private LoginEmailSenha loginEmailSenha;
    private CadastroActivity cadastroActivity;
    private TextView status;
    private Button botaoSair;

    //for perfil facebook
    private AccessToken accessToken;
    private ImageView picturePerfilUser;
    private TextView nameperfilUser;
    String name;
    String email;
    String id;
    String caminhoImagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_principal );
        toolbar = findViewById( R.id.tollbarPrincipalId );
        picturePerfilUser = findViewById( R.id.picturePerfil );
        nameperfilUser =findViewById( R.id.namePerfil );
                setSupportActionBar( toolbar );
        //status = findViewById( R.id.statusPrincipalId );
        botaoSair = findViewById( R.id.botaoSairprincipalId );
        loginFacebook = new LoginFacebook( this );
        //loginEmailSenha = new LoginEmailSenha( this, status, null, null );
        cadastroActivity = new CadastroActivity();

    }

    @Override
    protected void onStart() {
        super.onStart();

        accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            finish();
        }
        GraphRequest request = GraphRequest.newMeRequest( accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    name = object.getString( "name" );
                    email = object.getString( "email" );
                    id = (String) object.get( "id" );
                    nameperfilUser.setText( name );
                    caminhoImagem = "https://graph.facebook.com/" + object.getString( "id" ) + "/picture?type=large";
                    Picasso.get().load( caminhoImagem ).into( picturePerfilUser );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } );
        Bundle parametrs = new Bundle();
        parametrs.putString( "fields", "id,name,email" );
        request.setParameters( parametrs );
        request.executeAsync();
    }

    public void desconectar(View view) {
         loginFacebook.sairFacebook();
        // loginEmailSenha.signOut();
      //  retornarMain();
    }

    private void retornarMain() {
        Intent intent = new Intent( this, LoginActivity.class );
        startActivity( intent );
        finish();
    }
}
