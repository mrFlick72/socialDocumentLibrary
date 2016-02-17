package it.valeriovaudi.documentlibrary.common.web.htmcompressor;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.CharArrayWriter;
import java.io.PrintWriter;

/**
 * Created by Valerio on 17/02/2016.
 */
public class CharResponseWrapper extends HttpServletResponseWrapper {

    private final CharArrayWriter output;

    @Override
    public String toString() {
        return output.toString();
    }

    public CharResponseWrapper(HttpServletResponse response) {
        super(response);
        output = new CharArrayWriter();
    }

    @Override
    public PrintWriter getWriter() {
        return new PrintWriter(output);
    }
}