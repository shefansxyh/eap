/* chiknin@gmail.com */
package agent

import (
	"fmt"
	"net/http"
	"text/template"
	"time"
)

func StatsHandle(w http.ResponseWriter, r *http.Request) {
	//	err := r.ParseForm()
	//	if err != nil {
	//        return
	//    }

	account := JavascriptEscape(r.FormValue("A"))
	//url := "http://" + r.Host + r.RequestURI
	topDomain := GetTopDomain(r.Host)
	collectUrl := "collect" // http://stats.eap.com:8888/

	statsCidCookie, _ := r.Cookie("STATS.CID")
	statsCid := ""
	if statsCidCookie != nil {
		statsCid = statsCidCookie.Value
	}
	if statsCid == "" {
		statsCid = JavascriptEscape(fmt.Sprintf("%d", time.Now().UnixNano()))
		cookie := http.Cookie{Name: "STATS.CID", Value: statsCid, Domain: "." + topDomain, Path: "/", MaxAge: 86400000}
		http.SetCookie(w, &cookie)
	}

	statsSidCookie, _ := r.Cookie("STATS.SID")
	statsSid := ""
	if statsSidCookie != nil {
		statsSid = statsSidCookie.Value
	}
	if statsSid == "" {
		statsSid = JavascriptEscape(fmt.Sprintf("%d", time.Now().UnixNano()))
		cookie := http.Cookie{Name: "STATS.SID", Value: statsSid, Domain: "." + topDomain, Path: "/"}
		http.SetCookie(w, &cookie)
	}

	w.Header().Set("Content-Type", "application/javascript")
	w.Header().Set("P3P", "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"")

	//tpl := template.New("htmlTemplate")
	//tpl = tpl.Funcs(template.FuncMap{"unescaped", unescaped})
	tpl, err := template.ParseFiles("./template/html/stats.html")
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		//fmt.Println(err.Error())
	}

	data := map[string]string{"account": account, "statsCid": statsCid, "statsSid": statsSid, "collectUrl": collectUrl}

	err = tpl.Execute(w, data)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		//fmt.Println(err.Error())
	}
}

//func unescaped(x string) interface{} {
//	return template.HTML(x)
//}
