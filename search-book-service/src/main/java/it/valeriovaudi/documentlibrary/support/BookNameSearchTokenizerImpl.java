package it.valeriovaudi.documentlibrary.support;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by Valerio on 12/06/2015.
 */
@Component
public class BookNameSearchTokenizerImpl implements BookNameSearchTokenizer {
    @Override
    public List<String> bookNameTokenizer(String bookName) {
        return Arrays.asList(bookName.split(" ")).stream().<String>map(String::toUpperCase).collect(toList());
    }
}
