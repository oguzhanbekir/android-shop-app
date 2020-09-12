package com.example.eren.myapplication.Models;

public class TopCategories {
    String id;
    String name;
    String nodeId;

    public TopCategories(String id, String name, String nodeId) {
        this.id = id;
        this.name = name;
        this.nodeId = nodeId;
    }

    public TopCategories() {
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNodeId() {return nodeId; }
}
