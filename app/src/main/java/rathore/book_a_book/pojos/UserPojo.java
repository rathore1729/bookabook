package rathore.book_a_book.pojos;

import android.graphics.Color;

/**
 * Created by Rathore on 06-Jul-17.
 */

public class UserPojo {
    private String NAME;
    private String EMAIL;
    private String PASSWORD;
    private String PHONE;
    private String ADDRESS;
    private int BACKCOLOR;
    private String VERIFICATION;
    private String SEC_QUES;
    private String ANSWER;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public int getBACKCOLOR() {
        return BACKCOLOR;
    }

    public void setBACKCOLOR() {
        int[] colors = {Color.rgb(50,0,250),Color.rgb(159,0,250),Color.rgb(85,143,250),Color.rgb(0,82,0),Color.rgb(0,155,0),Color.rgb(255,0,128),Color.rgb(250,0,0),Color.rgb(240,80,50),Color.rgb(240,80,100),Color.rgb(0,110,240),Color.rgb(240,110,60),Color.rgb(127,70,0),Color.rgb(63,127,127),Color.rgb(205,105,0),Color.rgb(220,143,120),Color.rgb(127,127,0),};
        int seq = (int) (Math.random()*(colors.length-1));
        this.BACKCOLOR = colors[seq];
    }

    public String isVERIFICATION() {
        return VERIFICATION;
    }

    public void setVERIFICATION(String VERIFICATION) {
        this.VERIFICATION = VERIFICATION;
    }

    public String getSEC_QUES() {
        return SEC_QUES;
    }

    public void setSEC_QUES(String SEC_QUES) {
        this.SEC_QUES = SEC_QUES;
    }

    public String getANSWER() {
        return ANSWER;
    }

    public void setANSWER(String ANSWER) {
        this.ANSWER = ANSWER;
    }
}
