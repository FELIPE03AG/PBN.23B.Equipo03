package parte_5;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author anaya
 */
public class S19 {
    String NombreAsm;
    String Sn;
    String cc;
    String addr;
    String data;
    String ck;
    
    
    public S19(String NombreAsm, String Sn, String cc, String addr, String data, String ck) {
        this.NombreAsm = NombreAsm;
        this.Sn = Sn;
        this.cc = cc;
        this.addr = addr;
        this.data = data;
        this.ck = ck;
    }

    public String getNombreAsm() {
        return NombreAsm;
    }

    public void setNombreAsm(String NombreAsm) {
        this.NombreAsm = NombreAsm;
    }

    public String getSn() {
        return Sn;
    }

    public void setSn(String Sn) {
        this.Sn = Sn;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCk() {
        return ck;
    }

    public void setCk(String ck) {
        this.ck = ck;
    }
}
