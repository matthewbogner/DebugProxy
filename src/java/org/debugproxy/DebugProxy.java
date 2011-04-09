package org.debugproxy;

import org.littleshoot.proxy.DefaultHttpProxyServer;
import org.littleshoot.proxy.HttpFilter;

import java.util.HashMap;
import java.util.List;

/**
 * @author: Matthew Bogner
 * @date: 12/21/10
 */
public class DebugProxy {

    private int _portNumber = 2424;
    private Thread _proxyServerThread;
    private DefaultHttpProxyServer _server;
    private List<RewriteRule> _rewriteRules;
    private ServerStatus _serverStatus = ServerStatus.STOPPED;

    private enum ServerStatus { STARTED, STOPPED }

    public DebugProxy(List<RewriteRule> rewriteRules, int portNumber) {
        _rewriteRules = rewriteRules;
        _portNumber = portNumber;
    }
    public void stop() {
        if (_proxyServerThread != null && _server != null) {
            _server.stop();
            _proxyServerThread.interrupt();
            _serverStatus = ServerStatus.STOPPED;
        }
    }
    public void start(int portNumber) {
        //Stop any currently running server
        stop();

        _portNumber = portNumber;

        Runnable proxyServerRunnable = new Runnable() {
            @Override
            public void run() {
                HashMap<String, HttpFilter> filters = new HashMap<String, HttpFilter>();
                if (_rewriteRules != null && _rewriteRules.size() > 0) {
                    for(RewriteRule rule:_rewriteRules) {
                        String hostport = rule.getHostAndPort();
                        filters.put(hostport, new DebugProxyFilter(rule));
                    }
                }

                _server = new DefaultHttpProxyServer(_portNumber, filters);
                _server.start(true, true);
            }
        };

        _proxyServerThread = new Thread(proxyServerRunnable);

        _proxyServerThread.start();
        _serverStatus = ServerStatus.STARTED;

    }

    public void reloadRules(List<RewriteRule> rewriteRules) {
        _rewriteRules = rewriteRules;
        if (_serverStatus == ServerStatus.STARTED) {
            start(_portNumber);  // This will actually restart the server
        }
    }

}
