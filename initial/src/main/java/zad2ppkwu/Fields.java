package zad2ppkwu;

/**
 * Created by Justyna on 08.04.2018.
 */
public class Fields {
    private long id;
    private String mail;
    private int nip;
    private int pesel;
    private int regon;
    private char postcode;

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

    public int getNip() {
        return nip;
    }

    public void setNip(int nip) {
        this.nip = nip;
    }

    public int getPesel() {
        return pesel;
    }

    public void setPesel(int pesel) {
        this.pesel = pesel;
    }

    public int getRegon() {
        return regon;
    }

    public void setRegon(int regon) {
        this.regon = regon;
    }

    public char getPostcode() {
        return postcode;
    }

    public void setPostcode(char postcode) {
        this.postcode = postcode;
    }
}
