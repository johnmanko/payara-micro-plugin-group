package com.johnmanko.lib.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author John Manko
 */
@Entity
@NamedQueries(value = {
    @NamedQuery(name = "ApplicationSetting.findAll", query = "SELECT a FROM ApplicationSetting a WHERE a.id = :id"),
    @NamedQuery(name = "ApplicationSetting.findbyId", query = "SELECT object(o) FROM ApplicationSetting o WHERE o.id = :id")
})
public class ApplicationSetting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @Column(length = 2000)
    private String value;

    public ApplicationSetting() {
    }

    public ApplicationSetting(String id, String value) {
        this.id = id;
        this.value = value;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
