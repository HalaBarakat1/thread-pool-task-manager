## Thread Pool Assignment (Parallel Computing)

This repository is based on [wagnerjfr/web-http-socket-server-task-manager-docker](https://github.com/wagnerjfr/web-http-socket-server-task-manager-docker), extended for the Thread Pool assignment. The full report is in [`report/Thread-Pool-Report.pdf`](report/Thread-Pool-Report.pdf).

| Request | Changes |
|---|---|
| 1. Run and explore | No code changes |
| 2. New task | `src/task/TaskPrimeCount.java`: counts the primes in a range (`TaskPrimeCount&1,1000000` → `78498`) |
| 3. Concurrency test | `TaskExecutor.java`: START/END logs with time and thread name. `TasksList.java`: thread-safe list. `WebServerSocket.java`: choose the pool type (`java WebServerSocket` → cached, `java WebServerSocket 2` → fixed(2)). `test-concurrency.ps1`: sends 6 requests at the same time |
| 4. Docker | `Dockerfile`: base image `eclipse-temurin:8-jdk-alpine` and a `POOL_SIZE` environment variable |

```bash
docker build -t task-manager .
docker run -d -p 8080:8000 --name task-manager task-manager
docker run -d -p 8081:8000 -e POOL_SIZE=2 --name task-manager-fixed task-manager
curl --http0.9 -X POST -d "TaskPrimeCount&1,1000000" http://localhost:8080
```

---

# Multi-threaded WebServers tasks executor using WebHttpServer and WebServerSocket

This project aims to create two kinds of multi-threaded web servers (with thread-pooling) for executing simple tasks.
There are three tasks implemented and the user can add more (see details below).
The web servers receive the command and the parameter(s) for running a task through terminal command `curl`.

_P.S. There is another project where I used *ServerSocker* in GitHub: [localhost-chat-socket](https://github.com/wagnerjfr/localhost-chat-socket)_

## Full article
### [Unveiling Two Dynamic Multi-Threaded Web Servers for Task Execution](https://levelup.gitconnected.com/unveiling-two-dynamic-multi-threaded-web-servers-for-task-execution-62644e78a04a)
_Different Type of Web Serves Using HttpServer and ServerSocket_

## WebServers
* **WebServerHttp** is developed using *HttpServer* [[javadoc](https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html)].

* **WebServerSocket** is developed using *ServerSocket* [[javadoc](https://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html)].

Both web servers use *ExecutorService* [[javadoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html)] that executes each submitted task in the pooled thread, more specifically, `Executors.newCachedThreadPool()` which is an unbounded thread pool with automatic thread reclamation [[javadoc](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Executors.html#newCachedThreadPool--)].
