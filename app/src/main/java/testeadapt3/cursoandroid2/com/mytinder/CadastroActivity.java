package testeadapt3.cursoandroid2.com.mytinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private static final String TAG = "cadastro";
    private EditText campoNome;
    private EditText campoEmail;
    private EditText campoSenha;
    private Button btnCadastrar;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_cadatro );

        campoNome = findViewById( R.id.cadastrarNomeId );
        campoEmail = findViewById( R.id.cadastrarEmailIdD );
        campoSenha = findViewById( R.id.cadastrarSenhaId );
        btnCadastrar = findViewById( R.id.botaConcluiCadastro );
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btnCadastrar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccounte( campoEmail.getText().toString(), campoSenha.getText().toString() );
                toastMessage( "vc clicou para constrir cadastro" );
                btnCadastrar.setEnabled( true );
            }
        } );
    }

    public void onStart() {
        super.onStart();
       // atualizar( user );
    }

    private void createAccounte(String email, String senha) {
        Log.i( TAG, "criar conta " + email );
        if (!validaDados()) {
            return;
        }
        auth.createUserWithEmailAndPassword( email, senha ).
                addOnCompleteListener( this, new OnCompleteListener <AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task <AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i( TAG, "usuario cadastrado com sucesso" );
                            toastMessage( "sucesso no cadastro" );
                            irParaPrincipal();
                            FirebaseUser user = auth.getCurrentUser();
                            toastMessage( "sucesso ao criar" );
                           // atualizar( user );

                        } else {
                            String errorException = "";

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                errorException = "Senha com relevancia fraca, por favor digite mais caracteres.";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                errorException = "Email inválido, por favor verifique e digite novamente...";
                            } catch (FirebaseAuthUserCollisionException e) {
                                errorException = "Cadastro já realizado ou invalidado...";
                            } catch (Exception e) {
                                errorException = " ao efetuar Cadastro, Por favor tente outra vez!";
                                e.printStackTrace();
                            }
                            Log.i( TAG, "Erro" + errorException + task.getException() );
                            toastMessage( "Error " + errorException );

                        }
                    }
                } );
    }

    private void irParaPrincipal() {
        Intent intent = new Intent( CadastroActivity.this, PaginaPrinActivity.class );
        startActivity( intent );
        finish();
    }

    private boolean validaDados() {
        boolean valid = true;

        String email = campoEmail.getText().toString();
        if (TextUtils.isEmpty( email )) {
            campoEmail.setError( "Requerid..." );
            valid = false;
        } else {
            campoEmail.setError( null );
        }

        String senha = campoSenha.getText().toString();
        if (TextUtils.isEmpty( senha )) {
            campoSenha.setError( "Requerid..." );
            valid = false;
        } else {
            campoSenha.setError( null );
        }

        String nome = campoNome.getText().toString();
        if (TextUtils.isEmpty( nome )) {
            campoNome.setError( "Requerido..." );
            valid = false;
        } else {
            campoNome.setError( null );

        }
        return valid;
    }

    public void atualizar(FirebaseUser user) {
        this.user = user;
//        toastMessage( "resultado:  " + user.getEmail() );
    }

    public void toastMessage(String message) {
        Toast.makeText( this, message, Toast.LENGTH_LONG ).show();
    }
}
