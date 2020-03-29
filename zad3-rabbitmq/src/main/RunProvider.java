package main;

import cosmic.Provider;
import infra.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class RunProvider {
    public static void main(String[] args) throws IOException, TimeoutException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Provider name: ");
        String name = br.readLine();

        System.out.println("Provider capability 1: ");
        String cap1 = br.readLine();
        if (!Constants.CAPABILITIES.contains(cap1)) {
            throw new RuntimeException("Such capability is not specified");
        }

        System.out.println("Provider capability 2: ");
        String cap2 = br.readLine();
        if (!Constants.CAPABILITIES.contains(cap2)) {
            throw new RuntimeException("Such capability is not specified");
        }

        if (cap1.equals(cap2)) {
            throw new RuntimeException("Provider can't have 2 same capabilities");
        }

        Provider p = new Provider(name, cap1, cap2);
        p.run();
    }
}
