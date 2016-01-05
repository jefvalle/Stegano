package stegano.com.br.stegano.Decodificar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import stegano.com.br.stegano.R;

/**
 * Created by Jeferson on 22/12/2015.
 */
public class Tab_texto_decod extends Fragment{

    private TextView txtRoot;
    private String  txtNomeArq = "Imagem_1";
    private TextView txtSalvar;
    private TextView txtLer;
    public Spinner SpnListarArquivos;
    private ArrayList<String> Arquivos = new ArrayList<String>();

    TextView texto;
    View rootview;
    Tab_Image_decod tab_im = new Tab_Image_decod();

    public static String meuTexto;

    public String mensagem = "";

    private Button bt_copiar, bt_salvar;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootview = inflater.inflate(R.layout.decodificar_texto, container, false);
        texto = (TextView) rootview.findViewById(R.id.edit_text_dec);

        bt_copiar = (Button) rootview.findViewById(R.id.copyButton);
        bt_salvar = (Button) rootview.findViewById(R.id.saveButton);



        bt_copiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB && (texto.getText().length() > 1)) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(mensagem);
                    Toast.makeText(getActivity(), "Texto copiado com sucesso", Toast.LENGTH_SHORT).show();
                } else if (texto.getText().length() > 1) {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", mensagem);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), "Texto copiado com sucesso", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity(), "Vazio", Toast.LENGTH_SHORT).show();

            }
        });

        bt_salvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(texto.getText().length() > 1)
                    click_Salvar();
                else
                    Toast.makeText(getActivity(), "Vazio", Toast.LENGTH_SHORT).show();
            }
        });

        setMenuVisibility(true);

        return rootview;

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        texto.setText(mensagem);
        setMenuVisibility(true);
        setMenuVisibility(false);
    }

    public void setText (String m){
        texto.setText(mensagem);
    }
        @Override
        public void setMenuVisibility(final boolean visible) {
            super.setMenuVisibility(visible);
            if (visible) {
                texto.setText(tab_im.myTexto);
            }
        }

    private String ObterDiretorio()
    {
        File root = android.os.Environment.getExternalStorageDirectory();
        return root.toString();
    }

    public void click_Salvar()
    {
        String lstrNomeArq;
        File arq;
        byte[] dados;
        try
        {
            lstrNomeArq = txtNomeArq;

            arq = new File(Environment.getExternalStorageDirectory(), lstrNomeArq);
            FileOutputStream fos;

            dados = mensagem.getBytes();

            fos = new FileOutputStream(arq);
            fos.write(dados);
            fos.flush();
            fos.close();
            Toast.makeText(getActivity(), "Mensagem salva com sucesso", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), "Erro : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}