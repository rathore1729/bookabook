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
    private String DP;
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

    public String getDP() {
        return DP;
    }

    public void setDP(String DP) {
        this.DP = DP;
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

    @Override
    public String toString() {
        return "UserPojo{" +
                "NAME='" + NAME + '\'' +
                ", EMAIL='" + EMAIL + '\'' +
                ", PASSWORD='" + PASSWORD + '\'' +
                ", PHONE='" + PHONE + '\'' +
                ", ADDRESS='" + ADDRESS + '\'' +
                ", DP='" + DP + '\'' +
                ", SEC_QUES='" + SEC_QUES + '\'' +
                ", ANSWER='" + ANSWER + '\'' +
                '}';
    }
}
