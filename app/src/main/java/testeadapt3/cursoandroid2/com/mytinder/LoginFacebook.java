package testeadapt3.cursoandroid2.com.mytinder;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by laianeoliveira on 18/06/18.
 */

public class LoginFacebook {

    private static final String TAG = "loginFacebook";
    private LoginButton loginButton;
    private Activity activity;
    private CallbackManager callbackManager;
    private FirebaseAuth auth;

    public LoginFacebook(Activity activity) {
        this.activity = activity;
        auth = FirebaseAuth.getInstance();
        loginButton = activity.findViewById( R.id.login_buttonFacebook );
    }

    protected void signInFacebook() {

        loginButton.setReadPermissions( "public_profile, email, user_friends" );
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback( callbackManager, new FacebookCallback <LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i( TAG, "suecsso no resultado do facebook" );
                toast( "Sucesso ao logar no facebook" );
                facebookAccessToken( loginResult.getAccessToken() );
            }

            @Override
            public void onCancel() {
                Log.i( TAG, " login facebook cancelado" );
                toast( "cancelado logar no facebook" );

            }

            @Override
            public void onError(FacebookException error) {
                toast( "Erro ao logar no facebook" );

            }
        } );
    }

    private void facebookAccessToken(AccessToken token) {
        Log.d( TAG, "handleFacebookAccessToken:" + token );
        AuthCredential credential = FacebookAuthProvider.getCredential( token.getToken() );
        auth.signInWithCredential( credential )
                .addOnCompleteListener( activity, new OnCompleteListener <AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task <AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d( TAG, "signInWithCredential:success" );
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText( activity, "sucesso ao logar no facebook", Toast.LENGTH_LONG ).show();
                            irParaPrincipal();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w( TAG, "signInWithCredential:failure", task.getException() );
                            Toast.makeText( activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT ).show();
                        }
                    }
                } );
    }

    private void irParaPrincipal() {
        Intent intent = new Intent( activity, PaginaPrinActivity.class );
        activity.startActivity( intent );
        activity.finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult( requestCode, resultCode, data );

    }

    private void toast(String s) {
        Toast.makeText( activity, s, Toast.LENGTH_LONG ).show();
    }
    public void sairFacebook(){
        auth.signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity( intent );
        activity.finish();
        Toast.makeText( activity,"Deslogando do facebook",Toast.LENGTH_LONG ).show();
    }

}
