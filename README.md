# Fake data generator tool
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

Just for simplicity, amount of data to generate has been chosen to 1 file of 100MiB, i.e. args `--file-size | -s` and `--amount-files | -n` were specified to **100** and **1** respectively. Also, different types of thread pools were specified through arg `--thread-pool-type`. The results are presented below:

| Thread Pool Name<br/>(--thread-pool-type) | List of fakers<br/>(--fakers arg) | Amount of threads <br/>for getting data | Elapsed time |
|:------------------------------------------|:----------------------------------|:---------------------------------------:|:------------:|
| CachedThread                              | book,cat,beer,finance,dog         | sum from each of used fakers(see below) |   0:01:16    |
| WorkStealing                              | book,cat,beer,finance,dog         | sum from each of used fakers(see below) |   0:01:02    |
| SingleThread                              | book,cat,beer,finance,dog         | sum from each of used fakers(see below) |   0:04:28    |
| CachedThread                              | book                              |                    2                    |   0:00:23    |
| WorkStealing                              | book                              |                    2                    |   0:00:31    |
| SingleThread                              | book                              |                    2                    |   0:00:43    |
| CachedThread                              | cat                               |                    1                    |   0:00:12    |
| WorkStealing                              | cat                               |                    1                    |   0:00:09    |
| SingleThread                              | cat                               |                    1                    |   0:00:12    |
| CachedThread                              | beer                              |                    1                    |   0:00:10    |
| WorkStealing                              | beer                              |                    1                    |   0:00:08    |
| SingleThread                              | beer                              |                    1                    |   0:00:12    |
| CachedThread                              | finance                           |                    3                    |   0:07:03    |
| WorkStealing                              | finance                           |                    3                    |   0:07:48    |
| SingleThread                              | finance                           |                    3                    |   0:25:10    |
| CachedThread                              | dog                               |                    2                    |   0:00:28    |
| WorkStealing                              | dog                               |                    2                    |   0:00:17    |
| SingleThread                              | dog                               |                    2                    |   0:00:22    |
