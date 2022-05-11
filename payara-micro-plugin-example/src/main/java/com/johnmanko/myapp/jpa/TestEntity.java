package com.johnmanko.myapp.jpa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author John Manko
 */
@Entity
public class TestEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String someValue;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the someValue
     */
    public String getSomeValue() {
        return someValue;
    }

    /**
     * @param someValue the someValue to set
     */
    public void setSomeValue(String someValue) {
        this.someValue = someValue;
    }    
    
}
