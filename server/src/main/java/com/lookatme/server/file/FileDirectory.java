package com.lookatme.server.file;

import lombok.Getter;

@Getter
public enum FileDirectory {
    post("post"),
    item("item"),
    profile("profile");

    private final String name;

    FileDirectory(String name) {
        this.name = name;
    }
}
