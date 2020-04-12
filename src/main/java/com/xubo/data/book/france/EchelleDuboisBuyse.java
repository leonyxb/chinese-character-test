package com.xubo.data.book.france;

import com.xubo.data.book.Book;
import com.xubo.data.book.BookSource;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class EchelleDuboisBuyse extends BookSource {

    @Override
    protected List<Book> readBooks(List<String> rawLines) {
        Map<String, List<String>> lines = rawLines.stream()
                .collect(Collectors.groupingBy(
                        line -> line.split("\t")[0])
                );

        List<Book> books = new ArrayList<>();
        lines.forEach((k, v) -> {
            books.add(new EchelleBook(Integer.parseInt(k), v));
        });

        books.sort((o1, o2) -> {
            EchelleBook eb1 = (EchelleBook) o1;
            EchelleBook eb2 = (EchelleBook) o2;
            return eb1.getEchelle().compareTo(eb2.getEchelle());
        });
        return books;
    }

    @Override
    protected String getBookPath() {
        return "/book/french/echelle_dubois_buyse.txt";
    }

    @Override
    protected Charset getCharset() {
        return StandardCharsets.UTF_8;
    }
}
