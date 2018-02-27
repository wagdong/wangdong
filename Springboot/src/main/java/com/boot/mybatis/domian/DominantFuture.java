package com.boot.mybatis.domian;


import java.io.Serializable;

/**
 * Created by fff on 2017/8/16.
 */
public class DominantFuture implements Serializable {

    private Long id;
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
