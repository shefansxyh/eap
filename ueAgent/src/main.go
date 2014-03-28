package main

import (
	"./agent"
	"html/template"
	"net/http"
)

func main() {
	http.HandleFunc("/stats", agent.StatsHandle)
	http.HandleFunc("/collect", agent.CollectHandle)

	http.HandleFunc("/test.html", func(w http.ResponseWriter, r *http.Request) {
		tpl, err := template.ParseFiles("./template/html/test.html")
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
		}

		err = tpl.Execute(w, nil)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
		}
	})

	http.ListenAndServe(":8888", nil)
}
