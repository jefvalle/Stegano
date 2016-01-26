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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import stegano.com.br.stegano.Codificar.Senha;
import stegano.com.br.stegano.R;

/**
 * Created by Jeferson on 22/12/2015.
 */
public class Tab_Image_decod extends Fragment{

    View rootView;
    private static int RESULT_LOAD_IMAGE = 1;

    //private EditText editSenha;
    private EditText editUsuario;
    String senha = "";

    ProgressDialog dialogProgresso;

    private String selectedImagePath = "";
    AlertDialog.Builder dialogConcluido;
    private ImageView img;
    private String texto ;
    private String saidaF = "";

    // caminho de saida
    String extStorageDirectory = null;

    private boolean mExternalStorageAvailable = false;
    private boolean mExternalStorageWriteable = false;
    String TAG = null;

    private Button bt_browse, bt_decod;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String uploadImagePath = "";
    private EditText editSenha;

    public static String myTexto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.decodificar_imagem, null);

        dialogProgresso = new ProgressDialog(getActivity());
        dialogProgresso.setCancelable(false);
        dialogProgresso.setTitle("Stegano");
        dialogProgresso.setMessage("Decodificando ...");
        dialogProgresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogProgresso.setProgress(0);
        dialogProgresso.setMax(100);
        dialogProgresso.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                //exibirAlertDialog(senha);


                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.senha, null);
                builder.setView(dialogView);

                builder.setTitle("Login");

                editSenha = (EditText)dialogView.findViewById(R.id.textSenha);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(editSenha.getText().toString().equals(senha)){
                            Toast.makeText(getActivity(), "Senha correta",
                                    Toast.LENGTH_SHORT).show();
                            myTexto = saidaF;
                        }
                        else {
                            Toast.makeText(getActivity(), "Senha incorreta",
                                    Toast.LENGTH_SHORT).show();
                            myTexto = "";

                        }
                    }
                });
                // Configura o botão de Cancelar
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Fecha o AlertDialog
                        //dismiss();
                    }
                });
                builder.show();
            }
        });

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

                    dialogProgresso.show();

                    Thread background = new Thread(new Runnable() {
                        public void run() {
                            try {

                                ///////////////////////////////////
                                decodifica(uploadImagePath);
                                /////////////////////////////////

                                try {
                                    Thread.sleep(1);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                // if something fails do something smart
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
                    background.start();

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
                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, getActivity());
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                image.setImageBitmap(bm);
                uploadImagePath = tempPath;
        }
    }


    public int decodifica(String pathIma) throws IOException {

        String saida = "";
        String teste = "";
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
            dialogProgresso.dismiss();
            return 0;
        }

        //Procura pela mar�a��o na imagem
        for (int i=0;i<largura-1;i++){
            for (int j=0; j<altura-1;j++){
                if (imagem.getPixel(i,j) == Color.rgb(0, 0, 0)){
                    //Log.i(TAG, "Achou o zero: " + imagem.getPixel(i,j));
                    i = largura*altura;
                    j = largura*altura;
                }
                else{
                    inicial++;

                }
            }
        }

        int[] pixels = new int[inicial+5];

        inicial = 0;

        for (int i=0;i<largura-1;i++){
            for (int j=0; j<altura-1;j++){
                if (imagem.getPixel(i,j) == Color.rgb(0, 0, 0)){
                    i = largura*altura;
                    j = largura*altura;
                }
                else{
                    pixels[inicial] = imagem.getPixel(i, j);
                    inicial++;
                }

            }
        }
        imagem.recycle();

        for (inicial = 0; inicial < pixels.length-4;) {

            saida = saida + RetiraCaracteres(pixels[inicial], pixels[inicial + 1], pixels[inicial + 2]) + "";

            inicial = inicial + 3;
        }

        senha = saida.substring(saida.lastIndexOf("#$")+2,saida.lastIndexOf("$#"));

        saidaF = saida.substring(saida.lastIndexOf("$#")+2,saida.length()-1);

        //myTexto = saida.substring(saida.lastIndexOf("$#")+2,saida.length()-1);

        dialogProgresso.dismiss();

        return 0;
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

    private void exibirAlertDialog(String sen){
        Senha login = new Senha();
        login.senha = sen;
        login.show(getFragmentManager(), "login");
    }
}
