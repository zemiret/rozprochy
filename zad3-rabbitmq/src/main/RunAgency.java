package main;

import cosmic.Agency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class RunAgency {
    public static void main(String[] args) throws IOException, TimeoutException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Agency name: ");
        String name = br.readLine();

        Agency a = new Agency(name);
        a.run();
    }
}
