/* chiknin@gmail.com */
package agent

import (
	"net/http"
	"regexp"
	"strings"
)

func HtmlEscape(input string) string {
	result := ""
	for _, v := range input {
		if v == '&' {
			result += "&amp;"
		} else if v == '"' {
			result += "&quot;"
		} else if v == '<' {
			result += "&lt;"
		} else if v == '>' {
			result += "&gt;"
		} else if v == '\'' {
			result += "&#39;"
		} else if v == '/' {
			result += "&#092;"
		} else {
			result += string(v)
		}
	}
	return result
}

func JavascriptEscape(input string) string {
	result := ""
	for _, v := range input {
		if v == '"' {
			result += "\""
		} else if v == '\'' {
			result += "\\'"
		} else if v == '\\' {
			result += "\\\\"
		} else {
			result += string(v)
		}
	}
	return result
}

//func GetDomain(url string) string {
//	if !strings.Contains(url, "://") {
//		return ""
//	}

//	_url := strings.Split(url, "://")[1]
//	if strings.Contains(_url, "?") {
//		_url = strings.Split(_url, "?")[0]
//	}
//	if strings.Contains(_url, "/") {
//		_url = strings.Split(_url, "/")[0]
//	}

//	return _url
//}

//func GetTopDomain(url string) string {
//	domain := getDomain(url)
//	if domain == "" {
//		return ""
//	}

//	domainSubfixs := []string{".com", ".edu", ".gov", ".int", ".net", ".biz", ".info", ".pro", ".name", ".museum", ".coop", ".aero", ".idv", ".cc", ".tv", ".cn", ".hk", ".mo", ".tw"}

//	for _, v := range domainSubfixs {
//		if strings.HasSuffix(domain, v) {
//			_domainHead := strings.Split(domain, v)[0]
//			_domainHeadArray := strings.Split(_domainHead, ".")

//			return _domainHeadArray[len(_domainHeadArray)-1] + v
//		}
//	}

//	return ""
//}

func GetTopDomain(host string) string {
	_host := strings.Split(host, ":")
	domain := _host[0]

	domainSubfixs := []string{".com", ".edu", ".gov", ".int", ".net", ".biz", ".info", ".pro", ".name", ".museum", ".coop", ".aero", ".idv", ".cc", ".tv", ".cn", ".hk", ".mo", ".tw"}

	for _, v := range domainSubfixs {
		if strings.HasSuffix(domain, v) {
			_domainHead := strings.Split(domain, v)[0]
			_domainHeadArray := strings.Split(_domainHead, ".")

			return _domainHeadArray[len(_domainHeadArray)-1] + v
		}
	}

	return ""
}

func GetRemoteAddr(r *http.Request) string {
	ip := r.Header.Get("")
	if len(ip) == 0 || strings.EqualFold("unknown", ip) {
		ip = r.Header.Get("Proxy-Client-IP")
	}
	if len(ip) == 0 || strings.EqualFold("unknown", ip) {
		ip = r.Header.Get("WL-Proxy-Client-IP")
	}
	if len(ip) == 0 || strings.EqualFold("unknown", ip) {
		ip = r.Header.Get("HTTP_CLIENT_IP")
	}
	if len(ip) == 0 || strings.EqualFold("unknown", ip) {
		ip = r.Header.Get("HTTP_X_FORWARDED_FOR")
	}
	if len(ip) == 0 || strings.EqualFold("unknown", ip) {
		ip = r.RemoteAddr // TODO ip:port
	}
	if len(ip) > 0 {
		ips := regexp.MustCompile(" |,").Split(ip, -1)
		ip = ips[len(ips)-1]
	}

	return ip
}
