package com.aixohub.portfolio.checks;

import com.aixohub.portfolio.model.Client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class Checker {
    private static final List<Check> CHECKS;

    static {
        CHECKS = new ArrayList<Check>();
        Iterator<Check> iter = ServiceLoader.load(Check.class).iterator();
        while (iter.hasNext())
            CHECKS.add(iter.next());
    }

    public static final List<Issue> runAll(Client client) {
        List<Issue> answer = new ArrayList<Issue>();

        for (Check check : CHECKS)
            answer.addAll(check.execute(client));

        return answer;
    }
}
