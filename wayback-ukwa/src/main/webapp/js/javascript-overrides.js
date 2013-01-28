/**
 * Override window.XMLHttpRequest invocations to refer to the archival host.
 */
window.XMLHttpRequestBase = window.XMLHttpRequest; 

window.XMLHttpRequest = function() {
  var base = new window.XMLHttpRequestBase(); 
  base.openBase = base.open; 
  base.open = function(sMethod, sUrl, bAsync, sUser, sPassword){
      console.log("XHR "+sMethod+": "+sUrl); //manipulate sUrl here return 
	  base.openBase(sMethod, sUrl, bAsync, sUser, sPassword);
  }
  return base;
}



/**
 * Override window.open invocations to refer to the archival host.
 */
window.openBase = window.open; 

window.open = function() {
  var base = new window.openBase(); 
  base.openBase = base.open; 
  //
  // https://developer.mozilla.org/en-US/docs/DOM/window.open
  // var windowObjectReference = window.open(strUrl, strWindowName[, strWindowFeatures]);
  //
  // http://msdn.microsoft.com/en-gb/library/ie/ms536651(v=vs.85).aspx
  // var retval = window.open(url, name, features, replace);
  
  base.open = function(sMethod, sUrl, bAsync, sUser, sPassword){
      console.log("XHR "+sMethod+": "+sUrl); //manipulate sUrl here return 
	  base.openBase(sMethod, sUrl, bAsync, sUser, sPassword);
  }
  return base;
}

