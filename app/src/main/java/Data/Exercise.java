package Data;

import java.io.Serializable;

public class Exercise implements Serializable {
    private int IdImage;
    private String Title;
    private byte[] ImageTitle;
    private String Gif;

    public Exercise(int idImage, String title, byte[] imageTitle, String gif) {
        IdImage = idImage;
        Title = title;
        ImageTitle = imageTitle;
        Gif = gif;
    }

    public int getIdImage() {
        return IdImage;
    }

    public void setIdImage(int idImage) {
        IdImage = idImage;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public byte[] getImageTitle() {
        return ImageTitle;
    }

    public void setImageTitle(byte[] imageTitle) {
        ImageTitle = imageTitle;
    }

    public String getGif() {
        return Gif;
    }

    public void setGif(String gif) {
        Gif = gif;
    }
}