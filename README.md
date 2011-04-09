DebugProxy
=============

DebugProxy is intended to be used as a web browser proxy to facilitate debugging and resolving problems on web pages that is outside of the code that you can control.  For example, if you commonly write 
code that injects itself into other areas of a web page, this proxy can be used to debug cases where markup on that page is preventing your injection from working properly.  You may be familiar with 
the [Proxomitron](http://www.proxomitron.info/) and [Fiddler](http://www.fiddler2.com/) software. The DebugProxy more closely emulates the functionality of the Proxomitron, except with one major improvement
over both Fiddler and Proxomitron... it runs on all platforms, is simpler to use, and doesn't have a tie-dye background!

Download the Code
-------

    $ git clone git@github.com:matthewbogner/DebugProxy.git
    $ cd DebugProxy
    $ git remote add upstream git://github.com/matthewbogner/DebugProxy.git
    $ git fetch upstream

Build
-------

This project uses [Apache Maven](http://maven.apache.org/guides/getting-started/) for building its artifacts.

    $ cd DebugProxy
    $ mvn package
    $ cd target
    $ java -jar debugproxy-1.0-with-deps.jar

Running
------------
When you open the proxy, there is a simple text field to enter a port number that the server will listen on. It defaults to 2424, but can be changed to anything you wish. When the server starts, 
it will bind to your localhost/127.0.0.1 address. After starting the server, point your browser to connect to a proxy at localhost:2424 (or whatever port number you decided to use).

HTTPS traffic will tunnel through, but can't be intercepted at this time.

You can add filtering rules while the server is started or stopped. Defining a rule amounts to providing a URL to intercept, and a file from the local disk to serve up instead. The proxomitron was 
pretty picky about the URL you provided and whether or not it would actually match the site you are visiting. This proxy is intended to be much more lenient. Let's see an example:

    `URL: http://www.google.com?q=something+to+search`
    `File: /tmp/local-google.htm`

When determining whether or not to match the URL you are visiting, the Debug Proxy will strip off the protocol and query string from the URL that you have provided. In this example, the proxy will 
match any requests to www.google.com. The proxomitron was not this forgiving.