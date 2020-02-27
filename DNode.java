package com.dexer.dscript;

import java.util.ArrayList;

public class DNode {
    private int id;
    private String type;
    private String content;
    private String condition="none";
    private ArrayList<DNode> sub_nodes=new ArrayList<>();
    public void setType(String type) {
        this.type = type;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getType() {
        return type;
    }

    public String getCondition() {
        return condition;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return sub_nodes.size();
    }

    public ArrayList<DNode> getSubNodes() {
        return sub_nodes;
    }

    public void addSubNode(DNode node){
        sub_nodes.add(node);
    }
}
