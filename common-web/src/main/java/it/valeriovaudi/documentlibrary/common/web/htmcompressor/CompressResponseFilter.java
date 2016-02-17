package it.valeriovaudi.documentlibrary.common.web.htmcompressor;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Valerio on 17/02/2016.
 */
@WebFilter(
        filterName = "CompressResponseFilter",
        urlPatterns = { "/*" }
)
public class CompressResponseFilter implements Filter {

    private HtmlCompressor compressor;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        CharResponseWrapper responseWrapper = new CharResponseWrapper(
                (HttpServletResponse) resp);

        chain.doFilter(req, responseWrapper);

        String servletResponse = responseWrapper.toString();
        resp.getWriter().write(compressor.compress(servletResponse));
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        compressor = new HtmlCompressor();
        compressor.setCompressCss(true);
        compressor.setCompressJavaScript(true);
    }

    @Override
    public void destroy() {
    }

}