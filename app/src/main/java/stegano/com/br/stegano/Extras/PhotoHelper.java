package stegano.com.br.stegano.Extras;

/**
 * Created by Jeferson on 21/12/2015.
 */
import android.app.Activity;

import java.io.File;

public class PhotoHelper
{
    private Activity activity;
    private File imageFile;

    public PhotoHelper(Activity paramActivity)
    {
        this.activity = paramActivity;
    }

    public File getImageFile()
    {
        return this.imageFile;
    }

    public void resizeBitmap(String paramString, int paramInt)
    {
        /*paramString = BitmapFactory.decodeFile(paramString);
        int i = paramString.getWidth();
        int j = paramString.getHeight();
        float f1 = paramInt / i;
        float f2 = j * paramInt / i / j;
        Object localObject = new Matrix();
        ((Matrix)localObject).postScale(f1, f2);
        paramString = Bitmap.createBitmap(paramString, 0, 0, paramString.getWidth(), paramString.getHeight(), (Matrix)localObject, true);
        try
        {
            localObject = new FileOutputStream(this.imageFile);
            paramString.compress(Bitmap.CompressFormat.JPEG, 100, (OutputStream)localObject);
            ((FileOutputStream)localObject).flush();
            ((FileOutputStream)localObject).close();
            return;
        }
        catch (Exception paramString)
        {
            paramString.printStackTrace();
        }*/
    }

    public static enum PhotoSource
    {
        CAMERA,  GALLERY,  DECODE;
    }
}
