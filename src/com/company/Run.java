package com.company;

import java.io.IOException;

public class Run {
    public static void main (String []args) throws IOException {
        KMeans means = new KMeans(3,20);
        means.createData();
        means.getData();
        means.sort();
    }
}
