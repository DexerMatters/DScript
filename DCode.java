package com.dexer.dscript;

public class DCode {
    private String code;
    public DCode(String code){
        this.code=code.replaceAll("\\n","");
    }

    public void setCode(String code) {
        this.code = code.replaceAll("\\n","");
    }

    public String getCode() {
        return code;
    }

}
