# Using GitHub Actions to find errors in your mod.

GitHub actions is a very useful service that is provided free to public
and private repositories. It allows you to automatically run commands or
\*actions\* when an event happens.

To find errors, we'll be using the `push` and `pull_request` events.

## Getting Started

First of all, create a file called error\_action.yml in
`.github/workflows/` where your git repository is.

Put the following inside:

```yaml
name: Gradle Error Checker

on: [ push, pull_request ]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - name: Checkout
        uses: actions/checkout@v2
        with:
          fetch-depth: 0
```

This will run the `Checkout` step when a pull request is made or a
commit is push. However, we want it to lint our code and find those darn
errors.

Luckily for us, GitHub has a built in tool that can help us with that
called SuperLinter.

To add super linter, add the following underneath the `Checkout` step.

```yaml
- name: Check for errors.
        uses: github/super-linter@v3
        env:
          VALIDATE_ALL_CODEBASE: false
```

The `VALIDATE_ALL_CODEBASE: false` option makes sure we dont waste our
time linting stuff that has already been checked. Superlinter will only
check the files that have been changed in the commit or pull request.
You can set this to true if you want to check the entire code.

Now, if an error is found. Github reports it to us!

Aha! GitHub has found an error for us!

```java
/home/runner/work/MyRepo/MyMod/src/main/java/com/mymod/blocks/MyModBlocks.java:65: error: variable RAINBOW_GRASS not initialized in the default constructor
    public static final Block RAINBOW_GRASS;
```

