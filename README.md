# glazy
Git web API wrapper written in Kotlin. Its git, but all command line for when a gui is just too much effort.

# Build
Run `mvn package`. Add repository location to PATH environment variable.

# Usage
```
Usage: glazy [-hV] [COMMAND]
A command line interface to the github api.
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  issue   Perform operations on repository issues
  repo    Perform operations on a  repository.
  cache   Perform operations on the glazy cache.
  pull    Perform operations on pull requests
  label   Perform operations on repository labels
  collab  Perform operations on repository collaborators.
```

Pass the `--help` option to any subcommand to view its usage text.

# Development resources
[Kotlin Coding Conventions](https://kotlinlang.org/docs/reference/coding-conventions.html)

[Kotlin Docs](https://kotlinlang.org/docs/reference/)

[Git API docs](https://developer.github.com/v3/)

[Apache Maven Docs](https://maven.apache.org/guides/getting-started/index.html)

[Khhtp Docs](https://khttp.readthedocs.io/en/latest/)

[Picocli Docs](https://picocli.info/)

[Jackson Docs](https://github.com/FasterXML/jackson-docs)
