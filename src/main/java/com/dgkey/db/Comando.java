package com.dgkey.db;

public class Comando {
    public String operacao;
    public Mark mark;

    public Comando(String operacao, Mark mark) {
        this.operacao = operacao;
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "Comando{" +
                "operacao='" + operacao + '\'' +
                ", mark=" + mark +
                '}';
    }
}
