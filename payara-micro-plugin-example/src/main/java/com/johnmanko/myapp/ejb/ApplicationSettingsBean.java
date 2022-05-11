package com.johnmanko.myapp.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author John Manko
 */
@Startup
@Singleton
public class ApplicationSettingsBean {    
    
    @PersistenceContext
    public EntityManager em;
    
    @PostConstruct
    private void initialize() {
        
    }
    
}
