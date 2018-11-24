package com.kapoopr.elasticSearch.demo.model;

/**
 * Base Model class.
 */

public class Model {
    private Integer uniqueId;

    public Integer getId() {
        return uniqueId;
    }

    public void setId(Integer id) {
        this.uniqueId = id;
    }

    public Model(Integer id) {
        super();
        this.uniqueId = id;
    }

    public Model() {
        // TODO Auto-generated constructor stub
    }

}
