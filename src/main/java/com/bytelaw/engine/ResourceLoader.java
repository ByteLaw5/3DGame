package com.bytelaw.engine;

import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class ResourceLoader {
    public static String loadResource(String fileName) throws Exception {
        String result;
        try(InputStream in = ResourceLoader.class.getResourceAsStream(fileName); Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
           result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

    public static List<String> getAllFileLines(String fileName) throws Exception {
        List<String> ret = Lists.newArrayList();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(Class.forName(ResourceLoader.class.getName()).getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                ret.add(line);
            }
        }
        return ret;
    }
}
