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
        public String runFunction(String name, DFunction.ParamIns[] paramIns){
            for(DFunction func : static_func)
                if(func.getName().equals(name)) {
                    return func.run(paramIns);
                }
            return null;
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
    static Class getClassByName(String name){
        for (int i = 0; i < cls.size(); i++) {
            if(name.equals(cls.get(i).name)){
                return cls.get(i);
            }
        }
        return null;
    }
    static void importClass(){

    }
}
