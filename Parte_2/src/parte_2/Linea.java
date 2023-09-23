
package parte_2;

public class Linea {
    //ATRIBUTOS
    private String etiqueta;
    private String codop;
    private String operando;
    private Linea siguiente;
    private String ADDR;

    public Linea(String etiqueta, String codop, String operando, Linea siguiente, String ADDR) {
        this.etiqueta = etiqueta;
        this.codop = codop;
        this.operando = operando;
        this.siguiente = siguiente;
        this.ADDR = ADDR;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public String getCodop() {
        return codop;
    }

    public String getOperando() {
        return operando;
    }

    public Linea getSiguiente() {
        return siguiente;
    }

    public String getADDR() {
        return ADDR;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public void setCodop(String codop) {
        this.codop = codop;
    }

    public void setOperando(String operando) {
        this.operando = operando;
    }

    public void setSiguiente(Linea siguiente) {
        this.siguiente = siguiente;
    }

    public void setADDR(String ADDR) {
        this.ADDR = ADDR;
    }
}
