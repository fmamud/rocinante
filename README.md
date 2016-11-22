# rocinante

Test suite for integration tests

![Rocinante Horse](http://www.artnet.com/WebServices/images/ll00162lldQkOGFgVeECfDrCWvaHBOcZVDC/oskar-garvens-don-quixote-de-la-mancha-upon-his-horse-rocinante.jpg)

> **Rocinante** is [Don Quixote](https://en.wikipedia.org/wiki/Don_Quixote)'s donkey in the novel Don Quixote by _Miguel de Cervantes_. In many ways, Rocinante is not only Don Quixote's horse, but also his double: like Don Quixote, he is awkward, past his prime, and engaged in a task beyond his capacities.

## Project

Rocinante project is basically a [Spock](http://spockframework.org) extension that reads a mapping's tape directory and generate dynamic feature methods [Wiremock](http://wiremock.org/docs/record-playback) based.

`@Rocinante`: Field's annotation binder for mapping files

- `directory`: base path for files;
- `mappings`: mappings directory (default value: "mappings");
- `tapes`: tapes directory (default value: "__files");

`RocinanteExtension`: A Spock extension where the stuffs happen.

`RocinanteInterceptor`: A Spock interceptor that writes a field with `@Rocinante` annotation binder.

## Run

```bash
./gradlew test
```
