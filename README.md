# Fake data generator
This tool is intended for generating fake data or test data, with help of [Faker](https://github.com/DiUS/java-faker)
java library, and storing this data to CSV or Parquet files. Currently, only few list of faker are used in the
application, which are listed inside the [AvailableFakers](./src/main/java/preved/medved/generator/source/AvailableFakers.java)
file. But there is a simple way to add necessary list of them, which should be described below.

## How to build the tool
The application is build with help of Maven and JDK of following versions:

```shell
> mvn --version
Apache Maven 3.9.1
Maven home: ...
...

> java --version
openjdk 21.0.2 2024-01-16 LTS
OpenJDK Runtime Environment Zulu21.32+17-CA (build 21.0.2+13-LTS)
OpenJDK 64-Bit Server VM Zulu21.32+17-CA (build 21.0.2+13-LTS, mixed mode, sharing)
```

But it can be built by older versions of Maven and JDK as well.
To build the application, the following command should be used:

```shell
> mvn install
...

```

The prepared artefact can be found inside the `target` folder:
```shell
> ls -l target/fake-data-generator*.jar
```

## How to use
The built, on the previous step, java artefact is self-sufficient library, which contain what it's needed. Therefore,
in order to launch the application. the following command should be used:

```shell
> java -jar target/fake-data-generator-1.2-SNAPSHOT.jar --help
Usage: fake-data-generator [options]
  Options:
  * --fakers
      Space- or comma-separated list of fakers which will be used in specified
      order. Current available list of fakers: book, beer, cat, dog, finance
    --destination-folder, -p
      Where to put of generated files
    --file-size, -s
      Minimum size of data file for generating (in MiB). Default: 10
    --amount-files, -n
      Amount of data files for generating. Default: 1
    --csv
      CSV format will be used as output
      Default: false
    --parquet
      Parquet format will be used as output
      Default: false
    --help
      Shows this help message
```

In order to generate test data by `Dog` faker, to 1 CSV file of 100 MiB size, the following command is used:

```shell
> java -jar target/fake-data-generator-1.2-SNAPSHOT.jar -p target/ --csv --fakers dog -s 100 -n 1
```

The same, but by `Book` and `Beer` (order matters), and to store data to Parquet file, the command should be:

```shell
> java -jar target/fake-data-generator-1.2-SNAPSHOT.jar -p target/ --parquet --fakers book,beer -s 100 -n 1
```
Parameters like `--parquet` and `--csv` may be specified simultaneously.

### How to check
Regarding of generated data size, CSV file will have little more size than was requested by `--file-size | -s` args,
because commas for splitting records are not counted in the data amount counter. But, meantime, file size of Parquet
format will be less drastically, because of some data compression.

CSV (stands for `comma separated value`) is text file format, correctness of which can be checked by default *nix tools
like: `less`, `head`, `tail`, `wc`.

For checking of Parquet file format, `spark` and `spark-shell` command might be used:
```shell
> spark-shell
...
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 3.5.0
      /_/

Using Scala version 2.12.18 (OpenJDK 64-Bit Server VM, Java 21.0.2)
Type in expressions to have them evaluated.
Type :help for more information.
...

scala> val parquetFileDF = spark.read.parquet("./target/e085dff8-bf28-45ca-80fb-77b9af297852.parquet")
parquetFileDF: org.apache.spark.sql.DataFrame = [dog_name: string, dog_breed: string ... 6 more fields]

scala> parquetFileDF.show(2)
+--------+-----------+---------+---------------+-------+---------------+----------+-----------+
|dog_name|  dog_breed|dog_sound|dog_meme_phrase|dog_age|dog_coat_length|dog_gender|   dog_size|
+--------+-----------+---------+---------------+-------+---------------+----------+-----------+
|     Sam|Toy Terrier|  grrrrrr| smol pupperino|  young|           long|      male|      small|
|    Abby|   Keeshond|  grrrrrr|          11/10|  adult|         medium|    female|extra large|
+--------+-----------+---------+---------------+-------+---------------+----------+-----------+
only showing top 2 rows


scala> parquetFileDF.count()
res0: Long = 1885752
```

## Environment preparation before development

1. Install python virtual environment with help of command `python3.9 -m virtualenv -p python3.9 .venv`.
2. Activate this environment by `source .venv/bin/activate`
3. Install [pre-commit](https://pre-commit.com/) tool with help of command `pip install pre-commit`.
4. Install the PR-commit hook by executing `pre-commit install` inside project directory.

## How to add more fakers
To add one or few fakers to the tool, it' required to fulfil following actions:
1. clone the repo.
2. add new faker class to the `preved.medved.generator.source.faikers` package, which should extend the
[AbstractDataProducer](./src/main/java/preved/medved/generator/source/faikers/AbstractDataProducer.java).
3. fill `dataFetchers` list of new faker, inside its constructor, by analogy with already existing fakers. How
many thread should be used to prepare one row of data, depends on performance of the faker type of
[Faker](https://github.com/DiUS/java-faker).
4. add new enum item inside the file [AvailableFakers](./src/main/java/preved/medved/generator/source/AvailableFakers.java).
5. update the description in the file [FakersRelatedArgs](./src/main/java/preved/medved/cli/FakersRelatedArgs.java).
6. build the application and test its behaviour.

## Performance tests of the application
In order to figure out how to reach out, or, at least, to approach to the best performance configuration, some
"performance tests" have been run. The application was run on the environment with following configuration:
* Machine: MacBook Pro, 13-inch, 2020
  * Processor: 2,3 GHz Quad-Core Intel Core i7
  * Graphics: Intel Iris Plus Graphics 1536 MB
  * Memory: 32 GB 3733 MHz LPDDR4X
* OS: macOS 14.3.1 (23D60)
* Java: OpenJDK Runtime Environment Zulu21.32+17-CA (build 21.0.2+13-LTS)

Just for simplicity, amount of data to generate has been chosen to 1 file of 100MiB, i.e. args `--file-size | -s` and
`--amount-files | -n` were specified to **100** and **1** respectively. Also, different types of thread pools were
specified through arg `--thread-pool-type`. The results are presented below:

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
