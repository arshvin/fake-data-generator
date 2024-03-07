# fake-data-generator
Maven project generated with `com.a9ski:quick-start` archetype.

## Environment preparation before development

1. Install python virtual environment with help of command `python3.9 -m virtualenv -p python3.9 .venv`.
2. Activate this environment by `source .venv/bin/activate`
3. Install [pre-commit](https://pre-commit.com/) tool with help of command `pip install pre-commit`.
4. Install the PR-commit hook by executing `pre-commit install` inside project directory.

## Performance tests of the application
In order to figure out how to reach out, or, at least, to approach to the best performance configuration of configuration, some "performance tests" have been run. The application was run on the environment with following configuration:
* the machine: MacBook Pro, 13-inch, 2020
  * Processor: 2,3 GHz Quad-Core Intel Core i7
  * Graphics: Intel Iris Plus Graphics 1536 MB
  * Memory: 32 GB 3733 MHz LPDDR4X
* OS: macOS 14.3.1 (23D60)
* Java: OpenJDK Runtime Environment Zulu21.32+17-CA (build 21.0.2+13-LTS)

| Amount of MiB<br/>(--file-size arg) | Thread Pool<br/>Name          | List of fakers<br/>(--fakers arg) | Amount of threads <br/>for getting data | Elapsed time |
|:-----------------------------------:|:------------------------------|:----------------------------------|:---------------------------------------:|:------------:|
|                 100                 | CachedThreadPool              | book,cat,beer,finance,dog         | sum from each of used fakers(see below) |   0:01:16    |
|                 100                 | WorkStealingPool              | book,cat,beer,finance,dog         | sum from each of used fakers(see below) |   0:01:02    |
|                 100                 | SingleThreadScheduledExecutor | book,cat,beer,finance,dog         | sum from each of used fakers(see below) |   0:04:28    |
|                 100                 | CachedThreadPool              | book                              |                    2                    |   0:00:23    |
|                 100                 | WorkStealingPool              | book                              |                    2                    |   0:00:31    |
|                 100                 | SingleThreadScheduledExecutor | book                              |                    2                    |   0:00:43    |
|                 100                 | CachedThreadPool              | cat                               |                    1                    |   0:00:12    |
|                 100                 | WorkStealingPool              | cat                               |                    1                    |   0:00:09    |
|                 100                 | SingleThreadScheduledExecutor | cat                               |                    1                    |   0:00:12    |
|                 100                 | CachedThreadPool              | beer                              |                    1                    |   0:00:10    |
|                 100                 | WorkStealingPool              | beer                              |                    1                    |   0:00:08    |
|                 100                 | SingleThreadScheduledExecutor | beer                              |                    1                    |   0:00:12    |
|                 100                 | CachedThreadPool              | finance                           |                    3                    |   0:07:03    |
|                 100                 | WorkStealingPool              | finance                           |                    3                    |   0:07:48    |
|                 100                 | SingleThreadScheduledExecutor | finance                           |                    3                    |   0:25:10    |
|                 100                 | CachedThreadPool              | dog                               |                    2                    |   0:00:28    |
|                 100                 | WorkStealingPool              | dog                               |                    2                    |   0:00:17    |
|                 100                 | SingleThreadScheduledExecutor | dog                               |                    2                    |   0:00:22    |
