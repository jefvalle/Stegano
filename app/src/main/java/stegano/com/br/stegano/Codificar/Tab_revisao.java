package stegano.com.br.stegano.Codificar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import stegano.com.br.stegano.R;

/**
 * Created by Ratan on 7/29/2015.
 */
public class Tab_revisao extends Fragment {

    View rootview;

    String selectedImagePath, texto;
    Tab_Imagem tab_im = new Tab_Imagem();
    Tab_texto tab_tex = new Tab_texto();

    String extStorageDirectory = null;
    AlertDialog.Builder dialogConcluido;
    ProgressDialog dialogProgresso;
    String TAG = null;
    private static int RESULT_LOAD_IMAGE = 1;

    private TextView nome, senha;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.codificar_revisao,null);

        dialogConcluido = new AlertDialog.Builder(getActivity());
        dialogConcluido.setNeutralButton("OK", null);
        dialogConcluido.setTitle("Stegan");

        dialogProgresso = new ProgressDialog(getActivity());
        dialogProgresso.setCancelable(false);
        dialogProgresso.setTitle("Stegano");
        dialogProgresso.setMessage("Codificando ...");
        dialogProgresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogProgresso.setProgress(0);
        dialogProgresso.setMax(100);
        dialogProgresso.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                dialogConcluido.show();
            }
        });

        Button bt_codifica = (Button) rootview.findViewById(R.id.button_codificar);
        nome = (TextView) rootview.findViewById(R.id.editText_nome);
        senha = (TextView) rootview.findViewById(R.id.editText_senha);

        bt_codifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedImagePath = tab_im.mypath;
                texto = tab_tex.meuTexto;

                selectedImagePath = selectedImagePath+"";
                texto = texto+"";
                String pathSem = "";

                if (selectedImagePath.charAt(0)!='/')
                    Toast.makeText(getActivity(), "Nenhuma Imagem Selecionada", Toast.LENGTH_SHORT).show();
                else if (texto.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Mensagem vazia", Toast.LENGTH_SHORT).show();
                    pathSem = selectedImagePath.substring(0, selectedImagePath.lastIndexOf('/'));

                } else if (pathSem.trim().equals("/mnt/sdcard/Estegano"))
                    Toast.makeText(getActivity(),
                            "O caminho de entrada da imagem é o mesmo de saida, escolha outra imagem",
                            Toast.LENGTH_SHORT).show();
                else if (verificarSD()) {
                    dialogProgresso.show();
                    // thread para atualizar a barra de progresso
                    Thread background = new Thread(new Runnable() {
                        public void run() {
                            try {
                                //##Funcão principal/////////////
                                //texto = senha.getText().toString() + texto;
                                codifica(selectedImagePath, texto);


                                /////////////////////////////////
                                try {
                                    Thread.sleep(101);
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
                    Toast.makeText(getActivity(),
                            "Não é Possível Acessar o Cartão SD",Toast.LENGTH_SHORT).show();
            }
        });

        return rootview;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK
                && null != data) {
        }
    }

    /***************************************************************************************************************/

    public boolean verificarSD() {
        String state = Environment.getExternalStorageState();
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // SD montado, podemos ler e escrever no disco
            mExternalStorageAvailable = mExternalStorageWriteable = true;
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // SD montado como read only, s� podemos ler
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
            return false;
        } else {
            // Existe algo errado com o disco ou n�o existe dispositivo
            // Nao podemos fazer nada.
            mExternalStorageAvailable = mExternalStorageWriteable = false;
            return false;
        }
    }

    /***************************************************************************************************************************/

    public int codifica(String pathIma, String text) throws IOException {

        int altura, largura, contador = 0;
        int inicial = 0;
        char[] texto = text.toCharArray();
        Bitmap imagem = null;
        String nome_im;

        File appDirectory = Environment.getExternalStoragePublicDirectory("Stegano");

        if (nome.getText().length()>1) {
            nome_im = nome.getText().toString() + ".png";
        }else{
            nome_im = pathIma.substring(pathIma.lastIndexOf('/') + 1, pathIma.lastIndexOf('.')) + ".png";
        }

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathIma, bmOptions);
        largura = bmOptions.outWidth;
        altura = bmOptions.outHeight;

        imagem = BitmapFactory.decodeFile(pathIma);

        Bitmap dest = null;

        //dest = convertToMutable(imagem);

        dest = imagem.copy(Bitmap.Config.ARGB_8888, true);

        //dest.setHasAlpha(true);
        imagem.recycle();
        System.gc();// try to force the bytes from the imgIn to be released

        File saida = new File(appDirectory, nome_im);

        try {
            if (appDirectory.mkdirs()) {
                Log.i(TAG, "Diretorio criado! " + nome);
            } else {
                // **Length ou Size
                Log.i(TAG, "Diretorio não criado ou ja existe. " + nome);
            }

            FileOutputStream outP = new FileOutputStream(saida);

            char[] alphaBin, vermelhoBin, verdeBin, azulBin, letraBin = new char[8];
            int alphaint, vermelhoInt, verdeInt, azulInt = 0;

            int[] pixels = new int[text.length()*3];

            contador = 0;
            //Extrai pixels da imagem
            for (int i=0;i<largura-1;i++)
                for (int j=0; j<altura-1;j++){
                    if (contador < pixels.length){
                        pixels[contador] = dest.getPixel(i, j);
                        contador++;
                    }
                    else{
                        i = largura*altura;
                        j = largura*altura;
                    }

                }

            inicial = 0;
            contador = 0;
            //edita pixels extraidos

            for (int i=0; i < texto.length; i++){

                letraBin = GeraBinario((int) texto[contador]).toCharArray();

                vermelhoBin = GeraBinario(Color.red(pixels[inicial])).toCharArray();
                verdeBin = GeraBinario(Color.green(pixels[inicial])).toCharArray();
                azulBin = GeraBinario(Color.blue(pixels[inicial])).toCharArray();

                vermelhoBin[7] = letraBin[0];
                verdeBin[7] = letraBin[1];
                azulBin[7] = letraBin[2];
                vermelhoInt = Integer.parseInt(String.copyValueOf(vermelhoBin), 2);
                verdeInt = Integer.parseInt(String.copyValueOf(verdeBin), 2);
                azulInt = Integer.parseInt(String.copyValueOf(azulBin), 2);
                pixels[inicial] = Color.rgb(vermelhoInt, verdeInt, azulInt);

                vermelhoBin = GeraBinario(Color.red(pixels[inicial + 1])).toCharArray();
                verdeBin = GeraBinario(Color.green(pixels[inicial + 1])).toCharArray();
                azulBin = GeraBinario(Color.blue(pixels[inicial + 1])).toCharArray();
                vermelhoBin[7] = letraBin[3];
                verdeBin[7] = letraBin[4];
                azulBin[7] = letraBin[5];
                vermelhoInt = Integer.parseInt(String.copyValueOf(vermelhoBin), 2);
                verdeInt = Integer.parseInt(String.copyValueOf(verdeBin), 2);
                azulInt = Integer.parseInt(String.copyValueOf(azulBin), 2);
                pixels[inicial + 1] = Color.rgb(vermelhoInt, verdeInt, azulInt);

                vermelhoBin = GeraBinario(Color.red(pixels[inicial + 2])).toCharArray();
                verdeBin = GeraBinario(Color.green(pixels[inicial + 2])).toCharArray();
                azulBin = GeraBinario(Color.blue(pixels[inicial + 2])).toCharArray();
                vermelhoBin[7] = letraBin[6];
                verdeBin[7] = letraBin[7];
                azulBin[7] = azulBin[7];
                vermelhoInt = Integer.parseInt(String.copyValueOf(vermelhoBin), 2);
                verdeInt = Integer.parseInt(String.copyValueOf(verdeBin), 2);
                azulInt = Integer.parseInt(String.copyValueOf(azulBin), 2);
                pixels[inicial + 2] = Color.rgb(vermelhoInt,verdeInt, azulInt);

                contador++;
                inicial+=3;
            }

            contador = 0;
            for (int i=0;i<largura-1;i++)
                for (int j=0; j<altura-1;j++){
                    if (contador < pixels.length){
                        dest.setPixel(i, j, pixels[contador]);
                        //Log.i(TAG, "Pixel "+ contador +" :" + pixels[contador]);
                        contador++;
                    }
                    else{
                        dest.setPixel(i, j, 0);
                        i = largura*altura;
                        j = largura*altura;
                    }
                }

            // Ultimo pixel marca uma imagem codificada pelo app
            dest.setPixel(largura-1, altura-1, Color.rgb(0, 0, 0));

            dest.compress(Bitmap.CompressFormat.PNG, 100, outP);
            dest.recycle();
            System.gc();

            outP.flush();
            outP.close();

            MediaScannerConnection.scanFile(getActivity(), new String[]{saida.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String appDirectory, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + appDirectory + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });

        } catch (IOException e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            Log.w("ExternalStorage", "Error de escrita " + saida + " marcador ", e);
        }
        dialogConcluido.setMessage("Imagem codificada com Sucesso");
        dialogProgresso.dismiss();
        return 0;
    }

    /*****************************************************************************************/

    public String GeraBinario(int valor) {

        StringBuffer binario = new StringBuffer(); // guarda os dados
        for (int p = 7; p >= 0; p--) {
            binario.append((char) (((valor >> p) & 1) + 48));
        }
        return binario.toString();
    }
    /*****************************************************************************************/
    /*public long MemoriaLivre() {

        ActivityManager actMgr = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo minfo = new ActivityManager.MemoryInfo();
        actMgr.getMemoryInfo(minfo);
        Log.i( TAG, " minfo.lowMemory " + minfo.lowMemory );
        Log.i( TAG, " minfo.threshold " + minfo.threshold );
        Log.i( TAG, " minfo.availMem " + minfo.availMem );

        return minfo.availMem;
    }*/

    /*****************************************************************************************/
}