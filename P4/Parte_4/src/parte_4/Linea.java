
package parte_4;

public class Linea {
    //ATRIBUTOS
    private String conloc;
    private String etiqueta;// Almacena la etiqueta de la línea.
    private String codop;// Almacena el código de operación.
    private String operando;// Almacena el operando.
    private String ADDR;// Almacena la dirección de memoria.
    private String size;// Almacena el tamaño de la línea.
    private String porCalcular;// Almacena información por calcular o procesar.
    

    public Linea(String conloc, String etiqueta, String codop, String operando, String ADDR) {
        this.conloc = conloc;
        this.etiqueta = etiqueta;
        this.codop = codop;
        this.operando = operando;
        this.ADDR = ADDR;
        this.size = " ";// Tamaño inicialmente vacío.
        this.porCalcular = " ";// Información por calcular inicialmente vacía.
    }
    // MÉTODOS GETTERS
    public String getEtiqueta() {
        return etiqueta;
    }

    public String getCodop() {
        return codop;
    }

    public String getOperando() {
        return operando;
    }

    public String getADDR() {
        return ADDR;
    }

    public String getSize() {
        return size;
    }

    public String getPorCalcular() {
        return porCalcular;
    }

    public String getConloc() {
        return conloc;
    }
    
    // MÉTODOS SETTERS
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public void setCodop(String codop) {
        this.codop = codop;
    }

    public void setOperando(String operando) {
        this.operando = operando;
    }

    public void setADDR(String ADDR) {
        this.ADDR = ADDR;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setPorCalcular(String porCalcular) {
        this.porCalcular = porCalcular;
    }

    public void setConloc(String conloc) {
        this.conloc = conloc;
    }
    
    
}
