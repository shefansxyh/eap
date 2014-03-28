<%@page import="java.util.regex.Matcher"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.util.UUID"%>
<%@page language="java" pageEncoding="utf-8" contentType="application/javascript" trimDirectiveWhitespaces="true" %>
<%
response.setHeader("P3P", "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
%>
<%!
	public String htmlEscape(String input) {
		if (input == null) {
			return "";
		}
		StringBuffer result = new StringBuffer();
		int length = input.length();
		for (int i = 0; i < length; i++) {
			char c = input.charAt(i);
			if (c == '&') {
				result.append("&amp;");
			}
			else if (c == '\"') {
				result.append("&quot;");
			}
			else if (c == '<') {
				result.append("&lt;");
			}
			else if (c == '>') {
				result.append("&gt;");
			}
			else if (c == '\'') {
				result.append("&#39;");
			}
			else if (c == '/') {
				result.append("&#092;");
			}
			else {
				result.append(c);
			}
		}
		return result.toString();
	}

	public String javascriptEscape(String input) {
		if (input == null) {
			return "";
		}
		StringBuffer result = new StringBuffer();
		int length = input.length();
		for (int i = 0; i < length; i++) {
			char c = input.charAt(i);
			if (c == '"') {
				result.append("\"");
			}
			else if (c == '\'') {
				result.append("\\'");
			}
			else if (c == '\\') {
				result.append("\\\\");
			}
			else {
				result.append(c);
			}
		}
		return result.toString();
	}
	
	public String getDomain(String url) {
		if (url == null || url.length() == 0) {
			return null;
		}
		String startFlag = "://";
		int len = url.indexOf("/", url.indexOf(startFlag) + startFlag.length());
		if (len == -1) len = url.length();
		
		String domain = url.substring(url.indexOf(startFlag) + startFlag.length(), len);
		int portIdx = domain.lastIndexOf(":");
		if (portIdx != -1) {
			domain = domain.substring(0, portIdx);
		}
		
		return domain;
	}
	
	public String getTopDomain(String url) {
		String domain = getDomain(url);
		if (domain != null && domain.length() != 0) {
			String[] domainSubfixs = {
				".com",".edu",".gov",".int",".net",".biz",".info",".pro",".name",".museum",".coop",".aero",".idv",
				".cc",".tv",
				".cn",".hk",".mo",".tw"
			};
			for (String domainSubfix : domainSubfixs) {
				int sIdx = -1;
				if ((sIdx = domain.indexOf(domainSubfix)) != -1) {
					if ((sIdx = domain.lastIndexOf(".", sIdx -1)) != -1) {
						return domain.substring(sIdx + 1);
					}
				}
			}
			return domain;
		}
		return null;
	}
%>
<%
	String account = javascriptEscape(htmlEscape(request.getParameter("A")));
	if (account == null || account.length() == 0) {
		return;
	}

	String url = request.getRequestURL().toString();
	//request.getParameter("url");
	//if (url == null || url.length() == 0) {
	//	url = request.getHeader("referer");
	//}
	String topDomain = getTopDomain(url);
	
	String clientId = null;
	String sessionId = null;
	Cookie[] cookies = request.getCookies();
	if (cookies != null && cookies.length > 0) {
		for (Cookie cookie : cookies) {
			if ("STATS.CID".equals(cookie.getName())) {
				clientId = cookie.getValue();
			} 
			else if ("STATS.SID".equals(cookie.getName())) {
				sessionId = cookie.getValue();
			}
		}
	}
	//System.out.println("topDomain = " + request.getRequestURL().toString());
	//System.out.println("clientId = " + clientId);
	//System.out.println("sessionId = " + sessionId);
	
	if (clientId == null || clientId.length() == 0) {
		//System.out.println("request.getParameter clientId  " + request.getParameter("clientId"));
		//clientId = javascriptEscape(htmlEscape(request.getParameter("clientId"))); // from localStorage
		//if (clientId == null || clientId.length() == 0) {
			clientId = UUID.randomUUID().toString().replaceAll("-", "") + "|" + System.currentTimeMillis();
			Cookie clientIdCookie = new Cookie("STATS.CID", clientId);
			clientIdCookie.setDomain("." + topDomain);
			clientIdCookie.setPath("/");
			clientIdCookie.setMaxAge(Integer.MAX_VALUE);
			response.addCookie(clientIdCookie);
		//}
	}
	if (sessionId == null || sessionId.length() == 0) {
		//sessionId = javascriptEscape(htmlEscape(request.getParameter("sessionId"))); // from sessionStorage
		//if (sessionId == null || sessionId.length() == 0) {
			sessionId = UUID.randomUUID().toString().replaceAll("-", "");
			Cookie sessionIdCookie = new Cookie("STATS.SID", sessionId);
			sessionIdCookie.setDomain("." + topDomain);
			sessionIdCookie.setPath("/");
			response.addCookie(sessionIdCookie);
		//}
	}
	//System.out.println("clientId = " + clientId);
	//System.out.println("sessionId = " + sessionId);
	//System.out.println("account = " + account);
%>

;(function(window) {
	
	var account = '<%=account %>', version ='1.0', logBaseUrl = 'http://stats.eap.com:8080/PageStats/collect';
	
	var
	KEY_CLIENTID = 'STATS.CID',
	KEY_SESSIONID = 'STATS.SID',
	KEY_LASTACCESSTIME = 'STATS.LAT_' + account,
	KEY_META_PAGENO = 'STATS.pageNo',
	KEY_META_PAGEGROUP = 'STATS.pageGroup';
	
	var 
	win = window, 
	nav = win.navigator, 
	userAgent = nav.userAgent, 
	doc = win.document, 
	head = doc.getElementsByTagName('head')[0],
	body, 
	currDate = new Date(),
	addCookie = function(name, value) {
		var args = arguments,
			len = args.length,
			expires = (len > 2) ? args[2] : null,
			path = (len > 3) ? args[3] : '/',
			domain = (len > 4) ? args[4] : null,
			secure = (len > 5) ? args[5] : false;
			
		document.cookie = name + '=' + escape(value) + ((expires === null) ? '' : ('; expires=' + expires.toGMTString())) + ((path === null) ? '' : ('; path=' + path)) + ((domain === null) ? '' : ('; domain=' + domain)) + ((secure === true) ? '; secure' : '');
	},
	getCookieVal = function(offset) {
		var C = document.cookie, 
			endstr = C.indexOf(';', offset);
		if (endstr == -1) {
			endstr = C.length;
		}
		return unescape(C.substring(offset, endstr));
	},
	getCookie = function(name) {
		var C = document.cookie,
			arg = name + '=', 
			alen = arg.length,
			clen = C.length,
			i = 0,
			j = 0;
			
		while (i < clen) {
			j = i + alen;
			if (C.substring(i, j) == arg) {
				return getCookieVal(j);
			}
			i = C.indexOf(' ', i) + 1;
			if (i === 0) {
				break;
			}
		}
		return null;
	},
	cookieEnabled = (function(){
		var enabled = nav.cookieEnabled ? true : false;
		if (!enabled) {
			addCookie('testcookie', '1');
			if (getCookie('testcookie')) {
				enabled = true;
				addCookie('testcookie', '1', new Date());
			}
		}
		return enabled ? '1' : '0';
	})(),
	addEventListener = function(elem, type, listener) {
		if (elem.addEventListener) {
			elem.addEventListener(type, listener, false);
		} else if (elem.attachEvent) {
			elem.attachEvent('on' + type, listener);
		}
	},
	removeEventListener = function(elem, type, listener) {
		if (elem.addEventListener) {
			elem.removeEventListener(type, listener, false);
		} else if (elem.attachEvent) {
			elem.detachEvent('on' + type, listener);
		}
	},
	getFlashVersion = function() {
		var flashVersion = '';
		try {
		if (/msie/i.test(userAgent)) {
			var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
			if (swf) {
				var flashFullVerion = swf.GetVariable('$version').split(' ')[1].split(',');
				flashVersion = flashFullVerion[0] + (flashFullVerion.length > 1 ? '.' + flashFullVerion[1] : ''); // parseInt(swf.GetVariable('$version').split(' ')[1].split(',')[0]);
			}
		} else {
			if (nav.plugins && nav.plugins.length > 0) {
				var swf = nav.plugins['Shockwave Flash'];
				if (swf) {
					var words = swf.description.split(' ');
					for ( var i = 0; i < words.length; ++i) {
						if (isNaN(parseInt(words[i])))
							continue;
						flashVersion = words[i]; // parseInt(words[i]);
					}
				}
			}
		}
		} catch (e) {}
		return flashVersion;
	},
	logMetas = (function() {
		var logMetaNames = [ KEY_META_PAGENO, KEY_META_PAGEGROUP ];
		if (logMetaNames && logMetaNames.length > 0) {
			var metas = doc.getElementsByTagName('meta'), result = {};
			for (var i = 0; i < metas.length; i++) {
				var meta = metas[i], 
					metaName = meta.getAttribute('name');
				for (var logMetaNameIndex in logMetaNames) {
					if (logMetaNames[logMetaNameIndex] == metaName) {
						result[logMetaNames[logMetaNameIndex]] = meta.getAttribute('content');
					}
				}
			}
			return result;
		}
	})(),
	JSONP = function(url) {
		var script = document.createElement('script'), done = false;
		script.src = url;
		script.async = true;
		script.onload = script.onreadystatechange = function() {
			if ( !done && (!this.readyState || this.readyState === "loaded" || this.readyState === "complete") ) {
				done = true;
				script.onload = script.onreadystatechange = null;
				if ( script && script.parentNode ) {
					script.parentNode.removeChild( script );
				}
			}
		};
		head.appendChild(script);
	},
	toParamStr = function(params) {
		var v,arr = [];
		for (var n in params) {
			try {
				v = encodeURIComponent(params[n]); 
			} catch (e) {
				v = params[n];
			}
			arr.push(n + '=' + v);
		}
		return arr.join('&');
	},
	getEvent = function() {
		if (window.event) {
			return window.event;
		}
		
		var topArgs = arguments;
		while (topArgs.callee.caller) {
			topArgs = topArgs.callee.caller.arguments;
		}
		return topArgs[0];
	},
	mergeObj = function() {
		var args = arguments,
			result = {};
		for (var i = 0; i < args.length; i++) {
			if (args[i]) {
				for (var key in args[i]) {
					if (args[i][key]) {
						result[key] = args[i][key];
					}
				}
			}
		}
		return result;
	},
	log = function(logUrl) {
//		console.log(logUrl);
		JSONP(logUrl);
	};
	
	var ready = (function() {
		var readyBound = false, readyList = [], DOMContentLoaded;
		
		if (doc.addEventListener) {
			DOMContentLoaded = function() {
				doc.removeEventListener('DOMContentLoaded', DOMContentLoaded, false);
				ready();
			};
		} else if (doc.attachEvent) {
			DOMContentLoaded = function() {
				if (doc.readyState === 'complete') {
					doc.detachEvent('onreadystatechange', DOMContentLoaded);
					ready();
				}
			};
		}
		
		function ready() {
			if (!ready.isReady) {
				ready.isReady = true;
				for (var i = 0, j = readyList.length; i < j; i++) {
					readyList[i]();
				}
			}
		}
		function doScrollCheck(){
			try {
				doc.documentElement.doScroll("left");
			} catch(e) {
				setTimeout( doScrollCheck, 1 );
				return;
			}
			ready();
		}
		function bindReady() {
			if (readyBound) {
				return;
			}
			readyBound = true;
 
			if (doc.addEventListener) {
				doc.addEventListener('DOMContentLoaded', DOMContentLoaded, false);
				win.addEventListener('load', ready, false);
			} else if (doc.attachEvent) {
				doc.attachEvent('onreadystatechange', DOMContentLoaded);
				win.attachEvent('onload', ready);
				
				var toplevel = false;
				try {
					toplevel = W.frameElement == null;
				} catch (e) {}
 
				if (doc.documentElement.doScroll && toplevel) {
					doScrollCheck();
				}
			}
		}
		bindReady();
 
		return function(callback) {
			ready.isReady ? callback() : (readyList[readyList.length] = callback);
		};
	})();
	ready.isReady = false;
	
	/* ------ INIT ----- */
	var sessionStorage,localStorage;
	try {
		sessionStorage = win.sessionStorage;
	} catch (e) {}
	try {
		localStorage = win.localStorage;
	} catch (e) {}
	
	var clientId = getCookie(KEY_CLIENTID);
	if (!clientId && localStorage) {
		clientId = localStorage.getItem(KEY_CLIENTID);
	}
	if (!clientId) {
		clientId = '<%=clientId%>';
		localStorage && (localStorage.setItem(KEY_CLIENTID, clientId));
	}

	var sessionId = getCookie(KEY_SESSIONID);
	if (!sessionId && sessionStorage) {
		sessionId = sessionStorage.getItem(KEY_SESSIONID);
	}
	if (!sessionId) {
		sessionId = '<%=sessionId%>';
		sessionStorage && (sessionStorage.setItem(KEY_SESSIONID, sessionId));
	}
	
	function getLogBaseInfo(logType) {
		return {
			logType: logType,
			account: account,
			clientId: clientId || '', // TODO
			sessionId: sessionId || '', // TODO
			version: version,
			accessTime: new Date().getTime()
		};
	}
	
	var browserInfo;
	ready(function() {
		body = doc.body;

		var screen = win.screen;
		browserInfo = {
			javascriptEnabled: '1',
			cookieEnabled: cookieEnabled,
			javaEnabled: nav.javaEnabled() ? '1' : '0',
			flashVersion: getFlashVersion(),
			language: nav.language || nav.userLanguage || '',
			charset: doc.charset || doc.defaultCharset || '',
			browserRectangle: (win.innerWidth || body.offsetWidth || '') + 'x' + (win.innerHeight || body.offsetHeight || ''),
			screenRectangle: screen.width + 'x' + screen.height,
			screenColorDepth: screen.colorDepth || screen.pixelDepth,
			timeZone: currDate.getTimezoneOffset() / 60 * -1,
			timeZoneHours: currDate.getHours()
		};
	});
	
	var pendingEvents = window._stats || []; // TODO
	window._stats = {};
	_stats._trackEvent = function(category, action, optLabel, optValue) { // readyed
		var e = getEvent(),
		logUrl = logBaseUrl + '?' + toParamStr(mergeObj(getLogBaseInfo('event'), {
//			title: doc.title || '',
//			metaPageNo: logMetas[KEY_META_PAGENO] || '',
//			metaCampaign: logMetas[KEY_META_CAMPAIGN] || '',
			eventType: e.type,
			eventX: e.clientX + document.documentElement.scrollLeft + (body ? body.scrollLeft : 0),
			eventY: e.clientY + document.documentElement.scrollTop + (body ? body.scrollTop : 0),
			category: category || '',
			action: action || '',
			optLabel: optLabel || '',
			optValue: optValue || ''
		}));
		log(logUrl);
	};
	_stats._trackPageview = function(url, title, metaPageNo, metaPageGropu) {
		var logUrl = logBaseUrl + '?' + toParamStr(mergeObj(getLogBaseInfo('pageAjax'), browserInfo, {
			referer: win.location.href,
			url: url,
			title: doc.title || '',
			metaPageNo: logMetas[KEY_META_PAGENO] || '',
			metaPageGroup: logMetas[KEY_META_PAGEGROUP] || ''
		}));
		log(logUrl);
	};
	
	/* ------ do pageview log ----- */
	var doPageviewLog = function() {
		var currTimestamp = new Date().getTime(),
			accessTimeInterval,
			lastAccessTime = getCookie(KEY_LASTACCESSTIME);
		if (!lastAccessTime && sessionStorage) {
			lastAccessTime = sessionStorage.getItem(KEY_LASTACCESSTIME);
		}
		sessionStorage && (sessionStorage.setItem(KEY_LASTACCESSTIME, currTimestamp));
		addCookie(KEY_LASTACCESSTIME, currTimestamp);
		accessTimeInterval = currTimestamp - (lastAccessTime || 0);
		
		var logUrl = logBaseUrl + '?' + toParamStr(mergeObj(getLogBaseInfo('page'), browserInfo, {
			referer: doc.referrer,
			accessTimeInterval: accessTimeInterval,
			title: doc.title || '',
			metaPageNo: logMetas[KEY_META_PAGENO] || '',
			metaPageGroup: logMetas[KEY_META_PAGEGROUP] || ''
		}));
		log(logUrl);
	};
	ready(doPageviewLog);
})(window);