package it.valeriovaudi.documentlibrary.model;

/**
 * Created by Valerio on 29/04/2015.
 */
public class Page {
    private String id;
    private String fileName;
    private byte[] bytes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "Page{" +
                "id='" + id + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
