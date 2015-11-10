package it.valeriovaudi.documentlibrary.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.Map;

import static it.valeriovaudi.documentlibrary.support.GridFSDBFileSupport.gridFSDBFile2ByteArray;
import static it.valeriovaudi.documentlibrary.support.MongoDbCommonQueryFactory.createQueryFindById;

/**
 * Created by Valerio on 04/05/2015.
 */

@Repository
public class PdfBookMasterRepositoryMongoImpl implements PdfBookMasterRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfBookMasterRepositoryMongoImpl.class);
    private static final String PDF_CONTENT_TYPE = "application/pdf";

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public void setGridFsTemplate(GridFsTemplate gridFsTemplate) {
        this.gridFsTemplate = gridFsTemplate;
    }

    @Override
    public String savePdfMaster(InputStream inputStream,String fileName,Map<String,String> metadata){
        return String.valueOf(gridFsTemplate.store(inputStream, fileName, PDF_CONTENT_TYPE, metadata).getId());
    }

    @Override
    public byte[] readPdfMaster(String id){
        return gridFSDBFile2ByteArray(gridFsTemplate.findOne(createQueryFindById(id)));
    }

    @Override
    public boolean deletePdfMaster(String id){
        gridFsTemplate.delete(createQueryFindById(id));
        return gridFsTemplate.findOne(createQueryFindById(id)) == null;
    }

}
