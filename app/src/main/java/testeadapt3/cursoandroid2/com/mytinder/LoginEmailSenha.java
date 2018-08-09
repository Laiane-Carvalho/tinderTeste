package testeadapt3.cursoandroid2.com.mytinder;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by laianeoliveira on 18/06/18.
 */

public class LoginEmailSenha {
    private static final String TAGG = "signIn";
    private Activity activity;
    private TextView status;
    private EditText campoEmail;
    private EditText campoSenha;

    private FirebaseAuth auth;
    private CallbackManager callbackManager;

    public LoginEmailSenha(Activity activity, TextView status, EditText campoEmail, EditText campoSenha) {
        this.activity = activity;
        this.status = status;
        this.campoEmail = campoEmail;
        this.campoSenha = campoSenha;
        auth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
    }

    protected void signIn(String email, String senha) {
        Log.i( TAGG, "logar conta" );
        if (!validaDados()) {
            return;
        }
        auth.signInWithEmailAndPassword( email, senha )
                .addOnCompleteListener( activity, new OnCompleteListener <AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task <AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.i( TAGG, "sucesso no resultado de logar" );
                            toastMessage( "sucesso ao logar com conta cadastrada" );
                            irPrincipal();

                        } else {
                            String errorException = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                errorException = "Essa conta de email ñ existe ou foi desativada!";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                errorException = "Senha inváida, digite novamente!";
                            } catch (Exception e) {
                                errorException = "ao efetuar login, tente novamente, ou cadastre-se!";
                                e.printStackTrace();
                            }

                            Log.i( TAGG, "Erro" + errorException + task.getException() );
                            toastMessage( "Error " + errorException );
                        }
                    }
                } );
    }

    private void irPrincipal() {
        FirebaseUser user = auth.getCurrentUser();
        atualizar( user );
        Intent intent = new Intent();
        intent.putExtra( "Logar", true );
        activity.setResult( 11, intent );
        activity.finish();
    }

    private boolean validaDados() {
        boolean valid = true;

        String email = campoEmail.getText().toString();
        if (TextUtils.isEmpty( email )) {
            campoEmail.setError( "Requerid..." );
        } else {
            campoEmail.setError( null );
        }

        String senha = campoSenha.getText().toString();
        if (TextUtils.isEmpty( senha )) {
            campoSenha.setError( "Requerid..." );
        } else {
            campoSenha.setError( null );
        }

        return valid;
    }

    protected void signOut() {
        auth.signOut();
        atualizar( null );
    }

    private void atualizar(FirebaseUser user) {
        toastMessage( "login: " + user );
    }

    private void toastMessage(String message) {
        Toast.makeText( activity, message, Toast.LENGTH_LONG ).show();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
