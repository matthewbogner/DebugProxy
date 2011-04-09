package org.debugproxy;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.littleshoot.proxy.HttpFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: mbogner
 * @date: 2/18/11
 */
public class DebugProxyFilter implements HttpFilter {

    private RewriteRule _rewriteRule;

    public DebugProxyFilter(RewriteRule rewriteRule) {
        _rewriteRule = rewriteRule;
    }

    @Override
    public String toString() {
        return "DebugProxyFilter{" +
                "_rewriteRule=" + _rewriteRule +
                '}';
    }

    /**
     * Filters the HTTP response.
     *
     * @param response The response to filter.
     * @return The filtered response.
     */

    @Override
    public HttpResponse filterResponse(HttpResponse response) {
        System.out.println("should have filtered this response");

        //Read file contents for this rule into a byte array
        byte[] fileBytes = null;
        try {
            fileBytes = getBytesFromFile(new File(_rewriteRule.getFileName()));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            fileBytes = null;
        }

        if (fileBytes != null && fileBytes.length > 0) {
            //ChannelBufferFactory cb = new HeapChannelBufferFactory();
            //response.setContent(cb.getBuffer(fileBytes, 0, 0));

            response.setContent(ChannelBuffers.copiedBuffer(fileBytes));
            response.setHeader("Content-length", fileBytes.length);
        }
        return response;
    }


    /**
     * Returns the maximum response size to expect in bytes for this filter.
     * You should set this as small as possible to save memory, but of course
     * not smaller than response body sizes will be.
     *
     * @return The maximum response body size to support for this filter,
     *         in bytes.
     */
    @Override
    public int getMaxResponseSize() {
        return 1024*1000;  //1MB.... overkill
    }


    /**
     * Returns whether or not to filter responses received from the specified
     * HTTP request.
     *
     * @param httpRequest The request to check.
     * @return <code>true</code> if we should apply this set of rules,
     *         otherwise <code>false</code>.
     */
    @Override
    public boolean shouldFilterResponses(HttpRequest httpRequest) {
        String requestUri = httpRequest.getHeader("Host") + httpRequest.getUri();

        if (requestUri.startsWith(_rewriteRule.getSanitizedUrl())){
            return true;
        }
        return false;
    }

    private static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }


}
