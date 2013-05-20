/**
 * TODO Avoid polluting the global namespace. See also HTMLHeadInsert.jsp.
 * 
 *  - Overriding without polluting the global namespace: http://stackoverflow.com/a/9172526/6689
 */

convertToWaybackURL = function ( url ) {
  	// URL may already be in appropriate form - just return it:
  	if( url.indexOf(window.waybackPrefix) == 0 ) return url;
  	// Need to build an absolute URL.
  	if( url.indexOf("//") == 0 ) {
  		url = window.waybackOriginalHost + "http" + url;
  	}
  	// Server-relative URL
  	if( url.indexOf("/") == 0 ) {
  		url = window.waybackOriginalHost + url;
  	}
  	// Relative URL, should be fine:
  	if( url.indexOf("http://") != 0 && url.indexOf("https://") != 0 ) {
  		return url;
  	}
  	// Absolute URL (on original or other server)
  	// Turn into archival URL:
  	return window.waybackReplayPrefix + url;
};


  /**
   * Override window.XMLHttpRequest invocations to refer to the archival host.
   */
/*
(function() {
  window.XMLHttpRequestBase = window.XMLHttpRequest; 
  window.XMLHttpRequest = function() {
    var base = new window.XMLHttpRequestBase(); 
    base.openBase = base.open; 
    base.open = function(sMethod, sUrl, bAsync, sUser, sPassword){
        console.log("XHR "+sMethod+": "+sUrl);
        sUrl = convertToWaybackURL(sUrl);
        console.log("XHR "+sMethod+": "+sUrl);
  	  base.openBase(sMethod, sUrl, bAsync, sUser, sPassword);
    }
    return base;
  }
  XMLHttpRequest = window.XMLHttpRequest;
})();
*/
/*
(function() {
	    var proxied = window.XMLHttpRequest.prototype.open;
	    window.XMLHttpRequest.prototype.open = function() {
	        console.log( arguments );
	        sUrl = arguments[1];
	        console.log("XHR: "+sUrl);
	        sUrl = convertToWaybackURL(sUrl);
	        console.log("XHR: "+sUrl);
	        arguments[1] = sUrl;
	        return proxied.apply(this, [].slice.call(arguments));
	    };
	    XMLHttpRequest.prototype.open = window.XMLHttpRequest.prototype.open
})();
*/
/*
(function () {

	    	// Save reference to earlier defined object implementation (if any)
	    	var oXMLHttpRequest	= window.XMLHttpRequest;

	    	// Define on browser type
	    	var bGecko	= !!window.controllers,
	    		bIE		= window.document.all && !window.opera,
	    		bIE7	= bIE && window.navigator.userAgent.match(/MSIE 7.0/);
	    	
	    	// Enables "XMLHttpRequest()" call next to "new XMLHttpReques()"
	    	function fXMLHttpRequest() {
	    		this._object	= oXMLHttpRequest && !bIE7 ? new oXMLHttpRequest : new window.ActiveXObject("Microsoft.XMLHTTP");
//	    		this._listeners	= [];
	    	};

	    	// Constructor
	    	function cXMLHttpRequest() {
	    		return new fXMLHttpRequest;
	    	};
	    	cXMLHttpRequest.prototype	= fXMLHttpRequest.prototype;
	    	
	    	// Public Methods
	    	cXMLHttpRequest.prototype.open	= function(sMethod, sUrl, bAsync, sUser, sPassword) {
		        console.log("XHR: "+sUrl);
		        sUrl = convertToWaybackURL(sUrl);
		        console.log("XHR: "+sUrl);
		        if (arguments.length > 4)
					return this._object.open(sMethod, sUrl, bAsync, sUser, sPassword);
				else
				if (arguments.length > 3)
					return this._object.open(sMethod, sUrl, bAsync, sUser);
				else
					return this._object.open(sMethod, sUrl, bAsync);
		    };
		        
		    cXMLHttpRequest.prototype.send	= function() {
		        console.log( arguments );
		        return this._object.send.apply(this, [].slice.call(arguments));
		    }
	    	
	    	// Register new object with window
		    window.XMLHttpRequest	= cXMLHttpRequest;
		    XMLHttpRequest	= cXMLHttpRequest;
})();
*/


(function(XHR) {
    "use strict";

    var open = XHR.prototype.open;
    var send = XHR.prototype.send;

    XHR.prototype.open = function(method, url, async, user, pass) {
    	console.log("XHR: "+method+" "+url);
        url = convertToWaybackURL(url);
        console.log("XHR: "+url);
        this._url = url;
        open.call(this, method, url, async, user, pass);
    };

    XHR.prototype.send = function(data) {
        var self = this;
        var oldOnReadyStateChange;
        var url = this._url;

        function onReadyStateChange() {
            if(self.readyState == 4 /* complete */) {
                /* This is where you can put code that you want to execute post-complete*/
                /* URL is kept in this._url */
            }

            if(oldOnReadyStateChange) {
                oldOnReadyStateChange();
            }
        }

        /* Set xhr.noIntercept to true to disable the interceptor for a particular call */
        if(!this.noIntercept) {            
            if(this.addEventListener) {
                this.addEventListener("readystatechange", onReadyStateChange, false);
            } else {
                oldOnReadyStateChange = this.onreadystatechange; 
                this.onreadystatechange = onReadyStateChange;
            }
        }

        send.call(this, data);
    }
})(XMLHttpRequest);


  /**
   * Override window.open invocations to refer to the archival host.
   */
    //
    // https://developer.mozilla.org/en-US/docs/DOM/window.open
    // var windowObjectReference = window.open(strUrl, strWindowName[, strWindowFeatures]);
    //
    // http://msdn.microsoft.com/en-gb/library/ie/ms536651(v=vs.85).aspx
    // var retval = window.open(url, name, features, replace);
    
  window.openBase = window.open; 

  window.open = function(sUrl, strWindowName, features, replace ){
        console.log("GIVEN "+strWindowName+": "+sUrl);
        sUrl = convertToWaybackURL(sUrl);
        console.log("OPEN "+strWindowName+": "+sUrl);
  	  window.openBase(sUrl, strWindowName, features, replace);
  }

  document.writeBase = document.write; 

  document.write = function( str ){
        console.log("document.write.1 "+str);
        var wrapper= document.createElement('div');
        wrapper.innerHTML = str;
        if( wrapper.firstChild.tagName == "SCRIPT" ) {
    	    console.log("script.src.1 "+wrapper.firstChild.src);
    	    wrapper.firstChild.src = convertToWaybackURL(wrapper.firstChild.src);
    	    console.log("script.src.1 "+wrapper.firstChild.src);
        }
        // pass it on...
        console.log("document.write.2 "+wrapper.innerHTML);
        document.writeBase( wrapper.innerHTML );
  }

  /**
   * Override element addition events, looking for hrefs to patch up.
   */

  document.createElementBase = document.createElement;

  document.createElement = function (tag) {
  	  if (tag === 'script' && tag.src != undefined ) {
  		  console.log("document.createElement.2 "+tag.src);
  		  tag.src = convertToWaybackURL(tag.src); 
  		  console.log("document.createElement.3 "+tag.src);
  	  }
  	  return document.createElementBase(tag);
  };

