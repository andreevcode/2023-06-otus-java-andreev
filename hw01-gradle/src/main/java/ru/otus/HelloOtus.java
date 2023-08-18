package ru.otus;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

public class HelloOtus {

    public static void main(String[] args) {
        var chars = Lists.newArrayList('h','e','l','l','o','_','w','o','r','l','d');
        var result = ImmutableSet.copyOf(chars).asList();
        System.out.println(result);
    }
}
