# glazy
Git web API wrapper written in Kotlin. Its git, but all command line for when a gui is just too much effort.

# First Steps
- [ ] ~~Initial Project Setup~~
- [ ] ~~Basic API usage~~
  - [ ] ~~Parse JSON to Kotlin class~~
  - [ ] ~~Parse Kotlin class to JSON~~
  - [ ] ~~Basic GET requests~~
  - [ ] ~~Basic POST Requests~~
- [ ] Basic request handling
  - [ ] Test for internet connection
  - [ ] Request Stack
- [ ] ~~CLI~~

# Basic Commands
For all commands and subcommands, pass `glazy <command> <subcommand> --help` for more complete usage information.
## issue
Perform operations on issues. Currently only requests issues of repositories the user currently 'resides' in. Meaning are in a directory or are a child of a driectory containting the `.git` directory.
### list
Used to list all issues or a specific issue by number.

`glazy issue list` Will list all issues associated with the repository.

`glazy issue list --number 0` Will list the repository issue with the number 0.
### add
Add an issue to the repository.

`glazy add --title 'Sample Issue' --body 'Sample body'`
### patch
Edit or 'patch' an existing issue on the remote repository.

`glazy issue patch --state CLOSED -n 0` Will mark issue 0 as closed.

## repo
Allows the user to specify the repository to make operations to.
### show
Display information about a repository.

`glazy repo show` Will show information about the repository the usuer currently 'resides' in.

`glazy repo show --user foor --name bar` Will show information about the repository owned by 'foo' and called 'bar'.
### init
Creates a remote repository.

`glazy repo init --name bar --private` Creates an empty remote repository.
### patch
Edits a repository.

`glazy repo patch --user foo --name bar --public` Makes the repository 'bar' owned by 'foo' public, assuming appropriate permissions.
### delete
`glazy repo delete --user foo --name bar` Deletes the repository 'bar' owned by 'foo', assuming the appropriate permissions.

# Development resources
[Kotlin Coding Conventions](https://kotlinlang.org/docs/reference/coding-conventions.html)

[Kotlin Docs](https://kotlinlang.org/docs/reference/)

[Git API docs](https://developer.github.com/v3/)

[Apache Maven Docs](https://maven.apache.org/guides/getting-started/index.html)

[khhtp Docs](https://khttp.readthedocs.io/en/latest/)

[Picocli Docs](https://picocli.info/)

[Jackson Docs](https://github.com/FasterXML/jackson-docs)
