package com.security.project.utils;

import java.io.*;

public class EmailGenerator {

    private static final String ENCODING = "UTF-8";
    private static final String USER_NAME = "$user$";
    private static final String VERIFICATION_TOKEN = "$token$";

    private EmailGenerator(){

    }

    private static Reader getFileReader(final String fileName) {
        try {
            return new InputStreamReader(EmailGenerator.class.getResourceAsStream(fileName), ENCODING);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Wrong encoding " + fileName, e);
        }
    }

    public static String generateVerificationMail(String userName, String token, String emailType) {
        try {
            return getMailTemplate(emailType).toString()
                    .replace(USER_NAME, userName)
                    .replace(VERIFICATION_TOKEN,token);
        } catch (final IOException e) {
            throw new RuntimeException("failed to parse invite template", e);
        }
    }

//    public static String sendHtml(String userName, String emailType){
//        try {
//            return getMailTemplate(emailType).toString()
//                    .replace(USER_NAME, userName);
//        } catch (final IOException e) {
//            throw new RuntimeException("failed to parse invite template", e);
//        }
//    }

    private static StringBuilder getMailTemplate(String emailType) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(getFileReader(emailType));
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        return stringBuilder;
    }
}
