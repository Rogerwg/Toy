package com.toy.component.user.domain;

import java.util.Set;

public class AddressCluster {
    private int id;

    private int size;

    private Set<Integer> members;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Set<Integer> getMembers() {
        return members;
    }

    public void setMembers(Set<Integer> members) {
        this.members = members;
        this.size=members.size();
    }
}
