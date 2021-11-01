package com.claudiodornelles.data.impl.reports.config;

import java.util.ArrayList;
import java.util.List;

public class ChallengeReportTestConfig {

    public static List<String> regularFileResponse() {
        List<String> response = new ArrayList<>();
        response.add("001ç1234567891234çDiegoç50000");
        response.add("001ç3245678865434çJoão Gonçalvesç40000.99");
        response.add("002ç2345675434544345çJose da SilvaçRural");
        response.add("002ç2345675433444345çEduardoPereiraçRural");
        response.add("003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çDiego");
        response.add("003ç09ç[1-1-1,2-1-1.50,3-1-0.10]çJoão Gonçalves");
        return response;
    }

    public static List<String> fileWithInvalidPrefixResponse() {
        List<String> response = new ArrayList<>();
        response.add("018ç1234567891234çDiegoç50000");
        response.add("015ç3245678865434çRenatoç40000.99");
        response.add("012ç2345675434544345çJose da SilvaçRural");
        response.add("032ç2345675433444345çEduardoPereiraçRural");
        response.add("063ç10ç[1-10-100,2-30-2.50,3-40-3.10]çDiego");
        response.add("803ç08ç[1-34-10,2-33-1.50,3-40-0.10]çRenato");
        return response;
    }

    public static List<String> fileWithIncompleteInformationResponse() {
        List<String> response = new ArrayList<>();
        response.add("018çDiegoç50000");
        response.add("015ç3245678865434ç40000.99");
        response.add("012ç2345675434544345çRural");
        response.add("032ç2345675433444345çEduardoPereira");
        response.add("063ç10ç[1-10-100,2-30-2.50,3-40-3.10]");
        response.add("08ç[1-34-10,2-33-1.50,3-40-0.10]çRenato");
        return response;
    }
}
