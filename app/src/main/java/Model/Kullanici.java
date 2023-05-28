package Model;

import java.util.List;
import java.util.Map;

public class Kullanici {

    private String id;
    private String kullaniciadi;
    private String ad;
    private String resimurl;






    private String telNo;

    private String mail;


    private String sure;
    private  String mesafe;

    private Map<String,Boolean> EgitimDurumu;



    public Kullanici(){

    }

    public Kullanici(String id, String kullaniciadi, String ad, String resimurl, String mail,  String telNo, Map <String,
            Boolean> EgitimDurumuç, String sure, String mesafe) {
        this.id = id;
        this.kullaniciadi = kullaniciadi;
        this.ad = ad;
        this.resimurl = resimurl;





        this.mail = mail;
        this.telNo = telNo;
        this.EgitimDurumu = EgitimDurumu;

        this.sure = sure;
        this.mesafe = mesafe;


    }

    public String getSure() {
        return sure;
    }

    public void setSure(String sure) {
        this.sure = sure;
    }

    public String getMesafe() {
        return mesafe;
    }

    public void setMesafe(String mesafe) {
        this.mesafe = mesafe;
    }

    public Map<String, Boolean> getEgitimDurumu() {
        return EgitimDurumu;
    }

    public void setEgitimDurumu(Map<String, Boolean> egitimDurumu) {
        EgitimDurumu = egitimDurumu;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }









    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKullaniciadi() {
        return kullaniciadi;
    }

    public void setKullaniciadi(String kullaniciadi) {
        this.kullaniciadi = kullaniciadi;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getResimurl() {
        return resimurl;
    }

    public void setResimurl(String resimurl) {
        this.resimurl = resimurl;
    }


}
