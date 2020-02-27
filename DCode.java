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
    public DNode anaylze(){
        DNode node=new DNode();
        String condition="";
        String content="";
        String[] code_=code.split("");
        int id=0;
        int scan=0;
        for(int i=0;i<code_.length;i++){
            if(code_[i].equals(";")){
                int j=i;
                while (true){
                    if(j==code_.length-1) break;
                    j++;
                    if(code_[j].equals("{")) {
                        DNode sub=new DNode();
                        sub.setType("normal");
                        sub.setContent(content);
                        sub.setCondition("none");
                        sub.setId(id++);
                        node.addSubNode(sub);
                        content="";
                        scan = 1;
                        break;
                    }
                    if(code_[j+1].equals(";")||j+1==code_.length-1){
                        scan=0;
                        break;
                    }
                }
            }
            if(scan==1)
                condition+=code_[i];
            if(scan==0)
                content+=code_[i];
            if(code_[i].equals("}")){
                DNode sub=new DNode();
                sub.setType("conditional");
                sub.setContent(condition.substring(condition.indexOf("{")+1,condition.indexOf("}")));
                sub.setCondition(condition.substring(1,condition.indexOf("{")));
                sub.setId(id++);
                node.addSubNode(sub);
                condition="";
                scan=0;
            }
        }
        System.out.println(condition);
        System.out.println(content);
        return node;
    }

}
