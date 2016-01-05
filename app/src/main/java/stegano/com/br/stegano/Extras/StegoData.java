package stegano.com.br.stegano.Extras;

/**
 * Created by Jeferson on 21/12/2015.
 */
import android.net.Uri;
import java.io.File;

public class StegoData
{
    private String encodedImagePath;
    private Uri externalUri;
    private File imageFile;
    private String imagePath;
    private String message;

    public void clearData()
    {
        this.imagePath = null;
        this.encodedImagePath = null;
        this.message = null;
        this.imageFile = null;
    }

    public String getEncodedImagePath()
    {
        return this.encodedImagePath;
    }

    public Uri getExternalUri()
    {
        return this.externalUri;
    }

    public File getImageFile()
    {
        return this.imageFile;
    }

    public String getImagePath()
    {
        return this.imagePath;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setEncodedImagePath(String paramString)
    {
        this.encodedImagePath = paramString;
    }

    public void setExternalUri(Uri paramUri)
    {
        this.externalUri = paramUri;
    }

    public void setImageFile(File paramFile)
    {
        this.imageFile = paramFile;
    }

    public void setImagePath(String paramString)
    {
        this.imagePath = paramString;
    }

    public void setMessage(String paramString)
    {
        this.message = paramString;
    }
}
