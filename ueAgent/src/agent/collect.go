/* chiknin@gmail.com */
package agent

import (
	"fmt"
	"net/http"
)

func CollectHandle(w http.ResponseWriter, r *http.Request) {
	//charset := r.FormValue("charset")
	//r.TransferEncoding = []string{charset, "UTF-8"}

	logMsg := [5]string{GetRemoteAddr(r), r.RequestURI, r.Header.Get("cookie"), r.Referer(), r.UserAgent()}
	fmt.Println(logMsg)

	w.Header().Set("Cache-Control", "private, max-age=0, no-cache")
	w.Header().Set("Connection", "close")
	w.Header().Set("Pragma", "no-cache")
	w.Header().Set("Content-Type", "application/javascript")
}
