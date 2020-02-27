package com.dexer.dscript;

public class DError {
    public DError(DComplier complier,String type,int line,int position){
        complier.onError("");
    }
    public DError(DComplier complier,String type){
        if(type.equals("type"))
            complier.onError("Type error");
    }
}
