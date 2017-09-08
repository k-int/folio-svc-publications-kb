echo Submit title_1

curl -vX GET "http://localhost:8080/instances/search?q=oncurr"
curl -v -X POST http://localhost:8080/instances/resolve -H "Content-Type: application/json" -d @title_1.json
curl -v -X POST http://localhost:8080/instances/resolve -H "Content-Type: application/json" -d @fake_title_1.json
curl -v -X POST http://localhost:8080/instances/resolve -H "Content-Type: application/json" -d @fake_title_2.json
curl -vX GET "http://localhost:8080/instances/search?q=%"
