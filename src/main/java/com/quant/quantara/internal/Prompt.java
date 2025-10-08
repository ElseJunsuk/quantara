package com.quant.quantara.internal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.language.v1.Document;

public class Prompt {

    private String input;

    @JsonIgnore
    private Document resultDocument;

    public Prompt() {
    }

    public Prompt(String input) {
        this.input = input;
        this.resultDocument = Document.newBuilder()
                .setContent(input)
                .setType(Document.Type.PLAIN_TEXT)
                .build();
    }

    public Prompt(Document proto) {
        this.input = proto.hasContent() ? proto.getContent() : null;
        this.resultDocument = proto;
    }

    public static Prompt empty() {
        return new Prompt(Document.newBuilder().build());
    }

    public static Prompt of(String input) {
        return new Prompt(input);
    }

    public static Prompt of(Document document) {
        return new Prompt(document);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.input == null || this.input.isEmpty() || this.resultDocument == null || !this.resultDocument.hasContent();
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @JsonIgnore
    public Document getResultDocument() {
        return resultDocument;
    }

    public void setResultDocument(Document resultDocument) {
        this.resultDocument = resultDocument;
    }

}
