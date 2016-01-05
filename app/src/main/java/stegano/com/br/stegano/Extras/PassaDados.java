package stegano.com.br.stegano.Extras;

import android.app.Application;

/**
 * Created by Jeferson on 21/12/2015.
 */
public class PassaDados extends Application {

    private String txtValue;

    public String getTxtValue(){
        return txtValue;
    }
    public void setTxtValue(String aString){
        txtValue= aString;
    }
}
