package tech.land.ownership;

import java.util.HashSet;

import static com.google.common.collect.Sets.newHashSet;

public class TestUtilities {

    public static HashSet<String> setOf(String... companyIds) {
        return newHashSet(companyIds);
    }
}
