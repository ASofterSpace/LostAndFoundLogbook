# Lost and Found Logbook

**Class:** Main Application

**Language:** Java

**Platform:** Windows / Linux Server, Web Frontend

An application consisting of a backend server and (potentially several) web frontends showing info about lost and found items

## Setup

Download our Toolbox-Java (which is a separate project here on github) into an adjacent directory on your hard drive.

Start the build by calling under Windows:

```
build.bat
```

Or under Linux:

```
build.sh
```

## Run

To start up the Lost and Found Logbook server after it has been built, you can call under Windows:

```
run.bat
```

Or under Linux:

```
run.sh
```

Once the server is started, you can connect to it from a local webbrowser - or if you make the server machine available to the outside world, then also from there. :)

Btw. - the server uses a directory called `server` in which the page itself is kept in pieces, and a second directory called `deployed` in which the same data is fully templated, ready to be served.
Finally, a directory called `data` is used to store the actual data (so information about the actual lost and found objects.)

When running this on `localhost`, and you don't want to tell firefox again and again that using the webcam for taking photos is allowed, you can enter `about:config` in the firefox URL bar, go to `media.navigator.permission.disabled` and set it to `true`.
However, again, this should only be used when running this site on `localhost` - as soon as you connect to the real (and scary) internet again, you should switch this back to the default (`false`) such that you actually get asked before websites start doing whatever they want!

## Test

To run the Lost and Found Logbook selftest after it has been built, you can call it with the argument `--test`, or for short under Windows:

```
test.bat
```

Or under Linux:

```
test.sh
```

## License

We at A Softer Space really love the Unlicense, which pretty much allows anyone to do anything with this source code.
For more info, see the file UNLICENSE.

If you desperately need to use this source code under a different license, [contact us](mailto:moya@asofterspace.com) - I am sure we can figure something out.
