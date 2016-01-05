package stegano.com.br.stegano.Decodificar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import stegano.com.br.stegano.R;

/**
 * Created by Jeferson on 22/12/2015.
 */
public class Tab_Image_decod extends Fragment{

    View rootView;
    private static int RESULT_LOAD_IMAGE = 1;
    ProgressDialog dialogProgresso;

    private String selectedImagePath = "";
    AlertDialog.Builder dialogConcluido;
    private ImageView img;
    private String texto ;
    private String saida = "";

    // caminho de saida
    String extStorageDirectory = null;

    private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWriteable = false;
    String TAG = null;

    private Button bt_browse, bt_decod;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String uploadImagePath = "";

    public static String myTexto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.decodificar_imagem, null);

        dialogConcluido = new AlertDialog.Builder(getActivity());
        dialogConcluido.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        dialogConcluido.setTitle("Stegano");

        img = (ImageView) rootView.findViewById(R.id.decodificar_imagem);

        bt_browse = (Button) rootView.findViewById(R.id.botao_galeria_decod);
        bt_decod = (Button) rootView.findViewById(R.id.button_decodificar);

        bt_browse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                selectImage();
            }
        });

        bt_decod.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                verificarSD();
                if (uploadImagePath.isEmpty())
                    Toast.makeText(getActivity(), "Nenhuma Imagem Selecionada", Toast.LENGTH_SHORT).show();
                else if (mExternalStorageAvailable) {
                    try {

                        ////////////////////////////////////
                        saida = decodifica(uploadImagePath);
                        ////////////////////////////////////
                        myTexto = saida;

                        Toast.makeText(getActivity(), "Sucesso", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(getActivity(), "Não é Possível Acessar o Cartão SD", Toast.LENGTH_SHORT).show();

            }
        });

        return rootView;
    }

    private void selectImage() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        ImageView image = (ImageView) rootView.findViewById(R.id.decodificar_imagem);

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
            }
        }
    }


    public String decodifica(String pathIma) throws IOException {

        String saida = "";
        int inicial = 0;
        int largura = 0;
        int altura = 0;
        Bitmap imagem = null;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathIma, bmOptions);
        largura = bmOptions.outWidth;
        altura = bmOptions.outHeight;

        imagem = BitmapFactory.decodeFile(pathIma);


        largura = imagem.getWidth();
        altura = imagem.getHeight();
        int ultimo = imagem.getPixel(largura-1, altura-1);

        if (ultimo != Color.rgb(0, 0, 0)) {
            dialogConcluido.setMessage("Imagem não possui mensagem");
            dialogConcluido.show();
            return "";
        }

        //Procura pela mar�a��o na imagem
        for (int i=0;i<largura-1;i++){
            for (int j=0; j<altura-1;j++){
                if (imagem.getPixel(i,j) == 0){
                    Log.i(TAG, "Achou o zero: " + imagem.getPixel(i, j));
                    i = largura*altura;
                    j = largura*altura;
                }
                else{
                    inicial++;
                    //Log.i(TAG, "TamVetor: " + inicial + "Pixel: "+ imagem.getPixel(i, j));
                }

            }
        }

        int[] pixels = new int[inicial+5];

        inicial = 0;

        for (int i=0;i<largura-1;i++){
            for (int j=0; j<altura-1;j++){
                if (imagem.getPixel(i,j) == 0){
                    Log.i(TAG, "Achou o zero: " + imagem.getPixel(i, j));
                    i = largura*altura;
                    j = largura*altura;
                }
                else{
                    pixels[inicial] = imagem.getPixel(i, j);
                    inicial++;
                   // Log.i(TAG, "TamVetor: " + inicial + "Pixel: "+ imagem.getPixel(i, j));
                }

            }
        }
        imagem.recycle();


        inicial = 0;
        saida = saida + RetiraCaracteres(pixels[inicial], pixels[inicial+1], pixels[inicial+2]);
        inicial = 3;
        for (int j = 4; j < pixels.length-3; j+=4) {
            if (pixels[inicial]!=0 && pixels[inicial-1]!=0) {
                saida = saida + RetiraCaracteres(pixels[inicial], pixels[inicial+1], pixels[inicial+2]);
                inicial = inicial+3;
            }
            else
                j = largura*altura;
        }

        return saida.substring(0,saida.length()-1);
        //return saida;
    }

    /*****************************************************************************************/

    public char RetiraCaracteres(int pixel, int pixel_2, int pixel_3){

        char[] vermelhoBin = GeraBinario(Color.red(pixel)).toCharArray();
        char[] verdeBin = GeraBinario(Color.green(pixel)).toCharArray();
        char[] azulBin =  GeraBinario(Color.blue(pixel)).toCharArray();

        String letra = ""+vermelhoBin[7]+verdeBin[7]+azulBin[7];

        vermelhoBin = GeraBinario(Color.red(pixel_2)).toCharArray();
        verdeBin = GeraBinario(Color.green(pixel_2)).toCharArray();
        azulBin =  GeraBinario(Color.blue(pixel_2)).toCharArray();

        letra = letra+vermelhoBin[7]+verdeBin[7]+azulBin[7];

        vermelhoBin = GeraBinario(Color.red(pixel_3)).toCharArray();
        verdeBin = GeraBinario(Color.green(pixel_3)).toCharArray();

        letra = letra+vermelhoBin[7]+verdeBin[7];

        return  (char) Integer.parseInt(letra, 2);
    }
    /*****************************************************************************************/

    public String GeraBinario(int valor){

        StringBuffer binario = new StringBuffer(); // guarda os dados
        for (int p = 7; p >= 0; p--)
        {
            binario.append((char) (((valor >> p) & 1) + 48));
        }
        return binario.toString();
    }

    /*****************************************************************************************/

    public void verificarSD() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // SD can read and write to disk
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // SD read only
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Is there something wrong with the disk or device does not exist
            // we can not do anything.
            mExternalStorageAvailable = mExternalStorageWriteable = false;
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

    @Override
    public void onStop() {
        super.onPause();
        Log.d("Ciclo", "Fragment: Metodo onPause() chamado");
        myTexto = "";

    }
}
