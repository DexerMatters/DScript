package com.dexer.dscript.dni;

import com.dexer.dscript.DFunction;
import com.dexer.dscript.DObject;

import java.util.HashMap;
import java.util.Map;

import static com.dexer.dscript.DClass.getObjectById;

public class DArray {
    public static void load(){
        Map<String, DFunction.NativeCode> map=new HashMap();
        map.put("Array$$",new DFunction.NativeCode(){
            @Override
            public DFunction.ParamIns run(DFunction.ParamIns[] pi, String id) {
                System.out.println(123);
                return null;
            }
        });
        DRegister.registerNativeMethods(map);
    }
}
