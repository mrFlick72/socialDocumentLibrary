package it.valeriovaudi.documentlibrary.model.builder;

import it.valeriovaudi.documentlibrary.model.DocumentLibraryUser;

/**
 * Created by Valerio on 27/05/2015.
 */
public class DocumentLibraryUserBuilder {

    private DocumentLibraryUser documentLibraryUser;

    private DocumentLibraryUserBuilder(){
    }

    private void setDocumentLibraryUser(DocumentLibraryUser documentLibraryUser) {
        this.documentLibraryUser = documentLibraryUser;
    }

    public static DocumentLibraryUserBuilder newDocumentLibraryUserBuilder(){
        return newDocumentLibraryUserBuilder(new DocumentLibraryUser());
    }

    public static DocumentLibraryUserBuilder newDocumentLibraryUserBuilder(DocumentLibraryUser documentLibraryUser){
        DocumentLibraryUserBuilder documentLibraryUserBuilder = new DocumentLibraryUserBuilder();
        documentLibraryUserBuilder.setDocumentLibraryUser(documentLibraryUser);
        return documentLibraryUserBuilder;
    }

    public DocumentLibraryUserBuilder id(Long id){
        this.documentLibraryUser.setId(id);
        return this;
    }

    public DocumentLibraryUserBuilder firstName(String firstName){
        this.documentLibraryUser.setFirstName(firstName);
        return this;
    }

    public DocumentLibraryUserBuilder lastName(String lastName){
        this.documentLibraryUser.setLastName(lastName);
        return this;
    }

    public DocumentLibraryUserBuilder mail(String mail){
        this.documentLibraryUser.setMail(mail);
        return this;
    }

    public DocumentLibraryUserBuilder userName(String userName){
        this.documentLibraryUser.setUserName(userName);
        return this;
    }

    public DocumentLibraryUserBuilder password(String password){
        this.documentLibraryUser.setPassword(password);
        return this;
    }

    public DocumentLibraryUserBuilder showShortHelp(boolean showShortHelp){
        this.documentLibraryUser.setShowShortHelp(showShortHelp);
        return this;
    }

    public DocumentLibraryUser build(){
        return documentLibraryUser;
    }

}
