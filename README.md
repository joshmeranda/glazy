# glazy
Git web API wrapper written in Kotlin. Its git, but all command line for when a gui is just too much effort.

# NOTE

Be aware this project aimed to augment some missing functionality from the official [github command line](https://cli.github.com/).
That tool has implement most if not all of this project's functionality has been implemented and this project will no
longer be maintained, and should not be used in favor of the official tool.

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

# Configuration
Much of the functionality of this project relies on the local git configuration, specifically the `github.user` and
`github.token` fields. To properly configure these fields please use the following commands:

```shell script
git config --global github.user <username>
git config --global github.token <token>
```

# Development resources
[Kotlin Coding Conventions](https://kotlinlang.org/docs/reference/coding-conventions.html)

[Kotlin Docs](https://kotlinlang.org/docs/reference/)

[Git API docs](https://developer.github.com/v3/)

[Apache Maven Docs](https://maven.apache.org/guides/getting-started/index.html)

[Khhtp Docs](https://khttp.readthedocs.io/en/latest/)

[Picocli Docs](https://picocli.info/)

[Jackson Docs](https://github.com/FasterXML/jackson-docs)
