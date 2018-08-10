package testeadapt3.cursoandroid2.com.mytinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import testeadapt3.cursoandroid2.com.mytinder.enuns.TipoLogin;


public class LoginActivity extends AppCompatActivity {

    private LoginFacebook loginFacebook;
    private TextView status;
    private LoginEmailSenha loginEmailSenha;
    private CadastroActivity cadastroActivity;
    private Button botaoLogar;
    private EditText email;
    private EditText senha;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        FacebookSdk.sdkInitialize( getApplicationContext() );
        AppEventsLogger.activateApp( this );

        status = findViewById( R.id.statusId );
        botaoLogar = findViewById( R.id.btnLogarId );
        email = findViewById( R.id.campoEmailId );
        senha = findViewById( R.id.campoSenhaId );
        auth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize( getApplicationContext() );
        loginFacebook = new LoginFacebook( LoginActivity.this );
        loginEmailSenha = new LoginEmailSenha( this, status, email, senha );
        cadastroActivity = new CadastroActivity();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && user != null) {
            Intent intent = new Intent( LoginActivity.this, FinderActivity.class );
            startActivity( intent );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // loginFacebook.onActivityResult( requestCode, resultCode, data );
        loginEmailSenha.onActivityResult( requestCode, resultCode, data );
        loginFacebook.onActivityResult( requestCode,resultCode,data );

    }

    public void botaoCadastrar(View view) {
        Intent intent = new Intent( LoginActivity.this, CadastroActivity.class );
        startActivity( intent );
        finish();
    }

    public void logarFacebook(View view) {
        loginFacebook.signInFacebook();

    }

    public void logar(View view) {
        loginEmailSenha.signIn( email.getText().toString(), senha.getText().toString() );
        //loginEmailSenha.logarEmaileSenha( email.getText().toString(),senha.getText().toString() );
    }
    public static TipoLogin checkLoginFacebookOrGoogle() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getProviders().contains( "cadastro.com" )) {
                return TipoLogin.CADASTRO;
            }
            if (user.getProviders().contains( "facebook.com" )) {
                return TipoLogin.FACEBOOK;
            }
        }
        return TipoLogin.NENHUM;
    }
}
