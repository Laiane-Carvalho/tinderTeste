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

        picturePerfilUser = findViewById( R.id.picturePerfil );
        nameperfilUser =findViewById( R.id.namePerfil );
                setSupportActionBar( toolbar );
        //status = findViewById( R.id.statusPrincipalId );
        botaoSair = findViewById( R.id.botaoSairprincipalId );
        loginFacebook = new LoginFacebook( this );
        //loginEmailSenha = new LoginEmailSenha( this, status, null, null );
        cadastroActivity = new CadastroActivity();

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
