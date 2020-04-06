package com.dexer.dscript.dni;

import com.dexer.dscript.DComplier;
import com.dexer.dscript.DFunction;

import java.util.HashMap;
import java.util.Map;

public class DSystem {
    public static void load(){
        Map<String, DFunction.NativeCode> map=new HashMap();
        map.put("System$output",new DFunction.NativeCode(){
            @Override
            public DFunction.ParamIns run(DFunction.ParamIns[] pi, String id) {
                System.out.println(pi[0].value);
                return null;
            }
        });
        map.put("System$exit",new DFunction.NativeCode(){
            @Override
            public DFunction.ParamIns run(DFunction.ParamIns[] pi, String id) {
                DComplier.complier.close();
                return null;
            }
        });
        DRegister.registerNativeMethods(map);
    }
}
