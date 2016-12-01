package mx.com.magoo.waterme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    TextView txtname, txtemail;
    Button btnLogout;
    CircleImageView imgProfile;
    final int PICK_IMAGE = 0;
    Bitmap imgBitmap = null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgProfile = (CircleImageView) findViewById(R.id.imgProfile);
        txtname = (TextView)findViewById(R.id.txtUsername);
        txtemail = (TextView)findViewById(R.id.txtEmail);
        btnLogout = (Button)findViewById(R.id.btnLogout);


        ParseUser user = ParseUser.getCurrentUser();
        String name = user.getString("name");
        String email = user.getEmail();
        if (user.getParseFile("avatar") != null) {
            ParseFile avatarFile = user.getParseFile("avatar");
            String url = avatarFile.getUrl();
            Glide.with(ProfileActivity.this).load(url).into(imgProfile);
        }


        txtname.setText(name);
        txtemail.setText(email);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                });


            }
        });
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (imageStream != null) {
                imgBitmap = BitmapFactory.decodeStream(imageStream);
                imgProfile.setImageBitmap(imgBitmap);
                ParseUser user = ParseUser.getCurrentUser();
                if (imgBitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();

                    // Create the ParseFile
                    ParseFile file = new ParseFile("profileAvatar.png", image);
                    // Upload the image into Parse Cloud
                    user.put("avatar", file);
                    progressDialog = ProgressDialog.show(ProfileActivity.this, "Cargando...",
                            "Cargando, espere por favor...", false, false);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            progressDialog.cancel();
                            progressDialog = null;
                        }
                    });
                }
            }
        }
    }
}
