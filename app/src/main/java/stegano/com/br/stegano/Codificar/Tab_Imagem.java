package stegano.com.br.stegano.Codificar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

import stegano.com.br.stegano.R;

/**
 * Created by Jeferson on 7/29/2015.
 */
public class Tab_Imagem extends Fragment {


    View rootView;
    private static int RESULT_LOAD_IMAGE = 1;
    String extStorageDirectory = null;
    AlertDialog.Builder dialogConcluido;
    ProgressDialog dialogProgresso;
    String TAG = null;

    private String selectedImagePath = "";
    private EditText texto;

    private Button bt_browse;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String uploadImagePath = "";

    public static String mypath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.codificar_image, null);

        bt_browse = (Button) rootView.findViewById(R.id.botao_galeria_codificar);

        bt_browse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                selectImage();
            }
        });

        return rootView;
    }

    private void selectImage() {
        final CharSequence[] items = { getString(R.string.tirar), getString(R.string.galeria),
                getString(R.string.cancelar)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.selecionar));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
               if (items[item].equals(getString(R.string.tirar))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals(getString(R.string.galeria))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, getString(R.string.selecionar_f)), SELECT_FILE);
                } else if (items[item].equals(getString(R.string.cancelar))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        ImageView image = (ImageView) rootView.findViewById(R.id.imagem_codificar);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);

                    //bm = Bitmap.createScaledBitmap(bm, 70, 70, true);

                    image.setImageBitmap(bm);
                    uploadImagePath = f.getAbsolutePath();
                    mypath = uploadImagePath;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, getActivity());
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                image.setImageBitmap(bm);
                uploadImagePath = tempPath;
                mypath = uploadImagePath;
            }
        }
    }

    @SuppressWarnings("deprecation")
    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public String getpah (){

        String path = uploadImagePath;

        return path;
    }

    public void alteraImage (String tempPath){
        ImageView image = (ImageView) getView().findViewById(R.id.imagem_codificar);
        Bitmap bm;
        BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
        bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
        image.setImageBitmap(bm);
    }
}
