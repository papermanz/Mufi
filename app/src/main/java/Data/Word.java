package Data;

import java.io.Serializable;

public class Word implements Serializable {
    private int IdWord;
    private String Eng;
    private String Vie;

    public Word(int idWord, String eng, String vie) {
        IdWord = idWord;
        Eng = eng;
        Vie = vie;
    }

    public int getIdWord() {
        return IdWord;
    }

    public void setIdWord(int idWord) {
        IdWord = idWord;
    }

    public String getEng() {
        return Eng;
    }

    public void setEng(String eng) {
        Eng = eng;
    }

    public String getVie() {
        return Vie;
    }

    public void setVie(String vie) {
        Vie = vie;
    }

}
