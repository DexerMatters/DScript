package com.dexer.dscript.dni;

import com.dexer.dscript.DFunction;

import java.util.HashMap;
import java.util.Map;
import static com.dexer.dscript.DFunction.*;
public class DRegister {
    static Map<String,NativeCode> map=new HashMap<String,NativeCode>();
    static{
        DTest.load();
    }
    static void registerNativeMethods(Map<String,NativeCode> map_){
        map.putAll(map_);
    }
    public static NativeCode gain(String className,String methodName){

        return map.get(className+"$"+methodName);
    }

}
