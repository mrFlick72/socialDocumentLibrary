package it.valeriovaudi.documentlibrary.model;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Valerio on 26/05/2015.
 */
@Entity
@Table
public class DocumentLibraryUser implements Serializable {

    private static final long SERIAL_VERSION_UID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String firstName;
    private String lastName;

    @Email
    private String mail;

    @Column(nullable = false, unique = true)
    @NotNull
    @NotBlank
    private String userName;

    @Column(nullable = false)
    @NotNull
    @NotBlank
    private String password;

    private boolean showShortHelp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isShowShortHelp() {
        return showShortHelp;
    }

    public void setShowShortHelp(boolean showShortHelp) {
        this.showShortHelp = showShortHelp;
    }

    @Override
    public String toString() {
        return "DocumentLibraryUser{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mail='" + mail + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", showShortHelp=" + showShortHelp +
                '}';
    }
}
