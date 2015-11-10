package it.valeriovaudi.documentlibrary.repository;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by Valerio on 04/05/2015.
 */
public interface PdfBookMasterRepository {
    String savePdfMaster(InputStream inputStream,String fileName,Map<String,String> metadata);
    byte[] readPdfMaster(String id);
    boolean deletePdfMaster(String id);
}
