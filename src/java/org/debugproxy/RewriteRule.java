package org.debugproxy;

import java.net.URI;

/**
 * @author: mbogner
 * @date: 2/17/11
 */
public class RewriteRule {
    private String _url;
    private String _sanitizedUrl;
    private String _fileName;
    private String _hostAndPort;
    private static String[] _columns = new String[]{ "URL to Match", "Filename" };

    public RewriteRule(String url, String fileName) {
        _url = url;
        _fileName = fileName;

        //Keep a "sanitized" copy of the URL.  This is essentially just the URL that the user entered without the protocol and without any query parameters
        if (_url.contains("?")) {
            _sanitizedUrl = _url.substring(0, _url.indexOf("?"));
        } else {
            _sanitizedUrl = _url;
        }
        if (_sanitizedUrl.contains("://")) {
            _sanitizedUrl = _sanitizedUrl.substring(_sanitizedUrl.indexOf("://")+3);
        }

        //This is used by the LittleProxy HttpFilter to determine if this rule should even apply to the host that
        //a particular request was targeted at.  This is a bit overkill in our situation, but easier to just go with the flow...
        try {
            URI uri = new URI(_url);
            _hostAndPort = uri.toURL().getHost();
        }catch(Exception e) {
            e.printStackTrace();
            _hostAndPort = "";
        }

    }

    public String getUrl() {
        return _url;
    }

    public String getFileName() {
        return _fileName;
    }
    public String getSanitizedUrl() {
        return _sanitizedUrl;
    }
    public String getHostAndPort() {
        return _hostAndPort;
    }
    public static String[] getColumnNames() {
        return _columns;
    }

    public Object getValueForColumn(int colIndex) {
        if (colIndex < _columns.length) {
            switch(colIndex){
                case 0: return this.getUrl();
                case 1: return this.getFileName();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "RewriteRule{" +
                "_url='" + _url + '\'' +
                ", _fileName='" + _fileName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RewriteRule that = (RewriteRule) o;

        if (!_fileName.equals(that._fileName)) return false;
        if (!_url.equals(that._url)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _url.hashCode();
        result = 31 * result + _fileName.hashCode();
        return result;
    }
}
