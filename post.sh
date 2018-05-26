curl -d '{"amount":2334, "timestamp": 0}' -H "Content-Type: application/json; charset=UTF-8" -X POST http://localhost:8080/transactions --verbose
curl -d '{"amount":42, "timestamp": 1}' -H "Content-Type: application/json; charset=UTF-8" -X POST http://localhost:8080/transactions --verbose
curl -d '{"amount":633, "timestamp": 2}' -H "Content-Type: application/json; charset=UTF-8" -X POST http://localhost:8080/transactions --verbose
curl -d '{"amount":1412, "timestamp": 3}' -H "Content-Type: application/json; charset=UTF-8" -X POST http://localhost:8080/transactions --verbose
