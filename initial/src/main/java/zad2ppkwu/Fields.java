package zad2ppkwu;

import javafx.scene.control.Hyperlink;

import javax.validation.constraints.Size;

/**
 * Created by Justyna on 08.04.2018.
 */
public class Fields {
    private long id;

    //@Size(min = 5, message = "E-mail must follow the pattern: address@example.com")
    private String mail;

    //@Size(min = 10, max = 10, message = "NIP value must contain 10 digits")
    private long nip;

    //@Size(min = 11, max = 11, message = "PESEL value must contain 11 digits")
    private long pesel;

    //@Size(min = 9, max = 14, message = "REGON value must contain 9 or 14 digits")
    private long regon;

    //@Size(min = 6, max = 6, message = "Postcode must follow the pattern: XX-XXX")
    private String postcode;

    public Fields() {
    }

    public Fields(long id, String mail, long nip, long pesel, long regon, String postcode) {
        this.id = id;
        this.mail = mail;
        this.nip = nip;
        this.pesel = pesel;
        this.regon = regon;
        this.postcode = postcode;
    }
/**
    public void validateMail() {
        if(this.mail.length() < 5) {

        }
    }*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public long getNip() {
        return nip;
    }

    public void setNip(long nip) {
        this.nip = nip;
    }

    public long getPesel() {
        return pesel;
    }

    public void setPesel(long pesel) {
        this.pesel = pesel;
    }

    public long getRegon() {
        return regon;
    }

    public void setRegon(long regon) {
        this.regon = regon;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String  postcode) {
        this.postcode = postcode;
    }
}
