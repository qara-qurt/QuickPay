postgres:
	docker run -d --name postgres_quick_pay \
		-e POSTGRES_USER=dias \
		-e POSTGRES_PASSWORD=qaraqurt \
		-e POSTGRES_DB=quick-pay \
		-p 5432:5432 postgres:latest
