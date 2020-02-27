package com.dexer.dscript;

import java.util.ArrayList;

class DClass {
    private static ArrayList<Class> cls=new ArrayList<>();
    public static class Class{
        private String name;
        private ArrayList<DFunction> static_func=new ArrayList<>(),
                                    dymastic_func=new ArrayList<>();
        private Class(String name){
            this.name=name;
        }
        public void addFunction(DFunction function){
            if(function.getState()==DFunction.DYMASTIC||function.getState()==(DFunction.DYMASTIC|DFunction.NATIVE))
                dymastic_func.add(function);
            else
                static_func.add(function);
        }

    }
    static String getTypeOf(String str){
        String type="";
        if(str.matches("^\"\\s*\"")){
            type="String";
        }
        else if(str.matches("-?[0-9]+")){
            type="Integer";
        }
        else if(str.matches("-?[0-9]+\\.[0-9]+")){
            type="float";
        }
        return type;
    }

    static void createClass(String name){
        cls.add(new Class(name));
    }
    static void importClass(){

    }
}
